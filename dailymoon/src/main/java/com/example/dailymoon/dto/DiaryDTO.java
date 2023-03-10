package com.example.dailymoon.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	
	private List<FileDTO> files = new ArrayList<>();
	
	// [Entity To DTO]
	public static DiaryDTO diaryEntityToDTO(Diary diary) {
		DiaryDTO diaryDTO = DiaryDTO.builder().diaryNo(diary.getDiaryNo())
				.feeling(diary.getFeeling()).date(diary.getDate())
				.files(diary.getFiles().stream()
						.map(file -> FileDTO.fileEntityToDTO(file))
						.collect(Collectors.toList()))
				.detail(diary.getDetail()).build();
		return diaryDTO;
	}
}
