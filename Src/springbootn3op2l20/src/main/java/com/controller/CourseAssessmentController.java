package com.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

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

import com.entity.CourseAssessmentEntity;
import com.entity.view.CourseAssessmentView;

import com.service.CourseAssessmentService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;

/**
 * 课程评价
 * 后端接口
 * @author
 * @email
 * @date 2024-03-05 18:16:19
 */
@RestController
@RequestMapping("/courseAssessment")
public class CourseAssessmentController {
    @Autowired
    private CourseAssessmentService courseAssessmentService;








    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, CourseAssessmentEntity kechengpingjia,
                  HttpServletRequest request){

        System.out.println(params);
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("yonghu")) {
			kechengpingjia.setYonghuzhanghao((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<CourseAssessmentEntity> ew = new EntityWrapper<CourseAssessmentEntity>();

		PageUtils page = courseAssessmentService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, kechengpingjia), params), params));

        return R.ok().put("data", page);
    }

    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, CourseAssessmentEntity kechengpingjia,
                  HttpServletRequest request){
        EntityWrapper<CourseAssessmentEntity> ew = new EntityWrapper<CourseAssessmentEntity>();

		PageUtils page = courseAssessmentService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, kechengpingjia), params), params));
        return R.ok().put("data", page);
    }



	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( CourseAssessmentEntity kechengpingjia){
       	EntityWrapper<CourseAssessmentEntity> ew = new EntityWrapper<CourseAssessmentEntity>();
      	ew.allEq(MPUtil.allEQMapPre( kechengpingjia, "kechengpingjia"));
        return R.ok().put("data", courseAssessmentService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(CourseAssessmentEntity kechengpingjia){
        EntityWrapper<CourseAssessmentEntity> ew = new EntityWrapper<CourseAssessmentEntity>();
 		ew.allEq(MPUtil.allEQMapPre( kechengpingjia, "kechengpingjia"));
		CourseAssessmentView courseAssessmentView =  courseAssessmentService.selectView(ew);
		return R.ok("查询课程评价成功").put("data", courseAssessmentView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        CourseAssessmentEntity kechengpingjia = courseAssessmentService.selectById(id);
        return R.ok().put("data", kechengpingjia);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        CourseAssessmentEntity kechengpingjia = courseAssessmentService.selectById(id);
        return R.ok().put("data", kechengpingjia);
    }




    /**
     * 后端保存
     */
    @RequestMapping("/save")
    @SysLog("新增课程评价")
    public R save(@RequestBody CourseAssessmentEntity kechengpingjia, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(kechengpingjia);
        courseAssessmentService.insert(kechengpingjia);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @SysLog("新增课程评价")
    @RequestMapping("/add")
    public R add(@RequestBody CourseAssessmentEntity kechengpingjia, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(kechengpingjia);
        courseAssessmentService.insert(kechengpingjia);
        return R.ok();
    }


    /**
     * 总数
     */
    @RequestMapping("/count")
    public R count(@RequestParam Map<String, Object> params, CourseAssessmentEntity kechengpingjia, HttpServletRequest request){
        EntityWrapper<CourseAssessmentEntity> ew = new EntityWrapper<CourseAssessmentEntity>();
        int count = courseAssessmentService.selectCount(MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, kechengpingjia), params), params));
        return R.ok().put("count", count);
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改课程评价")
    public R update(@RequestBody CourseAssessmentEntity kechengpingjia, HttpServletRequest request){
        //ValidatorUtils.validateEntity(kechengpingjia);
        courseAssessmentService.updateById(kechengpingjia);//全部更新
        return R.ok();
    }

    /**
     * 审核
     */
    @RequestMapping("/shBatch")
    @Transactional
    @SysLog("审核课程评价")
    public R update(@RequestBody Long[] ids, @RequestParam String sfsh, @RequestParam String shhf){
        List<CourseAssessmentEntity> list = new ArrayList<CourseAssessmentEntity>();
        for(Long id : ids) {
            CourseAssessmentEntity kechengpingjia = courseAssessmentService.selectById(id);
            kechengpingjia.setSfsh(sfsh);
            kechengpingjia.setShhf(shhf);
            list.add(kechengpingjia);
        }
        courseAssessmentService.updateBatchById(list);
        return R.ok();
    }




    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除课程评价")
    public R delete(@RequestBody Long[] ids){
        courseAssessmentService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }












}
