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
public class DiaryDTO {
	private Long diaryNo;
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
