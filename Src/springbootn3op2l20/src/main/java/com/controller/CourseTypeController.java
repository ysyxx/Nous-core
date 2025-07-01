package com.controller;

import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.entity.CourseTypeEntity;
import com.entity.view.CourseTypeView;
import com.service.CourseTypeService;
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
@RequestMapping("/courseType")
public class CourseTypeController {
    @Autowired
    private CourseTypeService courseTypeService;


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, CourseTypeEntity lessonType,
                  HttpServletRequest request){
        EntityWrapper<CourseTypeEntity> ew = new EntityWrapper<CourseTypeEntity>();

        PageUtils page = courseTypeService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, lessonType), params), params));

        return R.ok().put("data", page);
    }

    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, CourseTypeEntity lessonType,
                  HttpServletRequest request){
        EntityWrapper<CourseTypeEntity> ew = new EntityWrapper<CourseTypeEntity>();

        PageUtils page = courseTypeService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, lessonType), params), params));
        return R.ok().put("data", page);
    }



    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( CourseTypeEntity lessonType){
        EntityWrapper<CourseTypeEntity> ew = new EntityWrapper<CourseTypeEntity>();
        ew.allEq(MPUtil.allEQMapPre( lessonType, "lessonType"));
        return R.ok().put("data", courseTypeService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(CourseTypeEntity lessonType){
        EntityWrapper<CourseTypeEntity> ew = new EntityWrapper<CourseTypeEntity>();
        ew.allEq(MPUtil.allEQMapPre( lessonType, "lessonType"));
        CourseTypeView courseTypeView =  courseTypeService.selectView(ew);
        return R.ok("查询课程类型成功").put("data", courseTypeView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        CourseTypeEntity kechengleixing = courseTypeService.selectById(id);
        return R.ok().put("data", kechengleixing);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        CourseTypeEntity lessonType = courseTypeService.selectById(id);
        return R.ok().put("data", lessonType);
    }




    /**
     * 后端保存
     */
    @RequestMapping("/save")
    @SysLog("新增课程类型")
    public R save(@RequestBody CourseTypeEntity lessonType, HttpServletRequest request){
        //ValidatorUtils.validateEntity(kechengleixing);
        courseTypeService.insert(lessonType);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @SysLog("新增课程类型")
    @RequestMapping("/add")
    public R add(@RequestBody CourseTypeEntity lessonType, HttpServletRequest request){
        //ValidatorUtils.validateEntity(kechengleixing);
        courseTypeService.insert(lessonType);
        return R.ok();
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改课程类型")
    public R update(@RequestBody CourseTypeEntity lessonType, HttpServletRequest request){
        //ValidatorUtils.validateEntity(kechengleixing);
        courseTypeService.updateById(lessonType);//全部更新
        return R.ok();
    }





    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除课程类型")
    public R delete(@RequestBody Long[] ids){
        courseTypeService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }












}
