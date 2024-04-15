package com.khu.cloudcomputing.khuropbox.service;

import com.khu.cloudcomputing.khuropbox.dto.FilesDTO;
import com.khu.cloudcomputing.khuropbox.dto.FilesUpdateDTO;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface FilesService{
    FilesDTO findById(Integer id);
    List<FilesDTO> findAll();
    void updateFile(FilesUpdateDTO filesUpdateDTO);
    void deleteFile(Integer id);
    Integer insertFile(FilesDTO file);
    String upload(MultipartFile multipartFile, String dirName) throws IOException;
}
