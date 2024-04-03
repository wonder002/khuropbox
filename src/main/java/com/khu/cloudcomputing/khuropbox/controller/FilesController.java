package com.khu.cloudcomputing.khuropbox.controller;

import com.khu.cloudcomputing.khuropbox.dto.FilesDTO;
import com.khu.cloudcomputing.khuropbox.dto.FilesUpdateDTO;
import com.khu.cloudcomputing.khuropbox.service.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FilesController {
    @Autowired
    private final FilesService filesService;

    public FilesController(FilesService filesService) {
        this.filesService = filesService;
    }

    @GetMapping("files")
    public List<FilesDTO> Files(){
        return filesService.findAll();
    }

    @GetMapping("files/{id}")
    public FilesDTO FilesId(@PathVariable(value="id") Integer id){
        return filesService.findById(id);
    }

    @PostMapping("delete/{id}")
    public void Delete(@PathVariable(value="id") Integer id){
        filesService.deleteFile(id);
    }

    @PostMapping("update")
    public void Update(@RequestBody FilesUpdateDTO fileUpdate){
        filesService.updateFileName(fileUpdate);
    }

    @PostMapping("insert")
    public Integer Insert(@RequestBody FilesDTO file){
        return filesService.insertFile(file);
    }

}
