package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import com.entity.LessonTypeEntity;
import com.entity.view.LessonTypeView;
import com.entity.vo.LessonTypeVO;
import org.apache.ibatis.annotations.Param;


/**
 * 课程类型
 *
 * @author
 * @email
 * @date 2024-03-05 18:16:19
 */
public interface LessonTypeDao extends BaseMapper<LessonTypeEntity> {

    List<LessonTypeVO> selectListVO(@Param("ew") Wrapper<LessonTypeEntity> wrapper);

    LessonTypeVO selectVO(@Param("ew") Wrapper<LessonTypeEntity> wrapper);

    List<LessonTypeView> selectListView(@Param("ew") Wrapper<LessonTypeEntity> wrapper);

    List<LessonTypeView> selectListView(Pagination page,@Param("ew") Wrapper<LessonTypeEntity> wrapper);


    LessonTypeView selectView(@Param("ew") Wrapper<LessonTypeEntity> wrapper);


}
