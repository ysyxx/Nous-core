package com.entity.view;

import com.entity.CourseAssessmentEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;


/**
 * 课程评价
 * 后端返回视图实体辅助类
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author
 * @email
 * @date 2024-03-05 18:16:19
 */
@TableName("course_assessment")
public class CourseAssessmentView extends CourseAssessmentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public CourseAssessmentView(){
	}

 	public CourseAssessmentView(CourseAssessmentEntity courseAssessmentEntity){
 	try {
			BeanUtils.copyProperties(this, courseAssessmentEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
