package com.example.dailymoon.form;

import java.util.List;

import com.example.dailymoon.dto.FileDTO;
import com.example.dailymoon.emotion.Feeling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DetailDiary {
	private Long diaryNo;
	private String date;
	private Feeling feeling;
	private String detail;
	private List<FileDTO> fileList;
}
