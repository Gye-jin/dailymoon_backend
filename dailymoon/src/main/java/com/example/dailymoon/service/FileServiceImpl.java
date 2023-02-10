package com.example.dailymoon.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class FileServiceImpl {
	// 버킷 이름
	@Value("${cloud.aws.s3.bucket}")
	private String S3Bucket;
	
	@Autowired
	AmazonS3Client amazonS3Client;
	
	/* Create
	 * -uuid 미반영=> 반영하여 s3에 저장하기
	 */
	public ResponseEntity<Object> upload(MultipartFile[] fileList) throws AmazonServiceException, SdkClientException, IOException {
		List<String> imagePathList = new ArrayList<>();
		
		for(MultipartFile file : fileList) {
			// 파일 이름
			String originalName = file.getOriginalFilename();
			// 파일 크기
			long size = file.getSize();
			
			ObjectMetadata objectMetaData = new ObjectMetadata();
			objectMetaData.setContentType(file.getContentType());
			objectMetaData.setContentLength(size);
			
			// S3에 업로드
			amazonS3Client.putObject(
					new PutObjectRequest(S3Bucket, originalName, file.getInputStream(), objectMetaData)
					.withCannedAcl(CannedAccessControlList.PublicRead)
					);
			// 접근 가능한 URL가져오기
			String imagePath = amazonS3Client.getUrl(S3Bucket, originalName).toString();
			imagePathList.add(imagePath);
		}
		return new ResponseEntity<Object>(imagePathList, HttpStatus.OK);
	}
}
