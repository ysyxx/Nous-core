package com.controller;

import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;
import com.annotation.SysLog;

import com.entity.ChapterEntity;
import com.entity.view.ChapterView;
import com.service.ChapterService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;

/**
 * 课程章节
 * 后端接口
 */
@RestController
@RequestMapping("/chapter")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    /**
     * 后端列表查询 (带分页)
     * URL: /chapter/page
     * @param params 查询参数 (包含 courseId, page, limit 等)
     * @param chapter 章节实体，用于构建查询条件
     * @return 封装在 R 对象中的、包含章节列表的JSON数据
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ChapterEntity chapter,
                  HttpServletRequest request){

        // 创建查询包装器
        EntityWrapper<ChapterEntity> ew = new EntityWrapper<ChapterEntity>();

        // 【核心】如果 params 中包含 courseId，则将其作为查询条件
        if (params.get("courseId") != null) {
            ew.eq("course_id", params.get("courseId"));
        }

        // 使用 MPUtil 构建排序和模糊查询等
        PageUtils page = chapterService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, chapter), params), params));

        return R.ok().put("data", page);
    }

    /**
     * 后端详情查询 (通过ID)
     * URL: /chapter/info/{id}
     * @param id 章节ID
     * @return 封装在 R 对象中的、包含完整章节信息的JSON
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ChapterEntity chapter = chapterService.selectById(id);
        return R.ok().put("data", chapter);
    }

    /**
     * 后端保存/新增章节
     * URL: /chapter/save
     * @param chapter 待保存的章节实体
     * @return 封装在 R 对象中的操作结果
     */
    @RequestMapping("/save")
    @SysLog("新增课程章节")
    public R save(@RequestBody ChapterEntity chapter, HttpServletRequest request){
        // 使用 ValidatorUtils (如果需要) 对实体进行校验
        // ValidatorUtils.validateEntity(chapter);

        // 插入新的章节记录
        chapterService.insert(chapter);

        return R.ok();
    }

    /**
     * 修改章节信息
     * URL: /chapter/update
     * @param chapter 待修改的章节实体 (ID必须存在)
     * @return 封装在 R 对象中的操作结果
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改课程章节")
    public R update(@RequestBody ChapterEntity chapter, HttpServletRequest request){
        // ValidatorUtils.validateEntity(chapter);

        // 根据ID全部更新
        chapterService.updateById(chapter);

        return R.ok();
    }

    /**
     * 删除章节
     * URL: /chapter/delete
     * @param ids 待删除的章节ID数组
     * @return 封装在 R 对象中的操作结果
     */
    @RequestMapping("/delete")
    @SysLog("删除课程章节")
    public R delete(@RequestBody Long[] ids){
        chapterService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}