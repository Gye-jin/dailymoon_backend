package com.example.dailymoon.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.example.dailymoon.dto.DiaryDTO;
import com.example.dailymoon.service.DiaryServiceImpl;
import com.example.dailymoon.service.FileServiceImpl;

@RestController
@RequestMapping(value = "/api",produces = "application/json")
@CrossOrigin(origins = {"*"})
public class DiaryController {
	@Autowired
	DiaryServiceImpl diaryService;
	
	@Autowired
	FileServiceImpl fileService;
	
	// [Create]
	// jwt토큰을 받아야함 - 임시로 userId로 진행
	@PostMapping("/diarys/create/{userId}/{strDate}")
	public void createDiarys(@PathVariable Long userId,
							 @PathVariable String strDate,
							 @ModelAttribute DiaryDTO diaryDTO,
							 @RequestParam(value = "fileList") List<MultipartFile> fileList) throws AmazonServiceException, SdkClientException, IOException {

		// strDate 날짜 타입으로 변경
		LocalDate date = LocalDate.parse(strDate, DateTimeFormatter.ISO_DATE);
		
		// diary 저장
		diaryService.createDiary(userId, diaryDTO, date);
		
		/* file존재 여부 확인하기
		 * file이 있을 경우 => if
		 * file이 없을 경우 => else
		 */
		if(!fileList.isEmpty()) {
			System.out.println("사진 있음");
			// uploadFileS3는 fileList와 diaryDTO를 파라미터로 전달 받기 때문에 loadDiaryDTO를 통해 diaryDTO 받아오기
			DiaryDTO loadDiaryDTO = diaryService.loadDiaryDTO(userId, date);
			// 입력받은 file을 s3와 DB에 저장
			fileService.uploadFileS3(fileList, loadDiaryDTO);
		}
		else {
			System.out.println("사진 없음");
		}
		
	}
}
