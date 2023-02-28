package com.example.dailymoon.dto;

import java.time.LocalDate;

import com.example.dailymoon.emotion.Feeling;
import com.example.dailymoon.entity.Diary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CalanderDTO {
	private Long diaryNo;
	private Feeling feeling;
	private LocalDate date;
	private String detail;
	
	// [Entity To DTO]
	public static CalanderDTO diaryEntityToDTO(Diary diary) {
		CalanderDTO diaryDTO = CalanderDTO.builder().diaryNo(diary.getDiaryNo())
				.feeling(diary.getFeeling()).date(diary.getDate())
				.detail(diary.getDetail()).build();
		return diaryDTO;
	}
}
