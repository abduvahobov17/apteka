package com.asgardiateam.aptekaproject.service;

import com.asgardiateam.aptekaproject.service.interfaces.MinioFileUploader;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNullElse;
import static org.apache.commons.lang3.StringUtils.isNoneEmpty;

@Log4j2
@Service
@RequiredArgsConstructor
public class MinioFileUploaderImpl implements MinioFileUploader {

    @Value("${minio.url}")
    private String END_POINT;
    @Value("${minio.login}")
    private String LOGIN;
    @Value("${minio.password}")
    private String PASSWORD;

    private MinioClient minioClient;

    @PostConstruct
    public void init() {
        minioClient = MinioClient
                .builder()
                .endpoint(END_POINT)
                .credentials(LOGIN, PASSWORD)
                .build();
    }

    @Override
    public String upload(String bucketName, MultipartFile file, String objectName) {

        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
            } else {
                System.out.printf("Bucket '%s' already exist. ", bucketName);
            }
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());

            return objectName;
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException minioException) {
            log.error(minioException);
        }
        return "";
    }

    @Override
    public String getTempUrl(String objectName, String bucketName) {
        try {

            if (nonNull(objectName) && isNoneEmpty(objectName))
                return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(requireNonNullElse(objectName, ""))
                        .expiry(68, TimeUnit.HOURS)
                        .build());

            return "";
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException minioException) {
            log.error(minioException);
        }
        return "";
    }
}