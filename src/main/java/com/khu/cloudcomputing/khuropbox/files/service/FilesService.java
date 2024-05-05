package com.khu.cloudcomputing.khuropbox.files.service;

import com.khu.cloudcomputing.khuropbox.files.dto.FilesDTO;
import com.khu.cloudcomputing.khuropbox.files.dto.FilesUpdateDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FilesService{
    FilesDTO findById(Integer id);
    List<FilesDTO> findAll();
    void updateFile(FilesUpdateDTO filesUpdateDTO);
    void deleteFile(Integer id);
    Integer insertFile(FilesDTO file);
    String upload(MultipartFile multipartFile, String dirName, Integer id) throws IOException;
    ResponseEntity<byte[]> download(String fileUrl) throws IOException;
}
