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

import com.dao.ChapterDao;
import com.entity.ChapterEntity;
import com.service.ChapterService;
import com.entity.vo.ChapterVO;
import com.entity.view.ChapterView;

@Service("chapterService")
public class ChapterServiceImpl extends ServiceImpl<ChapterDao, ChapterEntity> implements ChapterService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ChapterEntity> page = this.selectPage(
                new Query<ChapterEntity>(params).getPage(),
                new EntityWrapper<ChapterEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<ChapterEntity> wrapper) {
        Page<ChapterView> page =new Query<ChapterView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page,wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }

    @Override
    public List<ChapterVO> selectListVO(Wrapper<ChapterEntity> wrapper) {
        return baseMapper.selectListVO(wrapper);
    }

    @Override
    public ChapterVO selectVO(Wrapper<ChapterEntity> wrapper) {
        return baseMapper.selectVO(wrapper);
    }

    @Override
    public List<ChapterView> selectListView(Wrapper<ChapterEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public ChapterView selectView(Wrapper<ChapterEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }
}
