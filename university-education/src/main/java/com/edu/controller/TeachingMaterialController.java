package com.edu.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.edu.annotation.Log;
import com.edu.common.PageResult;
import com.edu.common.Result;
import com.edu.entity.EduTeachingMaterial;
import com.edu.entity.SysFileInfo;
import com.edu.service.TeachingMaterialService;
import com.edu.util.OfficeToPdfConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/teaching-material")
public class TeachingMaterialController {

    @Autowired
    private TeachingMaterialService materialService;

    @Value("${file.upload-path:D:/dev/university-education}")
    private String uploadPath;

    /**
     * 教师上传教学资料
     */
    @PostMapping
    @Log(module = "教学资料管理", operation = "上传教学资料")
    public Result<Void> upload(EduTeachingMaterial material,
                               @RequestParam("file") MultipartFile file) {
        materialService.upload(material, file);
        return Result.success();
    }

    /**
     * 教师查看自己的资料
     */
    @GetMapping("/my")
    public Result<List<EduTeachingMaterial>> getMyMaterials() {
        return Result.success(materialService.getMyMaterials());
    }

    /**
     * 管理员分页查询
     */
    @GetMapping("/page")
    @SaCheckPermission("system:teaching-material:list")
    public Result<PageResult<EduTeachingMaterial>> getPage(
            EduTeachingMaterial query,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(materialService.getPage(query, pageNum, pageSize));
    }

    /**
     * 管理员审核
     */
    @PutMapping("/audit/{id}")
    @SaCheckPermission("system:teaching-material:audit")
    @Log(module = "教学资料管理", operation = "审核教学资料")
    public Result<Void> audit(@PathVariable Long id,
                              @RequestParam Integer auditStatus,
                              @RequestParam(required = false) String auditRemark) {
        materialService.audit(id, auditStatus, auditRemark);
        return Result.success();
    }

    /**
     * 教师重新上传被拒绝的资料
     */
    @PutMapping("/reupload/{id}")
    @Log(module = "教学资料管理", operation = "重新上传教学资料")
    public Result<Void> reupload(@PathVariable Long id,
                                  @RequestParam("file") MultipartFile file) {
        materialService.reupload(id, file);
        return Result.success();
    }

    /**
     * 下载教学资料
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        EduTeachingMaterial material = materialService.getById(id);
        SysFileInfo fileInfo = materialService.getFileInfo(material.getFileId());
        String fullPath = uploadPath + "/" + fileInfo.getFilePath();
        File file = new File(fullPath);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileInfo.getFileName() + "\"")
                .body(resource);
    }

    /**
     * 预览教学资料（Office 文件转 PDF）
     */
    @GetMapping("/preview/{id}")
    public ResponseEntity<Resource> preview(@PathVariable Long id) {
        EduTeachingMaterial material = materialService.getById(id);
        SysFileInfo fileInfo = materialService.getFileInfo(material.getFileId());
        String fullPath = uploadPath + "/" + fileInfo.getFilePath();
        File file = new File(fullPath);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        
        String ext = fileInfo.getFileExt().toLowerCase();
        
        // Office 文件转 PDF 预览
        if ("doc".equals(ext) || "docx".equals(ext) || "xls".equals(ext) || "xlsx".equals(ext)) {
            try {
                File pdfFile = OfficeToPdfConverter.convert(file);
                if (pdfFile != null && pdfFile.exists()) {
                    Resource resource = new FileSystemResource(pdfFile);
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_PDF)
                            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"preview.pdf\"")
                            .body(resource);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // PDF 和图片直接预览
        Resource resource = new FileSystemResource(file);
        String mimeType;
        switch (ext) {
            case "pdf": mimeType = "application/pdf"; break;
            case "jpg": case "jpeg": mimeType = "image/jpeg"; break;
            case "png": mimeType = "image/png"; break;
            case "gif": mimeType = "image/gif"; break;
            default: mimeType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileInfo.getFileName() + "\"")
                .body(resource);
    }
}
