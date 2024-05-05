package com.khu.cloudcomputing.khuropbox.files.dto;

import com.khu.cloudcomputing.khuropbox.files.entity.Files;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class FilesDTO {
    private Integer id;
    private String fileName;
    private String fileLink;
    private Long fileSize;
    private String fileType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public FilesDTO(Files entity){
        this.id=entity.getId();
        this.fileName=entity.getFileName();
        this.fileLink=entity.getFileLink();
        this.fileSize=entity.getFileSize();
        this.fileType=entity.getFileType();
        this.createdAt=entity.getCreatedAt();
        this.updatedAt=entity.getUpdatedAt();
    }

    public Files toEntity(){
        return Files.builder()
                .fileName(fileName)
                .fileLink(fileLink)
                .fileSize(fileSize)
                .fileType(fileType)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
