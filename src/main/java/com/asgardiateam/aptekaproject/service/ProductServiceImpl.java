package com.asgardiateam.aptekaproject.service;

import com.asgardiateam.aptekaproject.entity.Product;
import com.asgardiateam.aptekaproject.entity.dynamicquery.criteria.ProductCriteria;
import com.asgardiateam.aptekaproject.entity.dynamicquery.specifications.ProductSpecifications;
import com.asgardiateam.aptekaproject.enums.State;
import com.asgardiateam.aptekaproject.exception.AptekaException;
import com.asgardiateam.aptekaproject.mapper.ProductMapper;
import com.asgardiateam.aptekaproject.payload.ProductDTO;
import com.asgardiateam.aptekaproject.payload.request.ProductRequest;
import com.asgardiateam.aptekaproject.repository.ProductRepository;
import com.asgardiateam.aptekaproject.service.interfaces.ProductService;
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
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    @Override
    public Optional<Product> findByName(String name) {
        return productRepository.findFirstByNameAndState(name, State.ALIVE);
    }

    @Override
    public ProductDTO create(ProductRequest productRequest) {
        return productMapper.toDTO(save(productMapper.toCreate(productRequest)));
    }

    @Override
    public ProductDTO update(ProductRequest productRequest, Long id) {
        return productMapper.toDTO(save(productMapper.toUpdate(productRequest, findById(id))));
    }

    @Override
    public ProductDTO getById(Long id) {
        return productMapper.toDTO(findById(id));
    }

    @Override
    public PageDto<ProductDTO> getAll(Pageable pageable, ProductCriteria criteria) {
        return Page2Dto.toDTO(findAll(pageable, criteria).map(productMapper::toDTO));
    }

    @Override
    public void deleteById(Long id) {
        delete(findById(id));
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findByIdAndState(id, State.ALIVE).orElseThrow(AptekaException::productNotFound);
    }

    @Override
    public Page<Product> findAll(Pageable pageable, ProductCriteria productCriteria) {
        return productRepository.findAll(ProductSpecifications.createSpecifications(productCriteria), pageable);
    }

    @Override
    public byte[] generateExcelProduct(ProductCriteria criteria, Pageable pageable) {
        List<Product> content = findAll(pageable, criteria).getContent();

        try {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            String[] columns = {"№",
                    "ID",
                    "Dori nomi",
                    "Tasnifi",
                    "Yetkazib beruvchi",
                    "Yetkazib beruvchi telefon raqami",
                    "Miqdori",
                    "O'lchov birligi",
                    "Narxi",
                    "Status",
            };

            XSSFWorkbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet("Dorilar ro'yhati");

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
            for (Product product : content) {

                Row row1 = sheet.createRow(rowId++);

                Cell number = row1.createCell(0);
                number.setCellValue(numberOfRow);
                number.setCellStyle(cellStyle);

                Cell ID = row1.createCell(1);
                ID.setCellValue(product.getId());
                ID.setCellStyle(cellStyle);

                Cell productName = row1.createCell(2);
                productName.setCellValue(product.getName());
                productName.setCellStyle(cellStyle);

                Cell description = row1.createCell(3);
                description.setCellValue(product.getDescription());
                description.setCellStyle(cellStyle);

                Cell supplier = row1.createCell(4);
                supplier.setCellValue(product.getSupplier());
                supplier.setCellStyle(cellStyle);

                Cell supplierPhone = row1.createCell(5);
                supplierPhone.setCellValue(product.getPhoneNumber());
                supplierPhone.setCellStyle(cellStyle);

                Cell amount = row1.createCell(6);
                amount.setCellValue(product.getAmount());
                amount.setCellStyle(cellStyle);

                Cell unitType = row1.createCell(7);
                unitType.setCellValue(product.getUnitType().name());
                unitType.setCellStyle(cellStyle);

                Cell price = row1.createCell(8);
                price.setCellValue(product.getPrice());
                price.setCellStyle(cellStyle);

                Cell state = row1.createCell(9);
                state.setCellValue(product.getState().getUzbName());
                state.setCellStyle(cellStyle);

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
    public Product save(Product product) {
        try {
            return productRepository.save(product);
        } catch (Exception e) {
            throw AptekaException.productSaveError();
        }
    }

    @Override
    public void delete(Product product) {
        try {
            product.setState(State.DELETED);
            productRepository.save(product);
        } catch (Exception e) {
            throw AptekaException.productDeleteError();
        }
    }
}
