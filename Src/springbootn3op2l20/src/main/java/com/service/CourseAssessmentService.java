package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.CourseAssessmentEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.CourseAssessmentVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.CourseAssessmentView;


/**
 * 课程评价
 *
 * @author
 * @email
 * @date 2024-03-05 18:16:19
 */
public interface CourseAssessmentService extends IService<CourseAssessmentEntity> {

    PageUtils queryPage(Map<String, Object> params);

   	List<CourseAssessmentVO> selectListVO(Wrapper<CourseAssessmentEntity> wrapper);

   	CourseAssessmentVO selectVO(@Param("ew") Wrapper<CourseAssessmentEntity> wrapper);

   	List<CourseAssessmentView> selectListView(Wrapper<CourseAssessmentEntity> wrapper);

   	CourseAssessmentView selectView(@Param("ew") Wrapper<CourseAssessmentEntity> wrapper);

   	PageUtils queryPage(Map<String, Object> params,Wrapper<CourseAssessmentEntity> wrapper);



}

