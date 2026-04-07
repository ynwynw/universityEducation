package com.edu.mapper;

import com.edu.entity.SysOperationLog;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface SysOperationLogMapper {
    List<SysOperationLog> selectList(SysOperationLog query);
    int insert(SysOperationLog log);
    int deleteByIds(@Param("ids") List<Long> ids);
}
