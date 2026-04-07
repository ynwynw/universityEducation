package com.edu.service;

import com.edu.common.PageResult;
import com.edu.entity.EduMajor;
import com.edu.exception.BusinessException;
import com.edu.mapper.EduClassMapper;
import com.edu.mapper.EduMajorMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class MajorService {
    @Autowired
    private EduMajorMapper majorMapper;
    @Autowired
    private EduClassMapper classMapper;

    public PageResult<EduMajor> getPage(EduMajor query, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<EduMajor> list = majorMapper.selectList(query);
        PageInfo<EduMajor> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getTotal(), list);
    }

    public EduMajor getById(Long id) {
        return majorMapper.selectById(id);
    }

    public List<EduMajor> getListByCollegeId(Long collegeId) {
        return majorMapper.selectByCollegeId(collegeId);
    }

    @Transactional
    public void add(EduMajor major) {
        EduMajor exist = majorMapper.selectByMajorCode(major.getMajorCode());
        if (exist != null) {
            throw new BusinessException("专业代码已存在");
        }
        major.setCreateTime(new Date());
        majorMapper.insert(major);
    }

    @Transactional
    public void update(EduMajor major) {
        EduMajor exist = majorMapper.selectByMajorCode(major.getMajorCode());
        if (exist != null && !exist.getId().equals(major.getId())) {
            throw new BusinessException("专业代码已存在");
        }
        major.setUpdateTime(new Date());
        majorMapper.update(major);
    }

    @Transactional
    public void delete(Long id) {
        int classCount = classMapper.countByMajorId(id);
        if (classCount > 0) {
            throw new BusinessException("该专业下存在班级，无法删除");
        }
        majorMapper.deleteById(id);
    }

    @Transactional
    public void batchDelete(List<Long> ids) {
        for (Long id : ids) {
            delete(id);
        }
    }
}
