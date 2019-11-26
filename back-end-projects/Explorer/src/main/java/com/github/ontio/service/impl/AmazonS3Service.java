package com.github.ontio.service.impl;

import com.amazonaws.services.s3.transfer.Upload;
import com.github.ontio.config.AmazonS3Client;
import com.github.ontio.config.ParamsConfig;
import com.github.ontio.exception.ExplorerException;
import com.github.ontio.util.ErrorInfo;
import com.github.ontio.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service("AmazonS3Service")
public class AmazonS3Service {

    private final AmazonS3Client amazonS3Client;
    private final ParamsConfig paramsConfig;

    @Autowired
    public AmazonS3Service(AmazonS3Client amazonS3Client,ParamsConfig paramsConfig) {
        this.amazonS3Client = amazonS3Client;
        this.paramsConfig = paramsConfig;
    }


    /**
     * upload file to S3
     *
     * @param pictureBase64Str
     */
    public void uploadFile2S3(String pictureBase64Str, String fileName) {

        String filepath = paramsConfig.LOGO_TEMP_FILEPATH + File.separator + fileName;
        File file = null;
        try {
            //delete:data:image/jpeg;base64,
            file = Helper.generateImage(pictureBase64Str.substring(pictureBase64Str.indexOf("base64,") + 7, pictureBase64Str.length()), filepath);
            uploadFile(file, fileName);
            log.info("S3:succeed to upload {}", fileName);
        } catch (Exception e) {
            log.error("error...", e);
            throw new ExplorerException(ErrorInfo.FILE_ERROR.code(), ErrorInfo.FILE_ERROR.desc(), false);
        } finally {
            if (Helper.isNotEmptyAndNull(file)) {
                file.delete();
            }
        }
    }


    private void uploadFile(File file, String fileName) throws Exception {
        Upload xfer = amazonS3Client.transferManager.upload(AmazonS3Client.AWS_S3_BUCKET_NAME, fileName, file);
        xfer.waitForCompletion();
    }



}
