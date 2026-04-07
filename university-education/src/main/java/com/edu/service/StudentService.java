package com.edu.service;

import cn.hutool.core.util.RandomUtil;
import com.edu.common.PageResult;
import com.edu.entity.EduClass;
import com.edu.entity.EduStudent;
import com.edu.entity.SysUser;
import com.edu.exception.BusinessException;
import com.edu.mapper.EduClassMapper;
import com.edu.mapper.EduStudentMapper;
import com.edu.mapper.SysUserMapper;
import com.edu.mapper.SysUserRoleMapper;
import com.edu.util.Md5Util;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@Service
public class StudentService {
    @Autowired
    private EduStudentMapper studentMapper;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    @Autowired
    private EduClassMapper classMapper;

    public PageResult<EduStudent> getPage(EduStudent query, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<EduStudent> list = studentMapper.selectList(query);
        PageInfo<EduStudent> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getTotal(), list);
    }

    public EduStudent getById(Long id) { return studentMapper.selectById(id); }
    public EduStudent getByStudentNo(String studentNo) { return studentMapper.selectByStudentNo(studentNo); }

    @Transactional
    public void add(EduStudent student) {
        if (studentMapper.checkStudentNoExists(student.getStudentNo(), null) > 0) {
            throw new BusinessException("学号已存在");
        }
        if (student.getStatus() == null) student.setStatus(1);
        studentMapper.insert(student);
        // 创建用户账号
        SysUser user = new SysUser();
        user.setUsername(student.getStudentNo());
        String salt = RandomUtil.randomString(6);
        user.setSalt(salt);
        user.setPassword(Md5Util.encrypt("123456", salt));
        user.setRealName(student.getName());
        user.setUserType("STUDENT");
        user.setRefId(student.getId());
        user.setStatus(1);
        userMapper.insert(user);
        userRoleMapper.batchInsert(user.getId(), Arrays.asList(3L)); // 学生角色
        // 更新班级人数
        classMapper.updateStudentCount(student.getClassId(), studentMapper.countByClassId(student.getClassId()));
    }

    @Transactional
    public void update(EduStudent student) {
        EduStudent old = studentMapper.selectById(student.getId());
        if (old == null) throw new BusinessException("学生不存在");
        student.setStudentNo(null); // 学号不可修改
        student.setCollegeId(null); student.setMajorId(null); student.setClassId(null); // 学院专业班级不可修改
        studentMapper.update(student);
    }

    @Transactional
    public void delete(Long id) {
        EduStudent student = studentMapper.selectById(id);
        if (student == null) return;
        studentMapper.deleteById(id);
        classMapper.updateStudentCount(student.getClassId(), studentMapper.countByClassId(student.getClassId()));
    }

    @Transactional
    public void batchDelete(List<Long> ids) { for (Long id : ids) delete(id); }

    public void downloadImportTemplate(HttpServletResponse response) {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("学生导入模板");
            String[] headers = {"学号", "姓名", "性别", "班级名称", "手机号", "邮箱"};
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = wb.createCellStyle();
            Font font = wb.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 20 * 256);
            }
            // 查询所有班级名称，写入隐藏 sheet 作为下拉数据源
            List<EduClass> allClasses = classMapper.selectList(new EduClass());
            Sheet hiddenSheet = wb.createSheet("classData");
            for (int i = 0; i < allClasses.size(); i++) {
                Row row = hiddenSheet.createRow(i);
                row.createCell(0).setCellValue(allClasses.get(i).getClassName());
            }
            // 隐藏数据源 sheet
            wb.setSheetHidden(wb.getSheetIndex(hiddenSheet), true);
            // 创建名称引用
            org.apache.poi.ss.util.CellRangeAddressList classRange = new org.apache.poi.ss.util.CellRangeAddressList(1, 1000, 3, 3);
            org.apache.poi.xssf.usermodel.XSSFDataValidationHelper dvHelper = new org.apache.poi.xssf.usermodel.XSSFDataValidationHelper((org.apache.poi.xssf.usermodel.XSSFSheet) sheet);
            String formula = "classData!$A$1:$A$" + Math.max(allClasses.size(), 1);
            org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint classConstraint = (org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint(formula);
            org.apache.poi.ss.usermodel.DataValidation classValidation = dvHelper.createValidation(classConstraint, classRange);
            classValidation.setShowErrorBox(true);
            classValidation.setErrorStyle(DataValidation.ErrorStyle.STOP);
            classValidation.createErrorBox("输入错误", "请从下拉列表中选择班级");
            sheet.addValidationData(classValidation);
            // 性别列下拉：男/女
            org.apache.poi.ss.util.CellRangeAddressList genderRange = new org.apache.poi.ss.util.CellRangeAddressList(1, 1000, 2, 2);
            org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint genderConstraint = (org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint) dvHelper.createExplicitListConstraint(new String[]{"男", "女"});
            org.apache.poi.ss.usermodel.DataValidation genderValidation = dvHelper.createValidation(genderConstraint, genderRange);
            genderValidation.setShowErrorBox(true);
            sheet.addValidationData(genderValidation);
            // 示例数据
            Row example = sheet.createRow(1);
            example.createCell(0).setCellValue("2024001001");
            example.createCell(1).setCellValue("张三");
            example.createCell(2).setCellValue("男");
            example.createCell(3).setCellValue(allClasses.isEmpty() ? "" : allClasses.get(0).getClassName());
            example.createCell(4).setCellValue("13800000000");
            example.createCell(5).setCellValue("zhangsan@example.com");
            String fileName = URLEncoder.encode("学生导入模板.xlsx", "UTF-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            wb.write(response.getOutputStream());
        } catch (IOException e) {
            throw new BusinessException("下载模板失败: " + e.getMessage());
        }
    }

    @Transactional
    public Map<String, Object> importStudents(MultipartFile file) {
        int successCount = 0;
        List<String> errors = new ArrayList<>();
        // 缓存班级名称 -> 班级对象的映射，避免重复查询
        Map<String, EduClass> classCache = new HashMap<>();
        try (Workbook wb = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = wb.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                String studentNo = getCellString(row, 0);
                String name = getCellString(row, 1);
                String genderStr = getCellString(row, 2);
                String className = getCellString(row, 3);
                String phone = getCellString(row, 4);
                String email = getCellString(row, 5);
                if (studentNo.isEmpty() || name.isEmpty()) {
                    errors.add("第" + (i + 1) + "行: 学号和姓名不能为空");
                    continue;
                }
                if (className.isEmpty()) {
                    errors.add("第" + (i + 1) + "行: 班级名称不能为空");
                    continue;
                }
                if (studentMapper.checkStudentNoExists(studentNo, null) > 0) {
                    errors.add("第" + (i + 1) + "行: 学号 " + studentNo + " 已存在");
                    continue;
                }
                // 查找班级
                EduClass eduClass = classCache.get(className);
                if (eduClass == null) {
                    eduClass = classMapper.selectByClassName(className);
                    if (eduClass == null) {
                        errors.add("第" + (i + 1) + "行: 班级 \"" + className + "\" 不存在");
                        continue;
                    }
                    // selectByClassName 不带 join，需要再查一次带关联的
                    eduClass = classMapper.selectById(eduClass.getId());
                    classCache.put(className, eduClass);
                }
                EduStudent student = new EduStudent();
                student.setStudentNo(studentNo);
                student.setName(name);
                student.setGender("女".equals(genderStr) ? 0 : 1);
                student.setPhone(phone.isEmpty() ? null : phone);
                student.setEmail(email.isEmpty() ? null : email);
                student.setCollegeId(eduClass.getCollegeId());
                student.setMajorId(eduClass.getMajorId());
                student.setClassId(eduClass.getId());
                student.setEnrollmentYear(eduClass.getEnrollmentYear());
                student.setStatus(1);
                studentMapper.insert(student);
                // 创建用户账号
                SysUser user = new SysUser();
                user.setUsername(studentNo);
                String salt = RandomUtil.randomString(6);
                user.setSalt(salt);
                user.setPassword(Md5Util.encrypt("123456", salt));
                user.setRealName(name);
                user.setUserType("STUDENT");
                user.setRefId(student.getId());
                user.setStatus(1);
                userMapper.insert(user);
                userRoleMapper.batchInsert(user.getId(), Arrays.asList(3L));
                successCount++;
            }
        } catch (IOException e) {
            throw new BusinessException("读取文件失败: " + e.getMessage());
        }
        // 更新涉及到的班级人数
        for (EduClass c : classCache.values()) {
            classMapper.updateStudentCount(c.getId(), studentMapper.countByClassId(c.getId()));
        }
        Map<String, Object> result = new HashMap<>();
        result.put("successCount", successCount);
        result.put("errorCount", errors.size());
        result.put("errors", errors);
        return result;
    }

    private String getCellString(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return "";
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }
}
