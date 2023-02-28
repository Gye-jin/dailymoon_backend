package com.example.dailymoon.entity;

import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.example.dailymoon.dto.FileDTO;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class File {
	// [Column]
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "file_no", nullable = false)
	private Long fileNo;

	@Column(name = "file_name", nullable = false)
	private String fileName;
	@Column(name = "file_path", nullable = false)
	private String filePath;
	@Column(name = "original_file_name", nullable = false)
	private String originalFileName;

	// [Join]
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diary_no")
	private Diary diary;


	
	public static File fileDTOToEntity(FileDTO fileDTO) {
		File file = File.builder().fileNo(fileDTO.getFileNo()).fileName(fileDTO.getFileName())
				.filePath(fileDTO.getFilePath()).originalFileName(fileDTO.getOriginalFileName()).build();
		return file;
	}
	
	// Insert
	public void insertDiaryInFile(Diary diary) {
		this.diary = diary;
	}
}
