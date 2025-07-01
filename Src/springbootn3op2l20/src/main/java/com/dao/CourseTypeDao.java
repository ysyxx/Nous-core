package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import com.entity.CourseTypeEntity;
import com.entity.view.CourseTypeView;
import com.entity.vo.CourseTypeVO;
import org.apache.ibatis.annotations.Param;


/**
 * 课程类型
 *
 * @author
 * @email
 * @date 2024-03-05 18:16:19
 */
public interface CourseTypeDao extends BaseMapper<CourseTypeEntity> {

    List<CourseTypeVO> selectListVO(@Param("ew") Wrapper<CourseTypeEntity> wrapper);

    CourseTypeVO selectVO(@Param("ew") Wrapper<CourseTypeEntity> wrapper);

    List<CourseTypeView> selectListView(@Param("ew") Wrapper<CourseTypeEntity> wrapper);

    List<CourseTypeView> selectListView(Pagination page, @Param("ew") Wrapper<CourseTypeEntity> wrapper);


    CourseTypeView selectView(@Param("ew") Wrapper<CourseTypeEntity> wrapper);


}
