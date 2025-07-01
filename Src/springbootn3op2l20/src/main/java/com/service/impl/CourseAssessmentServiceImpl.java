package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;


import com.dao.CourseAssessmentDao;
import com.entity.CourseAssessmentEntity;
import com.service.CourseAssessmentService;
import com.entity.vo.CourseAssessmentVO;
import com.entity.view.CourseAssessmentView;

@Service("courseAssessmentService")
public class CourseAssessmentServiceImpl extends ServiceImpl<CourseAssessmentDao, CourseAssessmentEntity> implements CourseAssessmentService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<CourseAssessmentEntity> page = this.selectPage(
                new Query<CourseAssessmentEntity>(params).getPage(),
                new EntityWrapper<CourseAssessmentEntity>()
        );
        return new PageUtils(page);
    }

    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<CourseAssessmentEntity> wrapper) {
		  Page<CourseAssessmentView> page =new Query<CourseAssessmentView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}


    @Override
	public List<CourseAssessmentVO> selectListVO(Wrapper<CourseAssessmentEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}

	@Override
	public CourseAssessmentVO selectVO(Wrapper<CourseAssessmentEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}

	@Override
	public List<CourseAssessmentView> selectListView(Wrapper<CourseAssessmentEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public CourseAssessmentView selectView(Wrapper<CourseAssessmentEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}


}
