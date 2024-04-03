package com.khu.cloudcomputing.khuropbox.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Files {
    @Id
    @GeneratedValue
    private Integer id;
    private String fileName;
    @NonNull
    private String fileLink;
    private Long fileSize;
    private String fileType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Files update(String fileName, LocalDateTime updatedAt){
        this.fileName=fileName;
        this.updatedAt=updatedAt;
        return this;
    }
}
