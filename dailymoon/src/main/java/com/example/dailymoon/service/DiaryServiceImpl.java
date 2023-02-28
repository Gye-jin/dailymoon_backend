package com.example.dailymoon.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.example.dailymoon.dto.DiaryDTO;
import com.example.dailymoon.dto.FileDTO;
import com.example.dailymoon.entity.Diary;
import com.example.dailymoon.entity.Member;
import com.example.dailymoon.form.PreviewDiary;
import com.example.dailymoon.repository.DiaryRepository;
import com.example.dailymoon.repository.FileRepository;
import com.example.dailymoon.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {
	@Autowired
	DiaryRepository diaryRepo;
	@Autowired
	MemberRepository memberRepo;
	@Autowired
	FileServiceImpl fileService;
	@Autowired
	FileRepository fileRepo;
	
	// 버킷 이름
	@Value("${cloud.aws.s3.bucket}")
	private String S3Bucket;
	
	@Autowired
	AmazonS3Client amazonS3Client;
	
	// [Create]==============================================================================================================================
	@Override
	@Transactional
	public void createDiary(Long userId, DiaryDTO diaryDTO, LocalDate date) {
		// member객체, date 삽입
		Member member = memberRepo.findByUserId(userId);

		if (!diaryRepo.ExistByMemberAndDate(member, date)) {
			// diary 객체 생성
			Diary diary = DiaryDTO.diaryDTOToEntity(diaryDTO);

			diary.memberInDiary(member);
			diary.dateInDiary(date);

			// DB삽입
			diaryRepo.save(diary);
		}
		
		
	}
	

	// 해당 날짜의 다이어리 가져오기
	@Override
	@Transactional
	public DiaryDTO loadDiaryDTO(Long userId, LocalDate date) {
		Diary diary = diaryRepo.findByUserIdAndDate(userId, date);
		DiaryDTO diaryDTO = Diary.diaryEntityToDTO(diary);
		return diaryDTO;
	}
	
	// 해당 달의 다이어리 가져오기
	@Override
	@Transactional
	public List<PreviewDiary> loadAllDairyDTO(Long userId) {
		List<Diary> allDiaryEntity = diaryRepo.findByUserId(userId);

		List<PreviewDiary> allDiary = new ArrayList<PreviewDiary>();
		for(Diary diary:allDiaryEntity) {
			PreviewDiary previewDiary = new PreviewDiary();
			previewDiary.setDate(diary.getDate().toString());
			previewDiary.setFeeling(diary.getFeeling());
			allDiary.add(previewDiary);
		}
		return allDiary;
	}
	
	// [Update]===============================================================================================================================
	@Override
	@Transactional
	public DiaryDTO updateDiary(DiaryDTO diaryDTO) {
		Diary diary = diaryRepo.findById(diaryDTO.getDiaryNo()).orElseThrow(NoSuchElementException::new);
		diary.updateDiary(diaryDTO);
		return Diary.diaryEntityToDTO(diary);
	}
	
	// [Delete]===============================================================================================================================
	@Override
	@Transactional
	public void deleteDiary(DiaryDTO diaryDTO) {
		
		
		try {
			for(FileDTO file:diaryDTO.getFiles()) {
				amazonS3Client.deleteObject(new DeleteObjectRequest(S3Bucket, file.getFileName()));
				
			}
		} catch(AmazonServiceException e) {
			e.printStackTrace();
		} catch(SdkClientException e) {
			e.printStackTrace();
		}
		diaryRepo.deleteById(diaryDTO.getDiaryNo());
	}
}
