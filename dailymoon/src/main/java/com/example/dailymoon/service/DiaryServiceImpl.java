package com.example.dailymoon.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dailymoon.dto.DiaryDTO;
import com.example.dailymoon.entity.Diary;
import com.example.dailymoon.entity.Member;
import com.example.dailymoon.repository.DiaryRepository;
import com.example.dailymoon.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl {
	@Autowired
	DiaryRepository diaryRepo;
	@Autowired
	MemberRepository memberRepo;
	
	// Create
	public void createDiary(Long userId, DiaryDTO diaryDTO, LocalDate date) {
		// diary 객체 생성
		Diary diary = DiaryDTO.diaryDTOToEntity(diaryDTO);
		
		//  member객체, date 삽입
		Member member = memberRepo.findByUserId(userId);
		diary.memberInDiary(member);
		diary.dateInDiary(date);
		
		// DB삽입
		diaryRepo.save(diary);
	}
	
	// Read
	public DiaryDTO loadDiaryDTO(Long userId, LocalDate date) {
		Diary diary = diaryRepo.findByUserIdAndDate(userId, date);
		DiaryDTO diaryDTO = Diary.diaryEntityToDTO(diary);
		return diaryDTO;
	}
	
}
