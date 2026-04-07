package com.edu.mapper;

import com.edu.entity.EduAnnouncement;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface EduAnnouncementMapper {
    
    EduAnnouncement selectById(@Param("id") Long id);
    
    List<EduAnnouncement> selectList(EduAnnouncement announcement);
    
    List<EduAnnouncement> selectByTargetType(@Param("targetType") String targetType, @Param("userId") Long userId);
    
    List<Map<String, Object>> selectLatest(@Param("limit") int limit);
    
    int insert(EduAnnouncement announcement);
    
    int update(EduAnnouncement announcement);
    
    int deleteById(@Param("id") Long id);
    
    int deleteByIds(@Param("ids") List<Long> ids);
    
    int countUnread(@Param("userId") Long userId, @Param("targetType") String targetType);
    
    int markAsRead(@Param("announcementId") Long announcementId, @Param("userId") Long userId);
}
