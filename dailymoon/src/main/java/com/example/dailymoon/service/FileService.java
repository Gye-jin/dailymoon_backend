package com.example.dailymoon.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	
	public ResponseEntity<Object> fileUpload(MultipartFile[] fileList);

}
