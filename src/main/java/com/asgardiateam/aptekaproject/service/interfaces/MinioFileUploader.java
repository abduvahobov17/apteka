package com.asgardiateam.aptekaproject.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface MinioFileUploader {

    String upload(String bucketName, MultipartFile file, String objectName);

    String getTempUrl(String objectName, String bucketName);
}