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
public class DiaryController {
	@Autowired
	DiaryServiceImpl diaryService;

	@Autowired
	FileServiceImpl fileService;

	// [Create]==============================================================================================================================
	// jwt토큰을 받아야함 - 임시로 userId로 진행
	@PostMapping("/diarys/create/{strDate}")
	@Transactional
	public boolean createDiarys(HttpServletRequest request, @PathVariable String strDate,
			@ModelAttribute DiaryDTO diaryDTO, @RequestParam(value = "fileList") List<MultipartFile> fileList)
			throws AmazonServiceException, SdkClientException, IOException {

		// strDate 날짜 타입으로 변경
		LocalDate date = LocalDate.parse(strDate, DateTimeFormatter.ISO_DATE);
		System.out.println("test");
		Long userId = (Long) request.getAttribute("id");

		/* 해당 날짜에 diary가 있는지 확인하기
		 * check: true => 해당 날짜 diary 작성되어 있지 않음
		 * check: false => 해당 날짜 diary 작성되어 있음
		 */
		boolean check = diaryService.checkDiary(userId, date);

		if (check & (fileList.get(0).getSize()!=0) ) {

			// diary 저장
			diaryService.createDiary(userId, diaryDTO, date);

			// uploadFileS3는 fileList와 diaryDTO를 파라미터로 전달 받기 때문에 loadDiaryDTO를 통해 diaryDTO
			// 받아오기
			DiaryDTO loadDiaryDTO = diaryService.loadDiaryDTO(userId, date);

			// 입력받은 file을 s3와 DB에 저장
			fileService.uploadFileToS3AndLoadFiles(fileList, loadDiaryDTO);
			return true;
		} else if(check & (fileList.get(0).getSize()==0)) {
			// diary 저장
			diaryService.createDiary(userId, diaryDTO, date);

			return true;
		} else {
			return false;
		}
	}
	// ======================================================================================================================================
	
	// [Read]================================================================================================================================
	// 해당 유저가 작성한 모든 데이터 출력
	@GetMapping("diarys/read/all")
	public List<PreviewDiary> loadMonthDiary(HttpServletRequest request) {
		return diaryService.loadAllDairyDTO((Long)request.getAttribute("id"));
	}
	
	// 해당 날짜에 작성된 다이어리 가져오기
	@GetMapping("diarys/read/{strDate}")
	public DetailDiary loadDiary(HttpServletRequest request, @PathVariable String strDate) {
		// strDate 날짜 타입으로 변경
		LocalDate date = LocalDate.parse(strDate, DateTimeFormatter.ISO_DATE);
		Long userId = (Long) request.getAttribute("id");
		
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
	@PutMapping("diarys/update/in-file")
	public DiaryDTO updateDiary(HttpServletRequest request, @ModelAttribute DiaryDTO diaryDTO, @ModelAttribute List<MultipartFile> fileList) throws IOException {
		Long userId = (Long) request.getAttribute("id");
		if (fileList.get(0).getSize()==0) {
			return diaryService.updateDiary(diaryDTO);
		} else {
			fileService.updateFile(diaryDTO.getDiaryNo(), fileList);
			return diaryService.updateDiary(diaryDTO);
		}
	}
	
	// ======================================================================================================================================
	
	// [Delete]==============================================================================================================================
	@DeleteMapping("diarys/delete/{userId}/{diaryNo}")
	public void deleteDiary(HttpServletRequest request, @PathVariable Long diaryNo) {
		Long userId = (Long) request.getAttribute("id");
		fileService.deleteFile(diaryNo);
		diaryService.deleteDiary(diaryNo);
	}
	// ======================================================================================================================================
}
