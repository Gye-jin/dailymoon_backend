package com.example.dailymoon.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.example.dailymoon.dto.CalanderDTO;
import com.example.dailymoon.dto.DiaryDTO;
import com.example.dailymoon.entity.Diary;
import com.example.dailymoon.service.DiaryServiceImpl;
import com.example.dailymoon.service.FileServiceImpl;

@RestController
@RequestMapping(value = "/api", produces = "application/json")
public class DiaryController {
	@Autowired
	DiaryServiceImpl diaryService;

	@Autowired
	FileServiceImpl fileService;

	// [Create]==============================================================================================================================
	// jwt토큰을 받아야함 - 임시로 userId로 진행
	@PostMapping("/diarys/create/{strDate}")
	public boolean createDiarys(HttpServletRequest request, @PathVariable String strDate,
			@ModelAttribute CalanderDTO diaryDTO, @RequestParam(value = "fileList", required = false) List<MultipartFile> fileList)
			throws AmazonServiceException, SdkClientException, IOException {

		// strDate 날짜 타입으로 변경
		LocalDate date = LocalDate.parse(strDate, DateTimeFormatter.ISO_DATE);
		Long userId = (Long) request.getAttribute("id");
		// 입력받은 file을 s3와 DB에 저장
		Diary diary = diaryService.createDiary(userId, diaryDTO, date);
		if(fileList != null) {
			fileService.uploadFileToS3AndLoadFiles(fileList, diary);
		}
		System.out.println("success");
		return true;
		 
	}
	// ======================================================================================================================================
	
	// [Read]================================================================================================================================
	// 해당 유저가 작성한 모든 데이터 출력
	@GetMapping("diarys/read/all")
	public List<CalanderDTO> loadMonthDiary(HttpServletRequest request) {
		System.out.println("readall");
		return diaryService.loadCalenderDTO((Long)request.getAttribute("id"));
	}
	
	// 해당 날짜에 작성된 다이어리 가져오기
	@GetMapping("diarys/read/{strDate}")
	public DiaryDTO loadDiary(HttpServletRequest request, @PathVariable String strDate) {
		// strDate 날짜 타입으로 변경
		LocalDate date = LocalDate.parse(strDate, DateTimeFormatter.ISO_DATE);
		Long userId = (Long) request.getAttribute("id");
		
		return diaryService.loadDiaryDTO(userId, date);
	}
	// ======================================================================================================================================
	
	// [Update]==============================================================================================================================
	@PutMapping("diarys/update")
	@Transactional
	public DiaryDTO updateDiary(HttpServletRequest request, @ModelAttribute DiaryDTO diaryDTO, @ModelAttribute List<MultipartFile> fileList) throws IOException {
		Long userId = (Long) request.getAttribute("id");
		if (fileList != null) {
			fileService.updateFile(diaryDTO.getDiaryNo(), fileList);
		} 
		return diaryService.updateDiary(userId,diaryDTO);
		
	}
	
	// ======================================================================================================================================
	
	// [Delete]==============================================================================================================================
	@DeleteMapping("diarys/delete")
	public void deleteDiary(HttpServletRequest request, @RequestBody DiaryDTO diaryDTO) {
		Long userId = (Long) request.getAttribute("id");
		System.out.println(diaryDTO.getDiaryNo());
		diaryService.deleteDiary(userId, diaryDTO);
	}
	// ======================================================================================================================================
}
