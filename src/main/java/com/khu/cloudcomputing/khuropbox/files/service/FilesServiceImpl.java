package com.khu.cloudcomputing.khuropbox.files.service;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.khu.cloudcomputing.khuropbox.files.dto.FilesDTO;
import com.khu.cloudcomputing.khuropbox.files.dto.FilesInformationDTO;
import com.khu.cloudcomputing.khuropbox.files.dto.FilesUpdateDTO;
import com.khu.cloudcomputing.khuropbox.files.entity.Files;
import com.khu.cloudcomputing.khuropbox.files.repository.FilesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.servlet.function.RequestPredicates.contentType;

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
    public FilesInformationDTO findById(Integer id) {//id를 이용하여 찾는 메서드
        return new FilesInformationDTO(filesRepository.findById(id).orElse(new Files()));
    }
    @Override
    public String findLinkById(Integer id){
        return filesRepository.findById(id).orElse(new Files()).getFileLink();
    }
    @Override
    public List<FilesInformationDTO> findAll() {//모든 파일을 찾는 메서드, 추후 user객체가 연계되면 바꿀 예정
        List<Files> list = filesRepository.findAll();
        List<FilesInformationDTO> listDTO = new ArrayList<>();
        for (Files files : list) {
            listDTO.add(new FilesInformationDTO(files));
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
    public void updateLink(Integer id, String fileLink){
        Files file=this.filesRepository.findById(id).orElseThrow();
        file.updateLink(fileLink);
        this.filesRepository.save(file);
    }
    @Override
    public void deleteFile(Integer id) {//파일 삭제 메서드
        filesRepository.deleteById(id);
    }
    @Override
    public void deleteAtS3(String filePath){
        try {
            // S3에서 삭제
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, filePath));
            log.info(String.format("[%s] deletion complete", filePath));
        } catch (AmazonServiceException e) {
            log.error(e.getErrorMessage());
        }
    }
    @Override
    public Integer insertFile(FilesDTO file) {//파일 업로드 메서드
        file.setCreatedAt(LocalDateTime.now());
        return filesRepository.save(file.toEntity()).getId();
    }
    @Override
    public String upload(MultipartFile multipartFile, String dirName, Integer id, String fileType) throws IOException { // dirName의 디렉토리가 S3 Bucket 내부에 생성됨
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        return upload(uploadFile, dirName, id, fileType);
    }
    @Override
    public ResponseEntity<byte[]> download(String fileUrl) throws IOException { // 객체 다운  fileUrl : 폴더명/파일네임.파일확장자
        S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(bucket, fileUrl));
        S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
        byte[] bytes = IOUtils.toByteArray(objectInputStream);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(contentType(fileUrl));
        httpHeaders.setContentLength(bytes.length);
        String[] arr = fileUrl.split("/");
        String type = arr[arr.length - 1];
        String fileName = URLEncoder.encode(type, "UTF-8").replaceAll("\\+", "%20");
        httpHeaders.setContentDispositionFormData("attachment", fileName); // 파일이름 지정

        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }
    private String upload(File uploadFile, String dirName, Integer id, String fileType) {
        String fileName = dirName + "/" + id+"."+fileType;
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
    private MediaType contentType(String keyname) {
        String[] arr = keyname.split("\\.");
        String type = arr[arr.length - 1];
        return switch (type) {
            case "txt" -> MediaType.TEXT_PLAIN;
            case "png" -> MediaType.IMAGE_PNG;
            case "jpg" -> MediaType.IMAGE_JPEG;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }
}
