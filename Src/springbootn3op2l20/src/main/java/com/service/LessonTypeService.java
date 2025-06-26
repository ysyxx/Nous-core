package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.LessonTypeEntity;
import com.entity.view.LessonTypeView;
import com.entity.vo.LessonTypeVO;
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
public interface LessonTypeService extends IService<LessonTypeEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<LessonTypeVO> selectListVO(Wrapper<LessonTypeEntity> wrapper);

    LessonTypeVO selectVO(@Param("ew") Wrapper<LessonTypeEntity> wrapper);

    List<LessonTypeView> selectListView(Wrapper<LessonTypeEntity> wrapper);

    LessonTypeView selectView(@Param("ew") Wrapper<LessonTypeEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params,Wrapper<LessonTypeEntity> wrapper);



}

