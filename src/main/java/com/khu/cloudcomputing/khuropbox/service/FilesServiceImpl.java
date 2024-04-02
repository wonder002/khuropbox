package com.khu.cloudcomputing.khuropbox.service;


import com.khu.cloudcomputing.khuropbox.dto.FilesDTO;
import com.khu.cloudcomputing.khuropbox.entity.Files;
import com.khu.cloudcomputing.khuropbox.repository.FilesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FilesServiceImpl implements FilesService{
    @Autowired
    private final FilesRepository filesRepository;

    public FilesDTO findById(Integer id){//id를 이용하여 찾는 메서드
        return new FilesDTO(filesRepository.findById(id).orElse(new Files()));
    }
    public List<FilesDTO> findAll(){//모든 파일을 찾는 메서드, 추후 user객체가 연계되면 바꿀 예정
        List<Files> list=filesRepository.findAll();
        List<FilesDTO> listDTO=new ArrayList<>();
        for (Files files : list) {
            listDTO.add(new FilesDTO(files));
        }
        return listDTO;
    }
    public void updateFileName(Integer id, String fileName){//파일이름 갱신 메서드
        Files file=this.filesRepository.findById(id).orElseThrow();
        file.update(fileName, LocalDateTime.now());
        this.filesRepository.save(file);
    }
    public void deleteFile(Integer id){//파일 삭제 메서드
        filesRepository.deleteById(id);
    }
    public Integer insertFile(FilesDTO file){//파일 업로드 메서드
        return filesRepository.save(file.toEntity()).getId();
    }
}
