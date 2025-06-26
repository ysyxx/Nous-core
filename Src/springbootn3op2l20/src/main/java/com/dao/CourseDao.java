package com.dao;

// 导入MyBatis-Plus的基础Mapper
import com.baomidou.mybatisplus.mapper.BaseMapper;
// 导入我们新的课程实体类
import com.entity.CourseEntity;
// 【关键】导入MyBatis的@Param注解
import org.apache.ibatis.annotations.Param;


/**
 * 课程信息的数据访问接口
 *
 * @author
 * @email
 * @date 2025-06-25 16:07:19
 */
public interface CourseDao extends BaseMapper<CourseEntity> {

    /**
     * 自定义查询方法：根据课程ID获取课程的完整信息，
     * 包括其下的所有章节和课时。
     * 这个方法对应我们在 CourseDao.xml 中定义的 "selectCourseWithDetails" 查询。
     * * @param id 课程的ID
     * @return 包含完整详情的课程实体
     */
    CourseEntity selectCourseWithDetails(@Param("id") Long id);

}