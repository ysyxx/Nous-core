// 文件路径: src/main/java/com/dao/LearningProgressDao.java

package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.LearningProgressEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学习进度表数据访问接口
 * @author
 * @date 2025-06-25
 */
@Mapper // 或者你项目使用的其他Mapper注解，如 @Repository
public interface LearningProgressDao extends BaseMapper<LearningProgressEntity> {

    LearningProgressEntity getProgressByUserIdAndLessonIdXml(@Param("userId") Long userId, @Param("lessonId") Long lessonId);

    List<LearningProgressEntity> selectProgressWithLessonInfoByUserId(@Param("userId") Long userId);

    List<LearningProgressEntity> selectProgressWithLessonInfoByCourseIdAndUserId(
            @Param("courseId") Long courseId,
            @Param("userId") Long userId
    );
}
