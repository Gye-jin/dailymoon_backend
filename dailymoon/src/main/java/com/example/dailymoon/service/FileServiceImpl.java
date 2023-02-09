package com.example.dailymoon.service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.dailymoon.dto.DiaryDTO;
import com.example.dailymoon.dto.FileDTO;
import com.example.dailymoon.entity.Diary;
import com.example.dailymoon.entity.File;
import com.example.dailymoon.repository.DiaryRepository;
import com.example.dailymoon.repository.FileRepository;

@Service
public class FileServiceImpl {
	// 버킷 이름
	@Value("${cloud.aws.s3.bucket}")
	private String S3Bucket;
	
	@Autowired
	AmazonS3Client amazonS3Client;
	@Autowired
	FileRepository fileRepo;
	@Autowired
	DiaryRepository diaryRepo;
	
	// Create
	public void uploadFileS3(List<MultipartFile> fileList, DiaryDTO diaryDTO) throws AmazonServiceException, SdkClientException, IOException {
		Diary diary = DiaryDTO.diaryDTOToEntity(diaryDTO);
		
		for(MultipartFile file : fileList) {
			// 파일 이름
			String fileName = file.getOriginalFilename()+UUID.randomUUID();
			// 파일 오리지널 이름
			String originalFileName = file.getOriginalFilename();
			
			// 파일 크기
			long size = file.getSize();
			// 저장할 파일 속성 지정
			ObjectMetadata objectMetaData = new ObjectMetadata();
			objectMetaData.setContentType(file.getContentType());
			objectMetaData.setContentLength(size);
			
			// S3에 업로드
			amazonS3Client.putObject(
					new PutObjectRequest(S3Bucket, fileName, file.getInputStream(), objectMetaData)
					.withCannedAcl(CannedAccessControlList.PublicRead)
					);
			// 접근 가능한 URL가져오기
			String filePath = amazonS3Client.getUrl(S3Bucket, fileName).toString();
			
			// fileDTO 생성
			FileDTO fileDTO = FileDTO.builder().fileName(fileName).filePath(filePath).originalFileName(originalFileName).build();
			// Entity변경
			File fileEntity = FileDTO.fileDTOToEntity(fileDTO);
			// fileEintity에 diary삽입
			fileEntity.insertDiaryInFile(diary);
			// fileRepo save
			fileRepo.save(fileEntity);
		}
	}
}
