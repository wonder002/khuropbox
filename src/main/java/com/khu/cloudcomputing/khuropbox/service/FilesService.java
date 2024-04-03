package com.khu.cloudcomputing.khuropbox.service;

import com.khu.cloudcomputing.khuropbox.dto.FilesDTO;
import com.khu.cloudcomputing.khuropbox.dto.FilesUpdateDTO;

import java.util.List;

public interface FilesService{
    FilesDTO findById(Integer id);
    List<FilesDTO> findAll();
    void updateFileName(FilesUpdateDTO filesUpdateDTO);
    void deleteFile(Integer id);
    Integer insertFile(FilesDTO file);
}
