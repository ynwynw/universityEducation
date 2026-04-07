package com.edu.common;

import lombok.Data;

/**
 * 分页查询参数
 */
@Data
public class PageQuery {
    
    /** 当前页码 */
    private Integer pageNum = 1;
    
    /** 每页大小 */
    private Integer pageSize = 10;
    
    /** 排序字段 */
    private String orderByColumn;
    
    /** 排序方向 desc/asc */
    private String isAsc;
}
