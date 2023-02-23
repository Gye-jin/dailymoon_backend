package com.example.dailymoon.form;


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
public class PreviewDiary {
	private Feeling feeling;
	private String date;
}
