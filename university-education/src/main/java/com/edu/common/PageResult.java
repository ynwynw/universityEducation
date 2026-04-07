package com.edu.common;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 分页结果
 */
@Data
public class PageResult<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 总记录数 */
    private Long total;
    
    /** 数据列表 */
    private List<T> list;
    
    /** 当前页码 */
    private Integer pageNum;
    
    /** 每页大小 */
    private Integer pageSize;
    
    public PageResult() {
    }
    
    public PageResult(Long total, List<T> list) {
        this.total = total;
        this.list = list;
    }
    
    public PageResult(List<T> list, Long total) {
        this.list = list;
        this.total = total;
    }
    
    public PageResult(List<T> list, Long total, Integer pageNum, Integer pageSize) {
        this.list = list;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
