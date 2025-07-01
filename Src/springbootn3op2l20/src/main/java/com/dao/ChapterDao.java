package com.dao;

import com.entity.ChapterEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.ChapterVO;
import com.entity.view.ChapterView;

/**
 * 课程章节 DAO接口
 * * @author
 * @email
 * @date 2025-06-28
 */
public interface ChapterDao extends BaseMapper<ChapterEntity> {

    /**
     * 自定义查询，返回VO列表
     * @param wrapper 查询条件
     * @return VO列表
     */
    List<ChapterVO> selectListVO(@Param("ew") Wrapper<ChapterEntity> wrapper);

    /**
     * 自定义查询，返回单个VO
     * @param wrapper 查询条件
     * @return 单个VO
     */
    ChapterVO selectVO(@Param("ew") Wrapper<ChapterEntity> wrapper);

    /**
     * 自定义查询，返回View列表（通常用于连表查询）
     * @param wrapper 查询条件
     * @return View列表
     */
    List<ChapterView> selectListView(@Param("ew") Wrapper<ChapterEntity> wrapper);

    /**
     * 自定义分页查询，返回View列表
     * @param page 分页信息
     * @param wrapper 查询条件
     * @return 带分页的View列表
     */
    List<ChapterView> selectListView(Pagination page, @Param("ew") Wrapper<ChapterEntity> wrapper);

    /**
     * 自定义查询，返回单个View
     * @param wrapper 查询条件
     * @return 单个View
     */
    ChapterView selectView(@Param("ew") Wrapper<ChapterEntity> wrapper);
}