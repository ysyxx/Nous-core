package com.service.impl;

import com.dao.LessonTypeDao;
import com.entity.LessonTypeEntity;
import com.entity.view.LessonTypeView;
import com.entity.vo.LessonTypeVO;
import com.service.LessonTypeService;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;

@Service("LessonTypeService")
public class LessonTypeServiceImpl extends ServiceImpl<LessonTypeDao, LessonTypeEntity> implements LessonTypeService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<LessonTypeEntity> page = this.selectPage(
                new Query<LessonTypeEntity>(params).getPage(),
                new EntityWrapper<LessonTypeEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<LessonTypeEntity> wrapper) {
        Page<LessonTypeView> page =new Query<LessonTypeView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page,wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }


    @Override
    public List<LessonTypeVO> selectListVO(Wrapper<LessonTypeEntity> wrapper) {
        return baseMapper.selectListVO(wrapper);
    }

    @Override
    public LessonTypeVO selectVO(Wrapper<LessonTypeEntity> wrapper) {
        return baseMapper.selectVO(wrapper);
    }

    @Override
    public List<LessonTypeView> selectListView(Wrapper<LessonTypeEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public LessonTypeView selectView(Wrapper<LessonTypeEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }


}
