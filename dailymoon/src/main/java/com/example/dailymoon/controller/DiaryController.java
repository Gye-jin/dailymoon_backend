package com.example.dailymoon.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.example.dailymoon.dto.DiaryDTO;
import com.example.dailymoon.dto.FileDTO;
import com.example.dailymoon.form.DetailDiary;
import com.example.dailymoon.form.PreviewDiary;
import com.example.dailymoon.service.DiaryServiceImpl;
import com.example.dailymoon.service.FileServiceImpl;

@RestController
@RequestMapping(value = "/api", produces = "application/json")
@CrossOrigin(origins = { "*" })
public class DiaryController {
	@Autowired
	DiaryServiceImpl diaryService;

	@Autowired
	FileServiceImpl fileService;

	// [Create]==============================================================================================================================
	// jwt토큰을 받아야함 - 임시로 userId로 진행
	// -------------------------------- 사진이 없는 경우 --------------------------------------
	@PostMapping("/diarys/create/{userId}/{strDate}")
	public boolean createDiarys(@PathVariable Long userId, @PathVariable String strDate,
			@ModelAttribute DiaryDTO diaryDTO) {
		// strDate 날짜 타입으로 변경
		LocalDate date = LocalDate.parse(strDate, DateTimeFormatter.ISO_DATE);

		/* 해당 날짜에 diary가 있는지 확인하기
		 * check: true => 해당 날짜 diary 작성되어 있지 않음
		 * check: false => 해당 날짜 diary 작성되어 있음
		 */
		boolean check = diaryService.checkDiary(userId, date);

		if (check) {
			// diary 저장
			diaryService.createDiary(userId, diaryDTO, date);
			return true;
		} else {
			return false;
		}
	}

	// ---------------------------------사진이 있는 경우-----------------------------------------------
	@PostMapping("/diarys/create/{userId}/{strDate}/in-file")
	@Transactional
	public boolean createDiarys(@PathVariable Long userId, @PathVariable String strDate,
			@ModelAttribute DiaryDTO diaryDTO, @RequestParam(value = "fileList") List<MultipartFile> fileList)
			throws AmazonServiceException, SdkClientException, IOException {

		// strDate 날짜 타입으로 변경
		LocalDate date = LocalDate.parse(strDate, DateTimeFormatter.ISO_DATE);

		/* 해당 날짜에 diary가 있는지 확인하기
		 * check: true => 해당 날짜 diary 작성되어 있지 않음
		 * check: false => 해당 날짜 diary 작성되어 있음
		 */
		boolean check = diaryService.checkDiary(userId, date);

		if (check) {

			// diary 저장
			diaryService.createDiary(userId, diaryDTO, date);

			// uploadFileS3는 fileList와 diaryDTO를 파라미터로 전달 받기 때문에 loadDiaryDTO를 통해 diaryDTO
			// 받아오기
			DiaryDTO loadDiaryDTO = diaryService.loadDiaryDTO(userId, date);

			// 입력받은 file을 s3와 DB에 저장
			fileService.uploadFileToS3AndLoadFiles(fileList, loadDiaryDTO);
			return true;
		} else {
			return false;
		}
	}
	// ======================================================================================================================================
	
	// [Read]================================================================================================================================
	// 해당 유저가 작성한 모든 데이터 출력
	@GetMapping("diarys/read/all/{userId}")
	public List<PreviewDiary> loadMonthDiary(@PathVariable Long userId) {
		return diaryService.loadAllDairyDTO(userId);
	}
	
	// 해당 날짜에 작성된 다이어리 가져오기
	@GetMapping("diarys/read/{userId}/{strDate}")
	public DetailDiary loadDiary(@PathVariable Long userId, @PathVariable String strDate) {
		// strDate 날짜 타입으로 변경
		LocalDate date = LocalDate.parse(strDate, DateTimeFormatter.ISO_DATE);
		
		DetailDiary detailDiary = new DetailDiary();
		DiaryDTO diaryDTO = diaryService.loadDiaryDTO(userId, date);
		// diary
		detailDiary.setDiaryNo(diaryDTO.getDiaryNo());
		detailDiary.setDate(diaryDTO.getDate().toString());
		detailDiary.setFeeling(diaryDTO.getFeeling());
		detailDiary.setDetail(diaryDTO.getDetail());
		// file
		List<FileDTO> fileList = fileService.loadFile(diaryDTO.getDiaryNo()); 
		detailDiary.setFileList(fileList);
		return detailDiary;
	}
	// ======================================================================================================================================
	
	// [Update]==============================================================================================================================
	@PutMapping("diarys/update/{userId}")
	public DiaryDTO updateDiary(@PathVariable Long userId, @ModelAttribute DiaryDTO diaryDTO) {
		return diaryService.updateDiary(diaryDTO);
	}
	
	@PutMapping("diarys/update/{userId}/in-file")
	public DiaryDTO updateDiary(@PathVariable Long userId, @ModelAttribute DiaryDTO diaryDTO, @ModelAttribute List<MultipartFile> fileList) throws IOException {
		fileService.updateFile(diaryDTO.getDiaryNo(), fileList);
		return diaryService.updateDiary(diaryDTO);
	}
	
	// ======================================================================================================================================
	
	// [Delete]==============================================================================================================================
	@DeleteMapping("diarys/delete/{userId}/{diaryNo}")
	public void deleteDiary(@PathVariable Long userId, @PathVariable Long diaryNo) {
		fileService.deleteFile(diaryNo);
		diaryService.deleteDiary(diaryNo);
	}
	// test
	@PostMapping("test/{test}")
	public String testDiary(@PathVariable String test) {
		return test;
	}
	// ======================================================================================================================================
}
