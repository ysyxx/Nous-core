package com.dao;

// 导入MyBatis-Plus的基础Mapper
import com.baomidou.mybatisplus.mapper.BaseMapper;
// 导入我们新的课程实体类
import com.entity.CourseEntity;
// 【关键】导入MyBatis的@Param注解
import org.apache.ibatis.annotations.Param;

// 导入MyBatis-Plus的Wrapper和Pagination类
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

// 导入新的VO和View类
import com.entity.vo.CourseVO;
import com.entity.view.CourseView;

import java.util.List;
import java.util.Map;


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

    // 从 KechengxinxiDao 合并过来的方法
    List<CourseVO> selectListVO(@Param("ew") Wrapper<CourseEntity> wrapper);

    CourseVO selectVO(@Param("ew") Wrapper<CourseEntity> wrapper);

    List<CourseView> selectListView(@Param("ew") Wrapper<CourseEntity> wrapper);

    List<CourseView> selectListView(Pagination page, @Param("ew") Wrapper<CourseEntity> wrapper);

    CourseView selectView(@Param("ew") Wrapper<CourseEntity> wrapper);
}
