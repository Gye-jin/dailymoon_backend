package com.example.dailymoon.service;

import java.time.LocalDate;
import java.util.List;

import com.example.dailymoon.dto.DiaryDTO;
import com.example.dailymoon.form.PreviewDiary;

public interface DiaryService {

	public void createDiary(Long userId, DiaryDTO diaryDTO, LocalDate date);
	public boolean checkDiary(Long userId, LocalDate date);
	public DiaryDTO loadDiaryDTO(Long userId, LocalDate date);
	public List<PreviewDiary> loadAllDairyDTO(Long userId);
	public DiaryDTO updateDiary(DiaryDTO diaryDTO);
	public void deleteDiary(Long diaryNo);
}
