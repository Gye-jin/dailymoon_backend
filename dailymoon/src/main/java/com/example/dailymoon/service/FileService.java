package com.example.dailymoon.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.example.dailymoon.dto.DiaryDTO;

public interface FileService {
	
	public void uploadFileToS3AndLoadFiles(List<MultipartFile> fileList, DiaryDTO diaryDTO) throws AmazonServiceException, SdkClientException, IOException;

//	public List<FileDTO> loadFile(Long diaryNo);
	
	public void updateFile(Long diaryNo, List<MultipartFile> fileList) throws IOException;
	
}
