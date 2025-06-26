// 文件路径: src/main/java/com/service/impl/LearningProgressServiceImpl.java

package com.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.LearningProgressDao;
import com.entity.LearningProgressEntity;
import com.entity.LessonEntity;
import com.service.LearningProgressService;
import com.service.LessonService; // 需要注入LessonService来获取课时总时长
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service("learningProgressService")
public class LearningProgressServiceImpl extends ServiceImpl<LearningProgressDao, LearningProgressEntity> implements LearningProgressService {

    @Autowired
    private LessonService lessonService; // 注入LessonService

    @Override
    public boolean updateOrSaveUserProgress(LearningProgressEntity progressEntity) {
        // 1. 根据 userId 和 lessonId 查找是否已存在进度记录
        EntityWrapper<LearningProgressEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", progressEntity.getUserId());
        wrapper.eq("lesson_id", progressEntity.getLessonId());
        LearningProgressEntity existingProgress = this.selectOne(wrapper);

        // 2. 获取课时的总时长
        LessonEntity lesson = lessonService.selectById(progressEntity.getLessonId());
        if (lesson == null || lesson.getDurationSeconds() == null || lesson.getDurationSeconds() <= 0) {
            // 如果课时不存在或没有时长，则无法计算进度，直接保存或更新秒数
            if (existingProgress != null) {
                existingProgress.setProgressSeconds(progressEntity.getProgressSeconds());
                return this.updateById(existingProgress);
            } else {
                return this.insert(progressEntity);
            }
        }
        Integer totalDuration = lesson.getDurationSeconds();

        // 3. 判断是否完成
        // 通常认为观看到95%就算完成
        boolean isCompleted = (progressEntity.getProgressSeconds() / totalDuration) >= 0.95;

        if (existingProgress != null) {
            // 如果记录已存在，则更新
            // 只在新的进度大于旧的进度时才更新，防止回退视频导致进度倒退
            if (progressEntity.getProgressSeconds() > existingProgress.getProgressSeconds()) {
                existingProgress.setProgressSeconds(progressEntity.getProgressSeconds());
            }
            // 如果之前未完成，现在完成了，则更新完成状态
            if (!existingProgress.getIsCompleted() && isCompleted) {
                existingProgress.setIsCompleted(true);
            }
            return this.updateById(existingProgress);
        } else {
            // 如果记录不存在，则创建新记录
            progressEntity.setIsCompleted(isCompleted);
            return this.insert(progressEntity);
        }
    }

    @Override
    public List<LearningProgressEntity> getProgressByUserId(Long userId) {
        EntityWrapper<LearningProgressEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("user_id", userId);
        return this.selectList(wrapper);
    }
}