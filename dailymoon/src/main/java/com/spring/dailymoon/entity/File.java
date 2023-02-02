package com.spring.dailymoon.entity;

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

import com.spring.dailymoon.dto.FileDTO;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class File {
	// [Column]
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "file_no", nullable = false)
	private int fileNo;

	@Column(name = "file_name", nullable = false)
	private String fileName;
	@Column(name = "file_path", nullable = false)
	private String filePath;
	@Column(name = "original_file_name", nullable = false)
	private String originalFileName;

	// [Join]
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "file")
	private Diary diary;

	// [Entity to DTO]
	public static FileDTO fileEntityToDTO(File file) {
		FileDTO fileDTO = FileDTO.builder().fileNo(file.fileNo).fileName(file.fileName)
				.filePath(file.filePath).originalFileName(file.originalFileName).build();
		return fileDTO;
	}
}