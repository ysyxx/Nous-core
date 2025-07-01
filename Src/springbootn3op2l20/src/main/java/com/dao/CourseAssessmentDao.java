package com.dao;

import com.entity.CourseAssessmentEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.CourseAssessmentVO;
import com.entity.view.CourseAssessmentView;


/**
 * 课程评价
 *
 * @author
 * @email
 * @date 2024-03-05 18:16:19
 */
public interface CourseAssessmentDao extends BaseMapper<CourseAssessmentEntity> {

	List<CourseAssessmentVO> selectListVO(@Param("ew") Wrapper<CourseAssessmentEntity> wrapper);

	CourseAssessmentVO selectVO(@Param("ew") Wrapper<CourseAssessmentEntity> wrapper);

	List<CourseAssessmentView> selectListView(@Param("ew") Wrapper<CourseAssessmentEntity> wrapper);

	List<CourseAssessmentView> selectListView(Pagination page, @Param("ew") Wrapper<CourseAssessmentEntity> wrapper);


	CourseAssessmentView selectView(@Param("ew") Wrapper<CourseAssessmentEntity> wrapper);


}
