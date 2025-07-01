// 文件路径: src/main/java/com/service/LearningProgressService.java

package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.entity.LearningProgressEntity;
import com.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 学习进度服务接口
 * @author
 * @date 2025-06-25
 */
public interface LearningProgressService extends IService<LearningProgressEntity> {

    /**
     * 更新或创建用户的学习进度
     * @param progressEntity 包含 userId, lessonId 和 progressSeconds 的进度实体
     * @return 操作是否成功
     */
    boolean updateOrSaveUserProgress(LearningProgressEntity progressEntity);


    PageUtils queryPage(Map<String, Object> params);
    /**
     * 获取指定用户的所有学习进度记录
     * @param userId 用户ID
     * @return 学习进度列表
     */
    List<LearningProgressEntity> getProgressByUserId(Long userId);

    LearningProgressEntity getProgressByUserIdAndLessonId(Long userId, Long lessonId);

    List<LearningProgressEntity> getProgressWithLessonInfoByUserId(Long userId);

    List<LearningProgressEntity> getProgressWithLessonInfoByCourseIdAndUserId(Long courseId, Long userId);

}
