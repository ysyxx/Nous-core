// CourseService.java
package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.entity.CourseEntity;

public interface CourseService extends IService<CourseEntity> {
    CourseEntity getCourseWithDetails(Long courseId);
}