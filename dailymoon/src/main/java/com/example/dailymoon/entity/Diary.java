package com.example.dailymoon.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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

import com.example.dailymoon.dto.CalanderDTO;
import com.example.dailymoon.dto.DiaryDTO;
import com.example.dailymoon.emotion.Feeling;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString(exclude = "files")
public class Diary {
	// [Column]
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="diary_no", nullable = false)
	private Long diaryNo;
	
	@Enumerated(EnumType.STRING)
	private Feeling feeling;
	
	@Column(updatable = false)
	private LocalDate date;
	
	private String detail;
	
	// [Join]
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private Member member;
	
	@JsonIgnore
	@OneToMany(mappedBy = "diary",cascade = CascadeType.REMOVE)
	private List<File> files = new ArrayList<File>();
	

	
	// [DTO to Entity]
	public static Diary diaryDTOToEntity(CalanderDTO diaryDTO) {
		Diary diary = Diary.builder().diaryNo(diaryDTO.getDiaryNo())
				.feeling(diaryDTO.getFeeling()).date(diaryDTO.getDate())
				.detail(diaryDTO.getDetail()).build();
		return diary;
	}
	
	// Insert
	public void memberInDiary(Member member) {
		this.member = member;
	}
	public void dateInDiary(LocalDate date) {
		this.date = date;
	}
	public void filesInDiary(List<File> files) {
		this.files = files;
	}
	
	// Update
	public void updateDiary(DiaryDTO diaryDTO) {
		this.feeling = diaryDTO.getFeeling();
		this.detail = diaryDTO.getDetail();
	}
}
