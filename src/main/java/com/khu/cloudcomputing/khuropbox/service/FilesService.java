package com.khu.cloudcomputing.khuropbox.service;

import com.khu.cloudcomputing.khuropbox.dto.FilesDTO;

import java.util.List;

public interface FilesService{
    FilesDTO findById(Integer id);
    List<FilesDTO> findAll();
    void updateFileName(Integer id, String fileName);
    void deleteFile(Integer id);
    Integer insertFile(FilesDTO file);
}
