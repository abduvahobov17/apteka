package com.asgardiateam.aptekaproject.service;

import com.asgardiateam.aptekaproject.entity.Bucket;
import com.asgardiateam.aptekaproject.entity.BucketProduct;
import com.asgardiateam.aptekaproject.entity.User;
import com.asgardiateam.aptekaproject.entity.dynamicquery.criteria.BucketCriteria;
import com.asgardiateam.aptekaproject.entity.dynamicquery.specifications.BucketSpecifications;
import com.asgardiateam.aptekaproject.enums.BucketStatus;
import com.asgardiateam.aptekaproject.exception.AptekaException;
import com.asgardiateam.aptekaproject.mapper.BucketMapper;
import com.asgardiateam.aptekaproject.payload.BucketDTO;
import com.asgardiateam.aptekaproject.repository.BucketRepository;
import com.asgardiateam.aptekaproject.service.interfaces.BucketService;
import com.asgardiateam.aptekaproject.utils.Page2Dto;
import com.asgardiateam.aptekaproject.utils.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class BucketServiceImpl implements BucketService {

    private final BucketRepository bucketRepository;
    private final BucketMapper bucketMapper;

    @Override
    public PageDto<BucketDTO> getBuckets(BucketCriteria criteria, Pageable pageable) {
        return Page2Dto.toDTO(findAll(criteria, pageable).map(bucketMapper::toDTO));
    }

    @Override
    public Optional<Bucket> getBucketByUserId(Long userId) {
        return bucketRepository.findByUser_IdAndBucketStatus(userId, BucketStatus.PROGRESS);
    }

    @Override
    public Bucket save(Bucket bucket) {
        return bucketRepository.save(bucket);
    }

    @Override
    public byte[] generateExcel(BucketCriteria criteria, Pageable pageable) {
        List<Bucket> content = findAll(criteria, pageable).getContent();

        try {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            String[] columns = {"№",
                    "Dorilar ro'yhati",
                    "ID",
                    "Miqdor",
                    "Narx",
                    "Buyurtmachi telefon raqami",
                    "Buyurtmachi",
                    "To'lov turi",
                    "Status",
                    "Qiymati",
                    "Jami buyurtma qiymati"
            };

            XSSFWorkbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet("Mijozlar ro'yhati");

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);

            Row headers = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headers.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(cellStyle);
            }

            int rowId = 1;
            int numberOfRow = 1;
            for (Bucket bucket : content) {

                int startRowToMerge = rowId;
                for (BucketProduct bucketProduct : bucket.getBucketProducts()) {

                    Row values = sheet.createRow(rowId++);

                    Cell number = values.createCell(0);  // -> should be merged
                    number.setCellValue(numberOfRow);
                    number.setCellStyle(cellStyle);

                    Cell product = values.createCell(1);
                    product.setCellValue(bucketProduct.getProduct().getName());
                    product.setCellStyle(cellStyle);

                    Cell ID = values.createCell(2);  // -> should be merged
                    ID.setCellValue(bucket.getId());
                    ID.setCellStyle(cellStyle);

                    Cell productAmount = values.createCell(3);
                    productAmount.setCellValue(bucketProduct.getAmount());
                    productAmount.setCellStyle(cellStyle);

                    Cell price = values.createCell(4);
                    price.setCellValue(bucketProduct.getProduct().getPrice());
                    price.setCellStyle(cellStyle);

                    Cell clientPhone = values.createCell(5);  // -> should be merged
                    clientPhone.setCellValue(bucket.getUser().getPhoneNumber());
                    clientPhone.setCellStyle(cellStyle);

                    Cell clientName = values.createCell(6);  // -> should be merged
                    clientName.setCellValue(bucket.getUser().getFirstName() + " " + bucket.getUser().getLastName());
                    clientName.setCellStyle(cellStyle);

                    Cell paymentType = values.createCell(7);  // -> should be merged
                    paymentType.setCellValue(bucket.getPaymentType().getUzbName());
                    paymentType.setCellStyle(cellStyle);

                    Cell status = values.createCell(8);  // -> should be merged
                    status.setCellValue(bucket.getBucketStatus().name());
                    status.setCellStyle(cellStyle);

                    Cell costOfProduct = values.createCell(9);
                    costOfProduct.setCellValue(bucketProduct.getAmount() * bucketProduct.getProduct().getPrice());
                    costOfProduct.setCellStyle(cellStyle);

                    Cell costOfOrder = values.createCell(10);  // -> should be merged
                    costOfOrder.setCellValue(bucket.getOverallAmount());
                    costOfOrder.setCellStyle(cellStyle);

                }

                if (bucket.getBucketProducts().size() > 1) {
                    sheet.addMergedRegion(new CellRangeAddress(startRowToMerge, rowId - 1, 0, 0));
                    sheet.addMergedRegion(new CellRangeAddress(startRowToMerge, rowId - 1, 2, 2));
                    sheet.addMergedRegion(new CellRangeAddress(startRowToMerge, rowId - 1, 5, 5));
                    sheet.addMergedRegion(new CellRangeAddress(startRowToMerge, rowId - 1, 6, 6));
                    sheet.addMergedRegion(new CellRangeAddress(startRowToMerge, rowId - 1, 7, 7));
                    sheet.addMergedRegion(new CellRangeAddress(startRowToMerge, rowId - 1, 8, 8));
                    sheet.addMergedRegion(new CellRangeAddress(startRowToMerge, rowId - 1, 10, 10));
                }
                startRowToMerge = rowId;
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

    private Bucket findById(Long id) {
        return bucketRepository.findById(id)
                .orElseThrow(AptekaException::bucketNotFound);
    }

    private Page<Bucket> findAll(BucketCriteria criteria, Pageable pageable) {
        return bucketRepository.findAll(BucketSpecifications.createSpecifications(criteria), pageable);
    }
}
