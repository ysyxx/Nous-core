// 文件路径: src/main/java/com/service/impl/LessonServiceImpl.java

package com.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.LessonDao;
import com.entity.LessonEntity;
import com.service.LessonService;
import org.springframework.stereotype.Service;

/**
 * 课时服务实现类
 *
 * @author
 * @email
 * @date 2025-06-25
 */
@Service("lessonService")
public class LessonServiceImpl extends ServiceImpl<LessonDao, LessonEntity> implements LessonService {

    // 你可能注意到了，这个实现类里面是空的。
    // 这正是MyBatis-Plus框架的强大之处！
    // 因为 LessonService 接口继承了 IService，而 LessonServiceImpl 继承了 ServiceImpl，
    // 所有常规的增删改查（CRUD）方法（如 save, updateById, getById, list, page 等）
    // 都已经由 ServiceImpl 自动实现了。你无需再手写一遍。

    // 如果你在 LessonService 接口中定义了任何自定义方法，
    // 你就需要在这里通过 @Override 来具体实现它。
}