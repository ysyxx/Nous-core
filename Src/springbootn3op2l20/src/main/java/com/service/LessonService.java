// 文件路径: src/main/java/com/service/LessonService.java

package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.entity.LessonEntity;

/**
 * 课时服务接口
 *
 * @author
 * @email
 * @date 2025-06-25
 */
public interface LessonService extends IService<LessonEntity> {
    // 继承了 IService<LessonEntity> 之后，
    // MyBatis-Plus会自动为我们提供所有基础的增删改查方法，
    // 例如 getById, save, update, remove, page, list 等。

    // 如果未来有针对“课时”的特殊业务逻辑，可以在这里添加自定义方法。
    // 例如：public boolean publishLesson(Long lessonId);
    // 目前，我们暂时不需要任何自定义方法。
}