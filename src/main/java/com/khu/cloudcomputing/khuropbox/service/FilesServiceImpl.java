package com.khu.cloudcomputing.khuropbox.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.khu.cloudcomputing.khuropbox.dto.FilesDTO;
import com.khu.cloudcomputing.khuropbox.dto.FilesUpdateDTO;
import com.khu.cloudcomputing.khuropbox.entity.Files;
import com.khu.cloudcomputing.khuropbox.repository.FilesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FilesServiceImpl implements FilesService {
    @Autowired
    private final FilesRepository filesRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public FilesDTO findById(Integer id) {//id를 이용하여 찾는 메서드
        return new FilesDTO(filesRepository.findById(id).orElse(new Files()));
    }

    @Override
    public List<FilesDTO> findAll() {//모든 파일을 찾는 메서드, 추후 user객체가 연계되면 바꿀 예정
        List<Files> list = filesRepository.findAll();
        List<FilesDTO> listDTO = new ArrayList<>();
        for (Files files : list) {
            listDTO.add(new FilesDTO(files));
        }
        return listDTO;
    }

    @Override
    public void updateFile(FilesUpdateDTO fileUpdate) {//파일이름 갱신 메서드
        Files file = this.filesRepository.findById(fileUpdate.getId()).orElseThrow();
        file.update(fileUpdate.getFileName(), fileUpdate.getFileLink(), LocalDateTime.now());
        this.filesRepository.save(file);
    }

    @Override
    public void deleteFile(Integer id) {//파일 삭제 메서드
        filesRepository.deleteById(id);
    }

    @Override
    public Integer insertFile(FilesDTO file) {//파일 업로드 메서드
        file.setCreatedAt(LocalDateTime.now());
        return filesRepository.save(file.toEntity()).getId();
    }
    @Override
    public String upload(MultipartFile multipartFile, String dirName) throws IOException { // dirName의 디렉토리가 S3 Bucket 내부에 생성됨

        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        return upload(uploadFile, dirName);
    }
    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);  // convert()함수로 인해서 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)

        return uploadImageUrl;      // 업로드된 파일의 S3 URL 주소 반환
    }
    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        }else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)    // PublicRead 권한으로 업로드 됨
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename()); // 업로드한 파일의 이름
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }
}
