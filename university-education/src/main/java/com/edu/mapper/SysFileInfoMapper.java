package com.edu.mapper;

import com.edu.entity.SysFileInfo;
import org.apache.ibatis.annotations.Param;

public interface SysFileInfoMapper {
    SysFileInfo selectById(@Param("id") Long id);
    int insert(SysFileInfo fileInfo);
    int deleteById(@Param("id") Long id);
}
