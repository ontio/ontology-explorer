package com.github.ontio.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AmazonS3Client {

    public AmazonS3 amazonS3;

    public TransferManager transferManager;

    public static String AWS_ACCESS_KEY_ID;

    public static String AWS_SECRET_ACCESS_KEY;

    public static String AWS_S3_BUCKET_NAME;

    public static String getAwsAccessKeyId() {
        return AWS_ACCESS_KEY_ID;
    }

    @Value("${aws_access_key_id}")
    public void setAwsAccessKeyId(String awsAccessKeyId) {
        AWS_ACCESS_KEY_ID = awsAccessKeyId;
    }

    public static String getAwsSecretAccessKey() {
        return AWS_SECRET_ACCESS_KEY;
    }

    @Value("${aws_secret_access_key}")
    public void setAwsSecretAccessKey(String awsSecretAccessKey) {
        AWS_SECRET_ACCESS_KEY = awsSecretAccessKey;
    }

    public static String getAwsS3BucketName() {
        return AWS_S3_BUCKET_NAME;
    }

    @Value("${aws_s3_bucket_name}")
    public void setAwsS3BucketName(String awsS3BucketName) {
        AWS_S3_BUCKET_NAME = awsS3BucketName;
    }


    @PostConstruct
    private void initializeAmazon(){
        AWSCredentials awsCredentials = new BasicAWSCredentials(AmazonS3Client.AWS_ACCESS_KEY_ID, AmazonS3Client.AWS_SECRET_ACCESS_KEY);
        this.amazonS3= AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.AP_SOUTHEAST_1)
                .build();
        this.transferManager = TransferManagerBuilder.standard()
                .withS3Client(this.amazonS3)
                .withMultipartUploadThreshold((long) (5 * 1024 * 1025))
                .build();
    }

}
