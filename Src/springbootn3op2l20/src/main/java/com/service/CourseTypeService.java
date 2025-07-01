package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.CourseTypeEntity;
import com.entity.view.CourseTypeView;
import com.entity.vo.CourseTypeVO;
import com.utils.PageUtils;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


/**
 * 课程类型
 *
 * @author
 * @email
 * @date 2024-03-05 18:16:19
 */
public interface CourseTypeService extends IService<CourseTypeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CourseTypeVO> selectListVO(Wrapper<CourseTypeEntity> wrapper);

    CourseTypeVO selectVO(@Param("ew") Wrapper<CourseTypeEntity> wrapper);

    List<CourseTypeView> selectListView(Wrapper<CourseTypeEntity> wrapper);

    CourseTypeView selectView(@Param("ew") Wrapper<CourseTypeEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params,Wrapper<CourseTypeEntity> wrapper);



}

