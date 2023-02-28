package com.example.dailymoon.dto;

import com.example.dailymoon.entity.File;

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
public class FileDTO {
	private Long fileNo;
	private String fileName;
	private String filePath;
	private String originalFileName;
	
	// [Entity to DTO]
	public static FileDTO fileEntityToDTO(File file) {
		FileDTO fileDTO = FileDTO.builder().fileNo(file.getFileNo()).fileName(file.getFileName())
				.filePath(file.getFilePath()).originalFileName(file.getOriginalFileName()).build();
		return fileDTO;
	}
}
