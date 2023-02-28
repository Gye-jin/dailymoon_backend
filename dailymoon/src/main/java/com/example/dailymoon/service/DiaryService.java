package com.example.dailymoon.service;

import java.time.LocalDate;
import java.util.List;

import com.example.dailymoon.dto.CalanderDTO;
import com.example.dailymoon.dto.DiaryDTO;
import com.example.dailymoon.entity.Diary;

public interface DiaryService {

	public Diary createDiary(Long userId, CalanderDTO diaryDTO, LocalDate date);
	public DiaryDTO loadDiaryDTO(Long userId, LocalDate date);
	public List<CalanderDTO> loadCalenderDTO(Long userId);
	public DiaryDTO updateDiary(Long userId,DiaryDTO diaryDTO);
	public void deleteDiary(Long userId, DiaryDTO diaryDTO);
}
