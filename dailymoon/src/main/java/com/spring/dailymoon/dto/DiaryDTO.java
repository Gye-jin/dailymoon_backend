package com.spring.dailymoon.dto;

import java.time.LocalDate;

import com.spring.dailymoon.emotion.Feeling;
import com.spring.dailymoon.entity.Diary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DiaryDTO {
	private int diaryNo;
	private Feeling feeling;
	private LocalDate date;
	private String detail;
	
	// [DTO to Entity]
	public static Diary diaryDTOToEntity(DiaryDTO diaryDTO) {
		Diary diary = Diary.builder().diaryNo(diaryDTO.diaryNo)
				.feeling(diaryDTO.feeling).date(diaryDTO.date)
				.detail(diaryDTO.detail).build();
		return diary;
	}
}
