package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.ForumEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.ForumVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.ForumView;


/**
 * 互动交流
 *
 * @author 
 * @email 
 * @date 2024-03-05 18:16:19
 */
public interface ForumService extends IService<ForumEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<ForumVO> selectListVO(Wrapper<ForumEntity> wrapper);
   	
   	ForumVO selectVO(@Param("ew") Wrapper<ForumEntity> wrapper);
   	
   	List<ForumView> selectListView(Wrapper<ForumEntity> wrapper);
   	
   	ForumView selectView(@Param("ew") Wrapper<ForumEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<ForumEntity> wrapper);

   	

}

