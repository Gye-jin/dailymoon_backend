package com.example.dailymoon.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.dailymoon.dto.DiaryDTO;
import com.example.dailymoon.dto.FileDTO;
import com.example.dailymoon.entity.Diary;
import com.example.dailymoon.entity.File;
import com.example.dailymoon.repository.DiaryRepository;
import com.example.dailymoon.repository.FileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
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
	@Override
	@Transactional
	public void uploadFileToS3AndLoadFiles(List<MultipartFile> fileList, DiaryDTO diaryDTO) throws AmazonServiceException, SdkClientException, IOException {
		Diary diary = DiaryDTO.diaryDTOToEntity(diaryDTO);
		
		for(MultipartFile file : fileList) {
			if(file.getSize()!=0) {
			// 파일 이름
			String fileName = diary.getDiaryNo()+"_"+file.getOriginalFilename();
			// 파일 오리지널 이름
			String originalFileName = file.getOriginalFilename();
			
			// 파일 크기
			long size = file.getSize();
//			// 저장할 파일 속성 지정
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
	
	// Read
	@Override
	public List<FileDTO> loadFile(Long diaryNo) {
		List<File> fileEntityList = fileRepo.findByDiaryNo(diaryNo);
		
		List<FileDTO> fileDTOList = new ArrayList<FileDTO>();
		
		for(File file:fileEntityList) {
			FileDTO fileDTO = File.fileEntityToDTO(file);
			fileDTOList.add(fileDTO);
		}
		return fileDTOList;
	}
	
	// Update
	@Override
	@Transactional
	public void updateFile(Long diaryNo, List<MultipartFile> fileList) throws IOException {
		Diary diary = diaryRepo.findById(diaryNo).orElseThrow(NoSuchElementException::new);
		try {
			List<File> fileEntityList = fileRepo.findByDiaryNo(diaryNo);
			for(File file:fileEntityList) {
				amazonS3Client.deleteObject(new DeleteObjectRequest(S3Bucket, file.getFileName()));
				fileRepo.deleteById(file.getFileNo());
			}
			for(MultipartFile file : fileList) {
				String fileName = diary.getDiaryNo()+"_"+file.getOriginalFilename();
				String originalFileName = file.getOriginalFilename();
				long size = file.getSize();
				ObjectMetadata objectMetaData = new ObjectMetadata();
				objectMetaData.setContentType(file.getContentType());
				objectMetaData.setContentLength(size);
				amazonS3Client.putObject(
						new PutObjectRequest(S3Bucket, fileName, file.getInputStream(), objectMetaData)
						.withCannedAcl(CannedAccessControlList.PublicRead)
						);
				String filePath = amazonS3Client.getUrl(S3Bucket, fileName).toString();
				FileDTO fileDTO = FileDTO.builder().fileName(fileName).filePath(filePath).originalFileName(originalFileName).build();
				File fileEntity = FileDTO.fileDTOToEntity(fileDTO);
				fileEntity.insertDiaryInFile(diary);
				fileRepo.save(fileEntity);
			}
		} catch(AmazonServiceException e) {
			e.printStackTrace();
		} catch(SdkClientException e) {
			e.printStackTrace();
		}
	}
	
	// Delete
	@Override
	@Transactional
	public void deleteFile(Long diaryNo) {
		List<File> fileList = fileRepo.findByDiaryNo(diaryNo);
		try {
			for(File file:fileList) {
				amazonS3Client.deleteObject(new DeleteObjectRequest(S3Bucket, file.getFileName()));
				fileRepo.deleteById(file.getFileNo());
			}
		} catch(AmazonServiceException e) {
			e.printStackTrace();
		} catch(SdkClientException e) {
			e.printStackTrace();
		}
	}
}
