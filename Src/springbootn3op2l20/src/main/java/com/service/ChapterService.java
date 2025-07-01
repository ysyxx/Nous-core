package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.ChapterEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.ChapterVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.ChapterView;

/**
 * 课程章节 服务类
 *
 * @author
 * @email
 * @date 2025-06-28
 */
public interface ChapterService extends IService<ChapterEntity> {

    /**
     * @param params 查询参数
     * @return 分页查询结果
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 自定义查询（返回VO列表）
     * @param wrapper 查询条件
     * @return 列表
     */
    List<ChapterVO> selectListVO(Wrapper<ChapterEntity> wrapper);

    /**
     * 自定义查询（返回单个VO）
     * @param wrapper 查询条件
     * @return 单个结果
     */
    ChapterVO selectVO(@Param("ew") Wrapper<ChapterEntity> wrapper);

    /**
     * 自定义查询（返回View列表）
     * @param wrapper 查询条件
     * @return 列表
     */
    List<ChapterView> selectListView(Wrapper<ChapterEntity> wrapper);

    /**
     * 自定义查询（返回单个View）
     * @param wrapper 查询条件
     * @return 单个结果
     */
    ChapterView selectView(@Param("ew") Wrapper<ChapterEntity> wrapper);

    /**
     * 自定义分页查询
     * @param params 查询参数
     * @param wrapper 查询条件
     * @return 分页查询结果
     */
    PageUtils queryPage(Map<String, Object> params,Wrapper<ChapterEntity> wrapper);
}