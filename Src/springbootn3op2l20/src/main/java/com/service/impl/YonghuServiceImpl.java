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


import com.dao.YonghuDao;
import com.entity.YonghuEntity;
import com.service.YonghuService;
import com.entity.vo.YonghuVO;
import com.entity.view.YonghuView;

@Service("yonghuService")
public class YonghuServiceImpl extends ServiceImpl<YonghuDao, YonghuEntity> implements YonghuService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<YonghuEntity> page = this.selectPage(
                new Query<YonghuEntity>(params).getPage(),
                new EntityWrapper<YonghuEntity>()
        );
        return new PageUtils(page);
    }

    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<YonghuEntity> wrapper) {
		  Page<YonghuView> page =new Query<YonghuView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}

	@Override
	public boolean updateUserAvatar(Long userId, String avatarUrl) {
		if (userId == null || avatarUrl == null || avatarUrl.trim().isEmpty()) {
			throw new IllegalArgumentException("用户ID和头像URL不能为空。");
		}

		YonghuEntity yonghu = this.selectById(userId); // 使用 this.selectById 调用父类（ServiceImpl）的方法
		if (yonghu == null) {
			// 如果用户不存在，可以抛出异常或返回 false
			// throw new RuntimeException("用户不存在，ID：" + userId);
			return false;
		}

		yonghu.setTouxiang(avatarUrl); // 设置新的头像URL
		return this.updateById(yonghu); // 调用父类（ServiceImpl）的 updateById 方法更新数据库
	}


	@Override
	public List<YonghuVO> selectListVO(Wrapper<YonghuEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}

	@Override
	public YonghuVO selectVO(Wrapper<YonghuEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}

	@Override
	public List<YonghuView> selectListView(Wrapper<YonghuEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public YonghuView selectView(Wrapper<YonghuEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}


}
