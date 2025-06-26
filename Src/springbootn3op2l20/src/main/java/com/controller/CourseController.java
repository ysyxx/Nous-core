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
import com.utils.UserBasedCollaborativeFiltering;

import com.entity.CourseEntity; // 修改: KechengxinxiEntity -> CourseEntity
import com.entity.view.CourseView; // 修改: KechengxinxiView -> CourseView

import com.service.CourseService; // 修改: KechengxinxiService -> CourseService
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;
import com.service.StoreupService;
import com.entity.StoreupEntity;

/**
 * 课程信息
 * 后端接口
 * @author
 * @email
 * @date 2024-03-05 18:16:19
 */
@RestController
@RequestMapping("/course") // 修改: /kechengxinxi -> /courses
public class CourseController { // 修改: KechengxinxiController -> CourseController
    @Autowired
    private CourseService courseService; // 修改: kechengxinxiService -> courseService

    @Autowired
    private StoreupService storeupService;

    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, CourseEntity course, // 修改: KechengxinxiEntity kechengxinxi -> CourseEntity course
                  HttpServletRequest request){
        EntityWrapper<CourseEntity> ew = new EntityWrapper<CourseEntity>(); // 修改: KechengxinxiEntity -> CourseEntity

        PageUtils page = courseService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, course), params), params)); // 修改: kechengxinxi -> course

        return R.ok().put("data", page);
    }

    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, CourseEntity course, // 修改: KechengxinxiEntity kechengxinxi -> CourseEntity course
                  HttpServletRequest request){
        EntityWrapper<CourseEntity> ew = new EntityWrapper<CourseEntity>(); // 修改: KechengxinxiEntity -> CourseEntity

        PageUtils page = courseService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, course), params), params)); // 修改: kechengxinxi -> course
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( CourseEntity course){ // 修改: KechengxinxiEntity kechengxinxi -> CourseEntity course
        EntityWrapper<CourseEntity> ew = new EntityWrapper<CourseEntity>(); // 修改: KechengxinxiEntity -> CourseEntity
        ew.allEq(MPUtil.allEQMapPre( course, "course")); // 修改: kechengxinxi -> course, "kechengxinxi" -> "course"
        return R.ok().put("data", courseService.selectListView(ew)); // 修改: kechengxinxiService -> courseService
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(CourseEntity course){ // 修改: KechengxinxiEntity kechengxinxi -> CourseEntity course
        EntityWrapper< CourseEntity> ew = new EntityWrapper< CourseEntity>(); // 修改: KechengxinxiEntity -> CourseEntity
        ew.allEq(MPUtil.allEQMapPre( course, "course")); // 修改: kechengxinxi -> course, "kechengxinxi" -> "course"
        CourseView courseView =  courseService.selectView(ew); // 修改: kechengxinxiService -> courseService, KechengxinxiView -> CourseView
        return R.ok("查询课程信息成功").put("data", courseView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        CourseEntity course = courseService.selectById(id); // 修改: KechengxinxiEntity kechengxinxi -> CourseEntity course, kechengxinxiService -> courseService
        course.setClickNum(course.getClickNum()+1); // 修改: kechengxinxi -> course
        courseService.updateById(course); // 修改: kechengxinxiService -> courseService, kechengxinxi -> course
        course = courseService.selectView(new EntityWrapper<CourseEntity>().eq("id", id)); // 修改: kechengxinxiService -> courseService, KechengxinxiEntity -> CourseEntity, kechengxinxi -> course
        return R.ok().put("data", course);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        CourseEntity course = courseService.selectById(id); // 修改: KechengxinxiEntity kechengxinxi -> CourseEntity course, kechengxinxiService -> courseService
        course.setClickNum(course.getClickNum()+1); // 修改: kechengxinxi -> course
        courseService.updateById(course); // 修改: kechengxinxiService -> courseService, kechengxinxi -> course
        course = courseService.selectView(new EntityWrapper<CourseEntity>().eq("id", id)); // 修改: kechengxinxiService -> courseService, KechengxinxiEntity -> CourseEntity, kechengxinxi -> course
        return R.ok().put("data", course);
    }

    /**
     * 赞或踩
     */
    @RequestMapping("/thumbsup/{id}")
    public R vote(@PathVariable("id") String id,String type){
        CourseEntity course = courseService.selectById(id); // 修改: KechengxinxiEntity kechengxinxi -> CourseEntity course, kechengxinxiService -> courseService
        if(type.equals("1")) {
            course.setThumbsUpNum(course.getThumbsUpNum()+1); // 修改: kechengxinxi -> course
        } else {
            course.setCrazilyNum(course.getCrazilyNum()+1); // 修改: kechengxinxi -> course
        }
        courseService.updateById(course); // 修改: kechengxinxiService -> courseService, kechengxinxi -> course
        return R.ok("投票成功");
    }

    /**
     * 后端保存
     */
    @RequestMapping("/save")
    @SysLog("新增课程信息")
    public R save(@RequestBody CourseEntity course, HttpServletRequest request){ // 修改: KechengxinxiEntity kechengxinxi -> CourseEntity course
        //ValidatorUtils.validateEntity(course); // 修改: kechengxinxi -> course
        courseService.insert(course); // 修改: kechengxinxiService -> courseService, kechengxinxi -> course
        return R.ok();
    }

    /**
     * 前端保存
     */
    @SysLog("新增课程信息")
    @RequestMapping("/add")
    public R add(@RequestBody CourseEntity course, HttpServletRequest request){ // 修改: KechengxinxiEntity kechengxinxi -> CourseEntity course
        //ValidatorUtils.validateEntity(course); // 修改: kechengxinxi -> course
        courseService.insert(course); // 修改: kechengxinxiService -> courseService, kechengxinxi -> course
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改课程信息")
    public R update(@RequestBody CourseEntity course, HttpServletRequest request){ // 修改: KechengxinxiEntity kechengxinxi -> CourseEntity course
        //ValidatorUtils.validateEntity(course); // 修改: kechengxinxi -> course
        courseService.updateById(course);//全部更新 // 修改: kechengxinxiService -> courseService, kechengxinxi -> course
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除课程信息")
    public R delete(@RequestBody Long[] ids){
        courseService.deleteBatchIds(Arrays.asList(ids)); // 修改: kechengxinxiService -> courseService
        return R.ok();
    }

    /**
     * 前端智能排序
     */
    @IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params, CourseEntity course, HttpServletRequest request,String pre){ // 修改: KechengxinxiEntity kechengxinxi -> CourseEntity course
        EntityWrapper<CourseEntity> ew = new EntityWrapper<CourseEntity>(); // 修改: KechengxinxiEntity -> CourseEntity
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
        params.put("sort", "clicknum");
        params.put("order", "desc");
        PageUtils page = courseService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, course), params), params)); // 修改: kechengxinxiService -> courseService, kechengxinxi -> course
        return R.ok().put("data", page);
    }

    /**
     * 协同算法（基于用户收藏的协同算法）
     */
    @RequestMapping("/autoSort2")
    public R autoSort2(@RequestParam Map<String, Object> params, CourseEntity course, HttpServletRequest request){ // 修改: KechengxinxiEntity kechengxinxi -> CourseEntity course
        String userId = request.getSession().getAttribute("userId").toString();
        Integer limit = params.get("limit")==null?10:Integer.parseInt(params.get("limit").toString());
        List<StoreupEntity> storeups = storeupService.selectList(new EntityWrapper<StoreupEntity>().eq("type", 1).eq("tablename", "course")); // 修改: "kechengxinxi" -> "course"
        Map<String, Map<String, Double>> ratings = new HashMap<>();
        if(storeups!=null && storeups.size()>0) {
            for(StoreupEntity storeup : storeups) {
                Map<String, Double> userRatings = null;
                if(ratings.containsKey(storeup.getUserid().toString())) {
                    userRatings = ratings.get(storeup.getUserid().toString());
                } else {
                    userRatings = new HashMap<>();
                    ratings.put(storeup.getUserid().toString(), userRatings);
                }

                if(userRatings.containsKey(storeup.getRefid().toString())) {
                    userRatings.put(storeup.getRefid().toString(), userRatings.get(storeup.getRefid().toString())+1.0);
                } else {
                    userRatings.put(storeup.getRefid().toString(), 1.0);
                }
            }
        }
        // 创建协同过滤对象
        UserBasedCollaborativeFiltering filter = new UserBasedCollaborativeFiltering(ratings);

        // 为指定用户推荐物品
        String targetUser = userId;
        int numRecommendations = limit;
        List<String> recommendations = filter.recommendItems(targetUser, numRecommendations);

        // 输出推荐结果
        System.out.println("Recommendations for " + targetUser + ":");
        for (String item : recommendations) {
            System.out.println(item);
        }

        EntityWrapper<CourseEntity> ew = new EntityWrapper<CourseEntity>(); // 修改: KechengxinxiEntity -> CourseEntity
        ew.in("id", String.join(",", recommendations));
        if(recommendations!=null && recommendations.size()>0) {
            ew.last("order by FIELD(id, "+"'"+String.join("','", recommendations)+"'"+")");
        }

        PageUtils page = courseService.queryPage(params, ew); // 修改: kechengxinxiService -> courseService
        List<CourseEntity> pageList = (List<CourseEntity>)page.getList(); // 修改: KechengxinxiEntity -> CourseEntity
        if(pageList.size()<limit) {
            int toAddNum = limit-pageList.size();
            ew = new EntityWrapper<CourseEntity>(); // 修改: KechengxinxiEntity -> CourseEntity
            ew.notIn("id", recommendations);
            ew.orderBy("id", false);
            ew.last("limit "+toAddNum);
            pageList.addAll(courseService.selectList(ew)); // 修改: kechengxinxiService -> courseService
        } else if(pageList.size()>limit) {
            pageList = pageList.subList(0, limit);
        }
        page.setList(pageList);

        return R.ok().put("data", page);
    }
}
