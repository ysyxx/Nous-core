// 文件路径: src/main/java/com/dao/LearningProgressDao.java

package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.LearningProgressEntity;

/**
 * 学习进度表数据访问接口
 * @author
 * @date 2025-06-25
 */
public interface LearningProgressDao extends BaseMapper<LearningProgressEntity> {
    // 继承 BaseMapper 后，自动拥有了对 LearningProgressEntity 的所有基础CRUD方法。
    // 暂时不需要添加自定义方法。
}