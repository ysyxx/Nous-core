package com.controller;

import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.entity.LessonTypeEntity;
import com.entity.view.LessonTypeView;
import com.service.LessonTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.annotation.IgnoreAuth;
import com.annotation.SysLog;

import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;

/**
 * 课程类型
 * 后端接口
 * @author
 * @email
 * @date 2024-03-05 18:16:19
 */
@RestController
@RequestMapping("/lessonType")
public class LessonTypeController {
    @Autowired
    private LessonTypeService lessonTypeService;


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, LessonTypeEntity lessonType,
                  HttpServletRequest request){
        EntityWrapper<LessonTypeEntity> ew = new EntityWrapper<LessonTypeEntity>();

        PageUtils page = lessonTypeService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, lessonType), params), params));

        return R.ok().put("data", page);
    }

    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,LessonTypeEntity lessonType,
                  HttpServletRequest request){
        EntityWrapper<LessonTypeEntity> ew = new EntityWrapper<LessonTypeEntity>();

        PageUtils page = lessonTypeService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, lessonType), params), params));
        return R.ok().put("data", page);
    }



    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( LessonTypeEntity lessonType){
        EntityWrapper<LessonTypeEntity> ew = new EntityWrapper<LessonTypeEntity>();
        ew.allEq(MPUtil.allEQMapPre( lessonType, "lessonType"));
        return R.ok().put("data", lessonTypeService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(LessonTypeEntity lessonType){
        EntityWrapper< LessonTypeEntity> ew = new EntityWrapper< LessonTypeEntity>();
        ew.allEq(MPUtil.allEQMapPre( lessonType, "lessonType"));
        LessonTypeView lessonTypeView =  lessonTypeService.selectView(ew);
        return R.ok("查询课程类型成功").put("data", lessonTypeView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        LessonTypeEntity kechengleixing = lessonTypeService.selectById(id);
        return R.ok().put("data", kechengleixing);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        LessonTypeEntity lessonType = lessonTypeService.selectById(id);
        return R.ok().put("data", lessonType);
    }




    /**
     * 后端保存
     */
    @RequestMapping("/save")
    @SysLog("新增课程类型")
    public R save(@RequestBody LessonTypeEntity lessonType, HttpServletRequest request){
        //ValidatorUtils.validateEntity(kechengleixing);
        lessonTypeService.insert(lessonType);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @SysLog("新增课程类型")
    @RequestMapping("/add")
    public R add(@RequestBody LessonTypeEntity lessonType, HttpServletRequest request){
        //ValidatorUtils.validateEntity(kechengleixing);
        lessonTypeService.insert(lessonType);
        return R.ok();
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改课程类型")
    public R update(@RequestBody LessonTypeEntity lessonType, HttpServletRequest request){
        //ValidatorUtils.validateEntity(kechengleixing);
        lessonTypeService.updateById(lessonType);//全部更新
        return R.ok();
    }





    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除课程类型")
    public R delete(@RequestBody Long[] ids){
        lessonTypeService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }












}
