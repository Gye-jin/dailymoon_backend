package com.spring.dailymoon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.dailymoon.dto.DiaryDTO;
import com.spring.dailymoon.dto.MemberDTO;
import com.spring.dailymoon.entity.Diary;
import com.spring.dailymoon.entity.Member;
import com.spring.dailymoon.repository.DiaryRepository;
import com.spring.dailymoon.repository.MemberRepository;

@Service
public class MemberService {
	@Autowired
	MemberRepository memberRepo;
	@Autowired
	DiaryRepository diaryRepo;
	
	// 테스트(회원가입)
	public void joinMember(long userId) {
		MemberDTO memberDTO = MemberDTO.builder()
				.userId(userId).nickname("우성").email("jws3167@naver.com").gender("M").birth("03월10일").build();
		Member member = MemberDTO.memberDTOToEntity(memberDTO);
		memberRepo.save(member);
		System.out.println("service: "+userId);
	}
	
	// 테스트(게시글 작성)
	public boolean postDiary(DiaryDTO diaryDTO) {
		System.out.println("service: "+diaryDTO.getDetail());
		System.out.println("service: "+diaryDTO.getFeeling());
		Diary diary = DiaryDTO.diaryDTOToEntity(diaryDTO);
		diaryRepo.save(diary);
		return false;
	}
}
