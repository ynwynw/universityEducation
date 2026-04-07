package com.edu.controller;

import com.edu.annotation.Log;
import com.edu.common.Result;
import com.edu.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    @Log(module = "文件管理", operation = "上传文件")
    public Result<Map<String, String>> upload(
            @RequestParam("file") MultipartFile file, 
            @RequestParam(required = false) String type) {
        return Result.success(fileService.upload(file, type));
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable String fileId) {
        Resource resource = fileService.download(fileId);
        String filename = fileService.getFilename(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    /**
     * 获取文件预览信息（前端根据返回决定预览还是下载）
     */
    @GetMapping("/preview/{fileId}")
    public Result<Map<String, String>> getPreviewInfo(@PathVariable String fileId) {
        return Result.success(fileService.getPreviewInfo(fileId));
    }

    /**
     * 直接查看文件（PDF/图片/txt 在浏览器中打开）
     */
    @GetMapping("/view/{fileId}")
    public ResponseEntity<Resource> viewFile(@PathVariable String fileId) {
        Resource resource = fileService.download(fileId);
        String mimeType = fileService.getMimeType(fileId);
        String filename = fileService.getFilename(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "inline; filename=\"" + filename + "\"")
                .body(resource);
    }

    @GetMapping("/image/{fileId}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileId) {
        Resource resource = fileService.download(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @DeleteMapping("/{fileId}")
    @Log(module = "文件管理", operation = "删除文件")
    public Result<Void> delete(@PathVariable String fileId) {
        fileService.delete(fileId);
        return Result.success();
    }
}
