package com.asgardiateam.aptekaproject.service;

import com.asgardiateam.aptekaproject.entity.User;
import com.asgardiateam.aptekaproject.entity.dynamicquery.criteria.UserCriteria;
import com.asgardiateam.aptekaproject.entity.dynamicquery.specifications.UserSpecifications;
import com.asgardiateam.aptekaproject.exception.AptekaException;
import com.asgardiateam.aptekaproject.mapper.UserMapper;
import com.asgardiateam.aptekaproject.payload.UserDTO;
import com.asgardiateam.aptekaproject.payload.request.UserRequest;
import com.asgardiateam.aptekaproject.repository.UserRepository;
import com.asgardiateam.aptekaproject.service.interfaces.UserService;
import com.asgardiateam.aptekaproject.utils.Page2Dto;
import com.asgardiateam.aptekaproject.utils.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserDTO create(UserRequest userRequest) {
        return userMapper.toDTO(save(userMapper.toCreate(userRequest)));
    }

    @Override
    public UserDTO update(UserRequest userRequest, Long id) {
        return userMapper.toDTO(userMapper.toUpdate(userRequest, findById(id)));
    }

    @Override
    public UserDTO getById(Long id) {
        return userMapper.toDTO(findById(id));
    }

    @Override
    public PageDto<UserDTO> getAll(Pageable pageable, UserCriteria userCriteria) {
        return Page2Dto.toDTO(findAll(pageable, userCriteria).map(userMapper::toDTO));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(AptekaException::userNotFoundById);
    }

    @Override
    public Page<User> findAll(Pageable pageable, UserCriteria userCriteria) {
        return userRepository.findAll(UserSpecifications.createSpecification(userCriteria), pageable);
    }

    @Override
    public void deleteById(Long id) {
        delete(findById(id));
    }

    @Override
    public void delete(User user) {
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            log.error(e);
            throw AptekaException.deleteUserException();
        }
    }

    @Override
    public User userByTelegramId(String telegramId) {
        return userRepository.findByTelegramId(telegramId)
                        .orElse(new User(telegramId));
    }

    @Override
    public byte[] generateExcel(Pageable pageable, UserCriteria criteria) {
        List<User> content = findAll(pageable, criteria).getContent();

        ZoneId asiaTashkent = ZoneId.of("Asia/Tashkent");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        try {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            String[] columns = {"№",
                    "ID",
                    "Mijoz F.I.SH",
                    "Telefon raqam",
                    "A`zo bo`lgan vaqt",
                    "Buyurtmalari qiymati",
                    "Status",
            };

            XSSFWorkbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet("Mijozlar ro'yhati");

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);

            Row row = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(cellStyle);
            }

            int rowId = 1;
            int numberOfRow = 0;
            for (User user : content) {

                Row row1 = sheet.createRow(rowId++);

                Cell number = row1.createCell(0);
                number.setCellValue(numberOfRow);
                number.setCellStyle(cellStyle);

                Cell ID = row1.createCell(1);
                ID.setCellValue(user.getId());
                ID.setCellStyle(cellStyle);

                Cell fullName = row1.createCell(2);
                fullName.setCellValue(user.getLastName() + " " + user.getFirstName());
                fullName.setCellStyle(cellStyle);

                Cell phone = row1.createCell(3);
                phone.setCellValue(user.getPhoneNumber());
                phone.setCellStyle(cellStyle);

                Cell registeredDate = row1.createCell(4);
                registeredDate.setCellValue(LocalDateTime.ofInstant(user.getCreatedDate(), asiaTashkent).format(dateTimeFormatter));
                registeredDate.setCellStyle(cellStyle);

                Cell orderAmount = row1.createCell(5);
                orderAmount.setCellValue(user.getOrderAmount());
                orderAmount.setCellStyle(cellStyle);

                Cell status = row1.createCell(6);
                status.setCellValue(user.getClientType().getUzbName());
                status.setCellStyle(cellStyle);

                numberOfRow++;
            }

            for (int i = 0; i < 15; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(byteArrayOutputStream);
            byteArrayOutputStream.close();
            workbook.close();
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            log.error(e);
            throw AptekaException.excelError();
        }
    }

    @Override
    public User save(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            log.error(e);
            throw new AptekaException("USER SAVE ERROR");
        }
    }
}
