package com.example.SpringBoot.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class S3Controller {

    private final S3Uploader s3Uploader;

    @GetMapping("/s3test")
    public String s3testPage() {
        return "s3test"; // s3test.html 페이지
    }

    @PostMapping("/s3test/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        String fileName = s3Uploader.upload(file, "images");
        String fileUrl = s3Uploader.getFileUrl(fileName);
        model.addAttribute("imageUrl", fileUrl);
        return "s3test"; // 업로드 결과를 같은 페이지에 보여줌
    }
}
