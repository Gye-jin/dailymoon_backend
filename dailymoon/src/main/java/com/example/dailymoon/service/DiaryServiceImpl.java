package com.example.dailymoon.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.example.dailymoon.common.ErrorCode;
import com.example.dailymoon.common.exception.ApiControllerException;
import com.example.dailymoon.dto.CalanderDTO;
import com.example.dailymoon.dto.DiaryDTO;
import com.example.dailymoon.dto.FileDTO;
import com.example.dailymoon.entity.Diary;
import com.example.dailymoon.entity.Member;
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
	FileRepository fileRepo;
	
	// 버킷 이름
	@Value("${cloud.aws.s3.bucket}")
	private String S3Bucket;
	
	@Autowired
	AmazonS3Client amazonS3Client;
	
	// [Create]==============================================================================================================================
	@Override
	@Transactional
	public Diary createDiary(Long userId, CalanderDTO diaryDTO, LocalDate date) {
		// member객체, date 삽입
		Member member = memberRepo.findByUserId(userId);
		
		Diary diaryEntity = diaryRepo.findByMemberAndDate(member, date).orElseGet(Diary::new);
		if (diaryEntity.getDiaryNo() != null) {
			throw new ApiControllerException(ErrorCode.CONFILICT);
		} else {
			diaryEntity = Diary.builder().date(date).member(member).detail(diaryDTO.getDetail())
					.feeling(diaryDTO.getFeeling()).build();
			// DB삽입
			diaryRepo.save(diaryEntity);
	
		}
		return diaryEntity;
	}
	

	// 해당 날짜의 다이어리 가져오기
	@Override
	@Transactional
	public DiaryDTO loadDiaryDTO(Long userId, LocalDate date) {
		
		Diary diary = diaryRepo.findByUserIdAndDate(userId, date).orElseThrow(() -> new ApiControllerException(ErrorCode.POSTS_NOT_FOUND));

		return DiaryDTO.diaryEntityToDTO(diary);
	}
	
	// 해당 달의 다이어리 가져오기
	@Override
	@Transactional
	public List<CalanderDTO> loadCalenderDTO(Long userId) {
		List<Diary> allDiaryEntity = diaryRepo.findByUserId(userId);

		List<CalanderDTO> calanderDTO = new ArrayList<CalanderDTO>();
		for(Diary diary:allDiaryEntity) {
			calanderDTO.add(CalanderDTO.diaryEntityToDTO(diary));
		}
		return calanderDTO;
	}
	
	// [Update]===============================================================================================================================
	@Override
	@Transactional
	public DiaryDTO updateDiary(Long userId,DiaryDTO diaryDTO) {
		
		Diary diary = diaryRepo.findById(diaryDTO.getDiaryNo()).orElseThrow(() -> new ApiControllerException(ErrorCode.POSTS_NOT_FOUND));
		if(!diary.getMember().getUserId().equals(userId)) {
			throw new ApiControllerException(ErrorCode.FORBIDDEN);
		}
		diary.updateDiary(diaryDTO);
		return DiaryDTO.diaryEntityToDTO(diary);
	}
	
	// [Delete]===============================================================================================================================
	@Override
	@Transactional
	public void deleteDiary(Long userId, DiaryDTO diaryDTO) throws AmazonServiceException, SdkClientException{
		Diary diary = diaryRepo.findById(diaryDTO.getDiaryNo())
				.orElseThrow(() -> new ApiControllerException(ErrorCode.POSTS_NOT_FOUND));
		if (!diary.getMember().getUserId().equals(userId)) {
			throw new ApiControllerException(ErrorCode.FORBIDDEN);
		}

		for (FileDTO file : diaryDTO.getFiles()) {
			amazonS3Client.deleteObject(new DeleteObjectRequest(S3Bucket, file.getFileName()));

		}
		diaryRepo.deleteById(diaryDTO.getDiaryNo());
	}
}
