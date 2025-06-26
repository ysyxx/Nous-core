// CourseService.java
package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.CourseEntity;
import com.entity.view.CourseView;
import com.entity.vo.CourseVO;
import com.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CourseService extends IService<CourseEntity> {
    CourseEntity getCourseWithDetails(Long courseId);


    PageUtils queryPage(Map<String, Object> params);

    List<CourseVO> selectListVO(Wrapper<CourseEntity> wrapper);

    CourseVO selectVO(@Param("ew") Wrapper<CourseEntity> wrapper);

    List<CourseView> selectListView(Wrapper<CourseEntity> wrapper);

    CourseView selectView(@Param("ew") Wrapper<CourseEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params,Wrapper<CourseEntity> wrapper);
}
