package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.LessonEntity;

/**
 * 课时表数据访问接口
 * * @author
 * @email
 * @date 2025-06-25
 */
public interface LessonDao extends BaseMapper<LessonEntity> {

    // 这个接口继承了MyBatis-Plus的BaseMapper<LessonEntity>
    // 它自动拥有了所有对`lessons`表的基础增删改查（CRUD）能力。
    // 例如：insert, selectById, selectBatchIds, updateById, deleteById, selectList等。

    // 只有当需要编写复杂的、多表连接的自定义SQL查询时，
    // 才需要在这里添加新的方法，并在对应的XML文件中实现。
    // 目前，我们暂时不需要添加任何自定义方法。

}