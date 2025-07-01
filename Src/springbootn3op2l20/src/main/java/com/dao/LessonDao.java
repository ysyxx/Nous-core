package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.entity.LessonEntity;
import com.entity.view.LessonView;
import com.entity.vo.LessonVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课时表数据访问接口
 * * @author
 * @email
 * @date 2025-06-25
 */
public interface LessonDao extends BaseMapper<LessonEntity> {

    LessonEntity selectByChapterIdAndOrder(@Param("chapterId") Long chapterId, @Param("lessonOrder") Integer lessonOrder);

    List<LessonVO> selectListVO(@Param("ew") Wrapper<LessonEntity> wrapper);

    LessonVO selectVO(@Param("ew") Wrapper<LessonEntity> wrapper);

    List<LessonView> selectListView(@Param("ew") Wrapper<LessonEntity> wrapper);

    List<LessonView> selectListView(Pagination page, @Param("ew") Wrapper<LessonEntity> wrapper);


    LessonView selectView(@Param("ew") Wrapper<LessonEntity> wrapper);
}
