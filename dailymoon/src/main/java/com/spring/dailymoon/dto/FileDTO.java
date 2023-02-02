package com.spring.dailymoon.dto;

import com.spring.dailymoon.entity.File;

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
	private int fileNo;
	private String fileName;
	private String filePath;
	private String originalFileName;
	
	public static File fileDTOToEntity(FileDTO fileDTO) {
		File file = File.builder().fileNo(fileDTO.fileNo).fileName(fileDTO.fileName)
				.filePath(fileDTO.filePath).originalFileName(fileDTO.originalFileName).build();
		return file;
	}
}
