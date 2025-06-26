package com.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;
import com.annotation.SysLog;

import com.entity.KechengpingjiaEntity;
import com.entity.view.KechengpingjiaView;

import com.service.KechengpingjiaService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 课程评价
 * 后端接口
 * @author 
 * @email 
 * @date 2024-03-05 18:16:19
 */
@RestController
@RequestMapping("/kechengpingjia")
public class KechengpingjiaController {
    @Autowired
    private KechengpingjiaService kechengpingjiaService;




    



    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,KechengpingjiaEntity kechengpingjia,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("yonghu")) {
			kechengpingjia.setYonghuzhanghao((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<KechengpingjiaEntity> ew = new EntityWrapper<KechengpingjiaEntity>();

		PageUtils page = kechengpingjiaService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, kechengpingjia), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,KechengpingjiaEntity kechengpingjia, 
		HttpServletRequest request){
        EntityWrapper<KechengpingjiaEntity> ew = new EntityWrapper<KechengpingjiaEntity>();

		PageUtils page = kechengpingjiaService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, kechengpingjia), params), params));
        return R.ok().put("data", page);
    }



	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( KechengpingjiaEntity kechengpingjia){
       	EntityWrapper<KechengpingjiaEntity> ew = new EntityWrapper<KechengpingjiaEntity>();
      	ew.allEq(MPUtil.allEQMapPre( kechengpingjia, "kechengpingjia")); 
        return R.ok().put("data", kechengpingjiaService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(KechengpingjiaEntity kechengpingjia){
        EntityWrapper< KechengpingjiaEntity> ew = new EntityWrapper< KechengpingjiaEntity>();
 		ew.allEq(MPUtil.allEQMapPre( kechengpingjia, "kechengpingjia")); 
		KechengpingjiaView kechengpingjiaView =  kechengpingjiaService.selectView(ew);
		return R.ok("查询课程评价成功").put("data", kechengpingjiaView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        KechengpingjiaEntity kechengpingjia = kechengpingjiaService.selectById(id);
        return R.ok().put("data", kechengpingjia);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        KechengpingjiaEntity kechengpingjia = kechengpingjiaService.selectById(id);
        return R.ok().put("data", kechengpingjia);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    @SysLog("新增课程评价") 
    public R save(@RequestBody KechengpingjiaEntity kechengpingjia, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(kechengpingjia);
        kechengpingjiaService.insert(kechengpingjia);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @SysLog("新增课程评价")
    @RequestMapping("/add")
    public R add(@RequestBody KechengpingjiaEntity kechengpingjia, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(kechengpingjia);
        kechengpingjiaService.insert(kechengpingjia);
        return R.ok();
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改课程评价")
    public R update(@RequestBody KechengpingjiaEntity kechengpingjia, HttpServletRequest request){
        //ValidatorUtils.validateEntity(kechengpingjia);
        kechengpingjiaService.updateById(kechengpingjia);//全部更新
        return R.ok();
    }

    /**
     * 审核
     */
    @RequestMapping("/shBatch")
    @Transactional
    @SysLog("审核课程评价")
    public R update(@RequestBody Long[] ids, @RequestParam String sfsh, @RequestParam String shhf){
        List<KechengpingjiaEntity> list = new ArrayList<KechengpingjiaEntity>();
        for(Long id : ids) {
            KechengpingjiaEntity kechengpingjia = kechengpingjiaService.selectById(id);
            kechengpingjia.setSfsh(sfsh);
            kechengpingjia.setShhf(shhf);
            list.add(kechengpingjia);
        }
        kechengpingjiaService.updateBatchById(list);
        return R.ok();
    }


    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除课程评价")
    public R delete(@RequestBody Long[] ids){
        kechengpingjiaService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	










}
