package com.khu.cloudcomputing.khuropbox.files.controller;

import com.khu.cloudcomputing.khuropbox.files.dto.FilesDTO;
import com.khu.cloudcomputing.khuropbox.files.dto.FilesUpdateDTO;
import com.khu.cloudcomputing.khuropbox.files.service.FilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/file")
public class FilesController {
    @Autowired
    private final FilesService filesService;

    private Integer id=1;
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
        filesService.updateFile(fileUpdate);
    }
    @PostMapping("upload")
    public void Upload(@RequestPart(value="fileName") String fileName, @RequestPart(value = "file") MultipartFile multipartFile) {
        String fileLink = "";
        if (multipartFile != null) { // 파일 업로드한 경우에만
            try {// 파일 업로드
                FilesDTO file=new FilesDTO();
                fileLink = filesService.upload(multipartFile, "", id); // S3 버킷의 images 디렉토리 안에 저장됨
                id++;
                file.setFileName(fileName);
                file.setFileLink(fileLink);
                file.setFileSize(multipartFile.getSize());
                file.setFileType(multipartFile.getName().substring(multipartFile.getName().lastIndexOf(".") + 1));
                filesService.insertFile(file);
                System.out.println("fileLink = " + fileLink);
            } catch (IOException e) {
                System.out.println("error");
            }
        }
    }
    @GetMapping("download/{id}")
    public ResponseEntity<byte[]> Download(@PathVariable(value="id") Integer id) throws IOException {
        FilesDTO file=filesService.findById(id);
        String filePath = file.getFileLink().substring(56);
        filePath='/'+ filePath;
        System.out.println(filePath);
        return filesService.download(filePath);
    }
}
