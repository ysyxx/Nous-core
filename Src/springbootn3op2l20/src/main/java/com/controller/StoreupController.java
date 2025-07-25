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

import com.entity.CourseEntity;
import com.entity.LearningProgressEntity;
import com.service.CourseService;
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

import com.entity.StoreupEntity;
import com.entity.view.StoreupView;

import com.service.StoreupService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 收藏表
 * 后端接口
 * @author
 * @email
 * @date 2024-03-05 18:16:19
 */
@RestController
@RequestMapping("/storeup")
public class StoreupController {
    @Autowired
    private StoreupService storeupService;

    @Autowired
    private CourseService courseService;






    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,StoreupEntity storeup,
		HttpServletRequest request){
        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
            storeup.setUserId((Long)request.getSession().getAttribute("userId"));
        }
        EntityWrapper<StoreupEntity> ew = new EntityWrapper<StoreupEntity>();

		PageUtils page = storeupService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, storeup), params), params));

        return R.ok().put("data", page);
    }

    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,StoreupEntity storeup,
		HttpServletRequest request){
        EntityWrapper<StoreupEntity> ew = new EntityWrapper<StoreupEntity>();

		PageUtils page = storeupService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, storeup), params), params));
        return R.ok().put("data", page);
    }



	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( StoreupEntity storeup){
       	EntityWrapper<StoreupEntity> ew = new EntityWrapper<StoreupEntity>();
      	ew.allEq(MPUtil.allEQMapPre( storeup, "storeup"));
        return R.ok().put("data", storeupService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(StoreupEntity storeup){
        EntityWrapper< StoreupEntity> ew = new EntityWrapper< StoreupEntity>();
 		ew.allEq(MPUtil.allEQMapPre( storeup, "storeup"));
		StoreupView storeupView =  storeupService.selectView(ew);
		return R.ok("查询收藏表成功").put("data", storeupView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        StoreupEntity storeup = storeupService.selectById(id);
        return R.ok().put("data", storeup);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        StoreupEntity storeup = storeupService.selectById(id);
        return R.ok().put("data", storeup);
    }




    /**
     * 后端保存
     */
    @RequestMapping("/save")
    @SysLog("新增收藏表")
    public R save(@RequestBody StoreupEntity storeup, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(storeup);
    	storeup.setUserId((Long)request.getSession().getAttribute("userId"));
        storeupService.insert(storeup);
        return R.ok();
    }

    /**
     * 赞或踩
     */
    @RequestMapping("/add")
    public R add(@RequestBody StoreupEntity storeup){
        StoreupEntity storeupEntity=storeupService.getStoreupByUserIdAndCourseId(storeup.getUserId(),storeup.getCourseId());
        CourseEntity courseEntity=courseService.selectByCourseId(storeup.getCourseId());
        if(storeupEntity==null){
            storeupService.insert(storeup);
        }
        else{
            storeupEntity.setThumbsUp(storeup.getThumbsUp()>0?1:0);
            storeupEntity.setThumbsDown(storeup.getThumbsDown()>0?1:0);
            storeupEntity.setFavorite(storeup.getFavorite()>0?1:0);
            storeupService.updateById(storeupEntity);
        }

        if(storeup.getFavorite()==2){
            courseEntity.setStoreupNum(courseEntity.getStoreupNum()+1);
        }

        if(storeup.getThumbsUp()==2){
            courseEntity.setThumbsUpNum(courseEntity.getThumbsUpNum()+1);
        }

        if(storeup.getThumbsDown()==2){
            courseEntity.setCrazilyNum(courseEntity.getCrazilyNum()+1);
        }

        courseService.updateById(courseEntity);
        return R.ok();
    }


    /**
     * 取消赞或踩
     */
    @RequestMapping("/cancel")
    public R cancel(@RequestBody StoreupEntity storeup){
        StoreupEntity storeupEntity=storeupService.getStoreupByUserIdAndCourseId(storeup.getUserId(),storeup.getCourseId());
        CourseEntity courseEntity=courseService.selectByCourseId(storeup.getCourseId());

            storeupEntity.setThumbsUp(storeup.getThumbsUp()>0?1:0);
            storeupEntity.setThumbsDown(storeup.getThumbsDown()>0?1:0);
            storeupEntity.setFavorite(storeup.getFavorite()>0?1:0);
            storeupService.updateById(storeupEntity);



        if(storeup.getThumbsUp()==-1){
            courseEntity.setThumbsUpNum(courseEntity.getThumbsUpNum()-1);
        }

        if(storeup.getThumbsDown()==-1){
            courseEntity.setCrazilyNum(courseEntity.getCrazilyNum()-1);
        }

        if(storeup.getFavorite()==-1){
            courseEntity.setStoreupNum(courseEntity.getStoreupNum()-1);
        }
        courseService.updateById(courseEntity);
        return R.ok();
    }

    /**
     * 获取赞、踩、收藏的状态
     */
    @RequestMapping("/status")
    public R statue(@RequestBody StoreupEntity storeup){
        StoreupEntity storeupEntity=storeupService.getStoreupByUserIdAndCourseId(storeup.getUserId(),storeup.getCourseId());
        if(storeupEntity==null){
            storeupEntity=storeup;
            storeupService.insert(storeup);
        }

        return R.ok().put("data",storeupEntity);
    }


     /**
     * 获取用户密保
     */
    @RequestMapping("/security")
    @IgnoreAuth
    public R security(@RequestParam String username){
        StoreupEntity storeup = storeupService.selectOne(new EntityWrapper<StoreupEntity>().eq("", username));
        return R.ok().put("data", storeup);
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @IgnoreAuth
    public R update(@RequestBody StoreupEntity storeup, HttpServletRequest request){
        //ValidatorUtils.validateEntity(storeup);
        storeupService.updateById(storeup);//全部更新
        return R.ok();
    }





    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除收藏表")
    public R delete(@RequestBody Long[] ids){
        storeupService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }


	/**
     * 前端智能排序
     */
	@IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params,StoreupEntity storeup, HttpServletRequest request,String pre){
        EntityWrapper<StoreupEntity> ew = new EntityWrapper<StoreupEntity>();
        Map<String, Object> newMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
		Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			String newKey = entry.getKey();
			if (pre.endsWith(".")) {
				newMap.put(pre + newKey, entry.getValue());
			} else if (StringUtils.isEmpty(pre)) {
				newMap.put(newKey, entry.getValue());
			} else {
				newMap.put(pre + "." + newKey, entry.getValue());
			}
		}
		params.put("sort", "clicktime");
        params.put("order", "desc");
		PageUtils page = storeupService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, storeup), params), params));
        return R.ok().put("data", page);
    }










}
