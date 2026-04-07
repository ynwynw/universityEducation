package com.edu.util;

import com.edu.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.UUID;

/**
 * 文件工具类
 */
public class FileUtil {
    
    /** 允许的文档类型 */
    public static final String[] DOCUMENT_TYPES = {"doc", "docx", "pdf", "ppt", "pptx", "xls", "xlsx"};
    
    /** 允许的图片类型 */
    public static final String[] IMAGE_TYPES = {"jpg", "jpeg", "png", "gif", "bmp"};
    
    /** 最大文件大小 50MB */
    public static final long MAX_FILE_SIZE = 50 * 1024 * 1024;
    
    /**
     * 获取文件扩展名
     */
    public static String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }
    
    /**
     * 生成文件存储相对路径
     * @param category 分类: images, materials, exports
     * @param originalFilename 原始文件名
     * @return 相对路径
     */
    public static String generatePath(String category, String originalFilename) {
        String ext = getExtension(originalFilename);
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return category + "/" + datePath + "/" + uuid + "." + ext;
    }
    
    /**
     * 获取完整存储路径
     */
    public static String getFullPath(String basePath, String relativePath) {
        return basePath + "/" + relativePath;
    }

    /**
     * 保存文件
     */
    public static void saveFile(MultipartFile file, String fullPath) throws IOException {
        File dest = new File(fullPath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        file.transferTo(dest);
    }
    
    /**
     * 验证文件类型
     */
    public static boolean isAllowedType(String filename, String[] allowedTypes) {
        String ext = getExtension(filename).toLowerCase();
        return Arrays.asList(allowedTypes).contains(ext);
    }
    
    /**
     * 验证是否为文档类型
     */
    public static boolean isDocument(String filename) {
        return isAllowedType(filename, DOCUMENT_TYPES);
    }
    
    /**
     * 验证是否为图片类型
     */
    public static boolean isImage(String filename) {
        return isAllowedType(filename, IMAGE_TYPES);
    }
    
    /**
     * 验证文件大小
     */
    public static void validateFileSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw BusinessException.error("文件大小不能超过50MB");
        }
    }
    
    /**
     * 验证文档文件
     */
    public static void validateDocument(MultipartFile file) {
        validateFileSize(file);
        if (!isDocument(file.getOriginalFilename())) {
            throw BusinessException.error("只支持 Word, PDF, PPT, Excel 格式的文件");
        }
    }
    
    /**
     * 验证图片文件
     */
    public static void validateImage(MultipartFile file) {
        validateFileSize(file);
        if (!isImage(file.getOriginalFilename())) {
            throw BusinessException.error("只支持jpg, jpeg, png, gif, bmp 格式的图片");
        }
    }
    
    /**
     * 删除文件
     */
    public static boolean deleteFile(String fullPath) {
        File file = new File(fullPath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}
