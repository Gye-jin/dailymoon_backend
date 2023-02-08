package com.example.dailymoon.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedDate;

import com.example.dailymoon.dto.DiaryDTO;
import com.example.dailymoon.emotion.Feeling;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Diary {
	// [Column]
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="diary_no", nullable = false)
	private Long diaryNo;
	
	@Enumerated(EnumType.STRING)
	private Feeling feeling;
	
	@CreatedDate
	@Column(updatable = false)
	private LocalDate date;
	
	private String detail;
	
	// [Join]
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diary")
	private Member member;
	
	@JsonIgnore
	@OneToMany(mappedBy = "diary")
	private List<File> files = new ArrayList<File>();
	
	// [Entity To DTO]
	public static DiaryDTO diaryEntityToDTO(Diary diary) {
		DiaryDTO diaryDTO = DiaryDTO.builder().diaryNo(diary.diaryNo)
				.feeling(diary.feeling).date(diary.date)
				.detail(diary.detail).build();
		return diaryDTO;
	}
}
