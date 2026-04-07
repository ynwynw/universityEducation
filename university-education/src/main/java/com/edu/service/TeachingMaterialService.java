package com.edu.service;

import com.edu.common.PageResult;
import com.edu.entity.EduTeachingMaterial;
import com.edu.entity.SysFileInfo;
import com.edu.entity.SysUser;
import com.edu.exception.BusinessException;
import com.edu.mapper.EduTeachingMaterialMapper;
import com.edu.mapper.SysFileInfoMapper;
import com.edu.util.FileUtil;
import com.edu.util.SecurityUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class TeachingMaterialService {

    @Autowired
    private EduTeachingMaterialMapper materialMapper;

    @Autowired
    private SysFileInfoMapper fileInfoMapper;

    @Value("${file.upload-path:D:/dev/university-education}")
    private String uploadPath;

    /**
     * 教师上传教学资料
     */
    @Transactional
    public void upload(EduTeachingMaterial material, MultipartFile file) {
        // 验证文件
        FileUtil.validateDocument(file);

        // 保存文件到磁盘
        String relativePath = FileUtil.generatePath("materials", file.getOriginalFilename());
        String fullPath = FileUtil.getFullPath(uploadPath, relativePath);
        try {
            FileUtil.saveFile(file, fullPath);
        } catch (IOException e) {
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }

        // 保存文件信息到数据库
        SysUser user = SecurityUtil.getLoginUser();
        SysFileInfo fileInfo = new SysFileInfo();
        fileInfo.setFileName(file.getOriginalFilename());
        fileInfo.setFilePath(relativePath);
        fileInfo.setFileSize(file.getSize());
        fileInfo.setFileType("document");
        fileInfo.setFileExt(FileUtil.getExtension(file.getOriginalFilename()));
        fileInfo.setUploaderId(user.getRefId());
        fileInfo.setUploaderName(user.getRealName());
        fileInfoMapper.insert(fileInfo);

        // 保存教学资料记录
        material.setFileId(fileInfo.getId());
        material.setTeacherId(user.getRefId());
        material.setTeacherName(user.getRealName());
        material.setAuditStatus(0); // 待审核
        materialMapper.insert(material);
    }

    /**
     * 教师查看自己的资料列表
     */
    public List<EduTeachingMaterial> getMyMaterials() {
        Long teacherId = SecurityUtil.getLoginUser().getRefId();
        return materialMapper.selectByTeacherId(teacherId);
    }

    /**
     * 管理员分页查询
     */
    public PageResult<EduTeachingMaterial> getPage(EduTeachingMaterial query, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<EduTeachingMaterial> list = materialMapper.selectPage(query);
        PageInfo<EduTeachingMaterial> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getTotal(), list);
    }

    /**
     * 管理员审核
     */
    @Transactional
    public void audit(Long id, Integer auditStatus, String auditRemark) {
        EduTeachingMaterial material = materialMapper.selectById(id);
        if (material == null) {
            throw BusinessException.notFound("教学资料");
        }
        if (material.getAuditStatus() == 1) {
            throw new BusinessException("已审核通过的资料不能重复审核");
        }

        SysUser user = SecurityUtil.getLoginUser();
        EduTeachingMaterial update = new EduTeachingMaterial();
        update.setId(id);
        update.setAuditStatus(auditStatus);
        update.setAuditRemark(auditRemark);
        update.setAuditorId(user.getId());
        update.setAuditorName(user.getRealName());
        update.setAuditTime(new Date());
        materialMapper.update(update);
    }

    /**
     * 教师重新上传被拒绝的资料
     */
    @Transactional
    public void reupload(Long id, MultipartFile file) {
        EduTeachingMaterial material = materialMapper.selectById(id);
        if (material == null) {
            throw BusinessException.notFound("教学资料");
        }
        // 只有审核拒绝的才能重新上传
        if (material.getAuditStatus() != 2) {
            throw new BusinessException("只有审核拒绝的资料才能重新上传");
        }
        // 验证是当前教师的资料
        Long teacherId = SecurityUtil.getLoginUser().getRefId();
        if (!material.getTeacherId().equals(teacherId)) {
            throw new BusinessException("只能重新上传自己的资料");
        }
        // 验证文件
        FileUtil.validateDocument(file);
        // 保存新文件
        String relativePath = FileUtil.generatePath("materials", file.getOriginalFilename());
        String fullPath = FileUtil.getFullPath(uploadPath, relativePath);
        try {
            FileUtil.saveFile(file, fullPath);
        } catch (IOException e) {
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }
        // 更新文件信息
        SysUser user = SecurityUtil.getLoginUser();
        SysFileInfo fileInfo = new SysFileInfo();
        fileInfo.setFileName(file.getOriginalFilename());
        fileInfo.setFilePath(relativePath);
        fileInfo.setFileSize(file.getSize());
        fileInfo.setFileType("document");
        fileInfo.setFileExt(FileUtil.getExtension(file.getOriginalFilename()));
        fileInfo.setUploaderId(user.getRefId());
        fileInfo.setUploaderName(user.getRealName());
        fileInfoMapper.insert(fileInfo);
        // 更新资料记录，重置审核状态
        materialMapper.resetAudit(id, fileInfo.getId());
    }

    /**
     * 获取资料详情
     */
    public EduTeachingMaterial getById(Long id) {
        EduTeachingMaterial material = materialMapper.selectById(id);
        if (material == null) {
            throw BusinessException.notFound("教学资料");
        }
        return material;
    }

    /**
     * 获取文件信息
     */
    public SysFileInfo getFileInfo(Long fileId) {
        SysFileInfo fileInfo = fileInfoMapper.selectById(fileId);
        if (fileInfo == null) {
            throw BusinessException.notFound("文件");
        }
        return fileInfo;
    }
}
