package com.example.SpringBoot.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.SpringBoot.file.dto.FileObjectDTO;
import com.example.SpringBoot.file.repository.FileObjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3 amazonS3;
    private final FileObjectRepository fileObjectRepository;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = dirName + "/" + UUID.randomUUID() + extension;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        PutObjectRequest putObjectRequest = new PutObjectRequest(
                bucketName,
                fileName,
                multipartFile.getInputStream(),
                metadata
        ).withCannedAcl(CannedAccessControlList.PublicRead); // 퍼블릭 읽기 권한 추가

        amazonS3.putObject(putObjectRequest);
        System.out.println("테스트");

        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    // 단일 파일 업로드
    public void uploadAndRegisterFile(String referenceType, Long referenceId, String fileType, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            this.uploadAndRegisterFiles(referenceType, referenceId, fileType, new MultipartFile[]{file});
        }
    }

    // 다중 파일 업로드
    public void uploadAndRegisterFiles(String referenceType, Long referenceId, String fileType, MultipartFile[] files) throws IOException {
        System.out.println(files.length);

        for (MultipartFile file : files) {
            System.out.println("for");
            if (!file.isEmpty()) {
                System.out.println("파일 테스트");
                // S3 업로드 (prefix로 fileType 사용해 구분)
                String url = this.upload(file, fileType);

                FileObjectDTO obj = new FileObjectDTO();
                obj.setReferenceType(referenceType);
                obj.setReferenceId(referenceId);
                obj.setFileType(fileType);
                obj.setUrl(url);
                obj.setOriginalName(file.getOriginalFilename());

                fileObjectRepository.insert(obj);
            }
        }
    }


    public String getFileUrl(String fileName) {
        return amazonS3.getUrl(bucketName, fileName).toString(); // 브라우저 접근용 URL
    }

    public void deleteFile(String key) {
        amazonS3.deleteObject(bucketName, key);
    }
}
