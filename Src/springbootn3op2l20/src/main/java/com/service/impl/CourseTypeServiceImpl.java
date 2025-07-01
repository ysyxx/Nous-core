package com.service.impl;

import com.dao.CourseTypeDao;
import com.entity.CourseTypeEntity;
import com.entity.view.CourseTypeView;
import com.entity.vo.CourseTypeVO;
import com.service.CourseTypeService;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;

@Service("CourseTypeService")
public class CourseTypeServiceImpl extends ServiceImpl<CourseTypeDao, CourseTypeEntity> implements CourseTypeService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<CourseTypeEntity> page = this.selectPage(
                new Query<CourseTypeEntity>(params).getPage(),
                new EntityWrapper<CourseTypeEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<CourseTypeEntity> wrapper) {
        Page<CourseTypeView> page =new Query<CourseTypeView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page,wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }


    @Override
    public List<CourseTypeVO> selectListVO(Wrapper<CourseTypeEntity> wrapper) {
        return baseMapper.selectListVO(wrapper);
    }

    @Override
    public CourseTypeVO selectVO(Wrapper<CourseTypeEntity> wrapper) {
        return baseMapper.selectVO(wrapper);
    }

    @Override
    public List<CourseTypeView> selectListView(Wrapper<CourseTypeEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public CourseTypeView selectView(Wrapper<CourseTypeEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }


}
