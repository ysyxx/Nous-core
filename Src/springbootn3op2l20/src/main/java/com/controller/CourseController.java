package com.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.attribute.CourseTypeEnum;
import com.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;
import com.annotation.SysLog;

import com.entity.CourseEntity; // 修改: KechengxinxiEntity -> CourseEntity
import com.entity.view.CourseView; // 修改: KechengxinxiView -> CourseView

import com.service.CourseService; // 修改: KechengxinxiService -> CourseService
import com.service.TokenService;

import java.io.IOException;
import com.service.StoreupService;
import com.entity.StoreupEntity;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private OBSUtil obsUtil;
    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, CourseEntity course,
                  HttpServletRequest request){
        EntityWrapper<CourseEntity> ew = new EntityWrapper<CourseEntity>();
        PageUtils page = courseService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, course), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 前端列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, CourseEntity course,
                  HttpServletRequest request){

        if (course.getCrazilyNum() != null && course.getCrazilyNum() == 0) {
            course.setCrazilyNum(null); // 将0值设为null，让MPUtil忽略
        }
        if (course.getThumbsUpNum() != null && course.getThumbsUpNum() == 0) {
            course.setThumbsUpNum(null);
        }
        if (course.getClickNum() != null && course.getClickNum() == 0) {
            course.setClickNum(null);
        }
        EntityWrapper<CourseEntity> ew = new EntityWrapper<CourseEntity>(); //
        PageUtils page = courseService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, course), params), params));

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
     * 后端保存
     */
    @PostMapping("/save")
    @SysLog("新增课程信息")
    public R save(
            @RequestParam("courseData") String courseJsonString, // 接收 JSON 字符串
            @RequestParam("file") MultipartFile file, // 接收上传的文件
            HttpServletRequest request
    ) {
        CourseEntity course;
        try {
            course = JSON.parseObject(courseJsonString, CourseEntity.class); // 使用 Fastjson
        } catch (Exception e) {
            return R.error("课程数据解析失败: " + e.getMessage());
        }

        CourseTypeEnum typeEnum = CourseTypeEnum.fromCode(course.getKechengleixing());
        if (typeEnum != null) {
            course.setKechengleixing(typeEnum.getDescription());
        }
        else{
            return R.error("不存在的课程类型编码: " + course.getKechengleixing());
        }

        course.setCrazilyNum(0);
        course.setClickNum(0);
        course.setThumbsUpNum(0);
        course.setAddtime(new Date()); // 如果是 java.util.Date
        course.setKechengpingfen(0);
        try {
            if (file.isEmpty()) {
                return R.error("上传文件不能为空");
            }

            String fileName = "courses/covers/" + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            String imageUrl = obsUtil.uploadFile(fileName, file.getInputStream());
            course.setCoverImageUrl(imageUrl);

        } catch (IOException e) {
            return R.error("图片文件上传失败: " + e.getMessage());
        }

        courseService.insert(course);
        return R.ok("创建新课程成功");
    }


    /**
     * 修改
     */
    @PostMapping("/update")
    @SysLog("修改课程信息")
    public R update(
            @RequestParam("courseData") String courseJsonString, // 接收 JSON 字符串
            @RequestParam("file") MultipartFile file, // 接收上传的文件
            HttpServletRequest request
    ) {
        CourseEntity course;
        try {
            course = JSON.parseObject(courseJsonString, CourseEntity.class); // 使用 Fastjson
        } catch (Exception e) {
            return R.error("课程数据解析失败: " + e.getMessage());
        }

        course.setCrazilyNum(0);
        course.setClickNum(0);
        course.setThumbsUpNum(0);
        course.setAddtime(new Date()); // 如果是 java.util.Date

        try {
            if (file.isEmpty()) {
                return R.error("上传文件不能为空");
            }

            String fileName = "courses/covers/" + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            String imageUrl = obsUtil.uploadFile(fileName, file.getInputStream());
            course.setCoverImageUrl(imageUrl);

        } catch (IOException e) {
            return R.error("图片文件上传失败: " + e.getMessage());
        }
        courseService.updateById(course);
        return R.ok("修改课程成功");
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除课程信息")
    public R delete(@RequestBody Long[] ids){
        courseService.deleteBatchIds(Arrays.asList(ids));
        return R.ok("删除成功");
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
        params.put("sort", "click_num");
        params.put("order", "desc");
        PageUtils page = courseService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, course), params), params)); // 修改: kechengxinxiService -> courseService, kechengxinxi -> course
        return R.ok().put("data", page);
    }

    /**
     * 总数
     */
    @RequestMapping("/count")
    public R count(@RequestParam Map<String, Object> params,CourseEntity course, HttpServletRequest request){
        EntityWrapper<CourseEntity> ew = new EntityWrapper<CourseEntity>();
        int count = courseService.selectCount(MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, course), params), params));
        return R.ok().put("count", count);
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
                if(ratings.containsKey(storeup.getUserId().toString())) {
                    userRatings = ratings.get(storeup.getUserId().toString());
                } else {
                    userRatings = new HashMap<>();
                    ratings.put(storeup.getUserId().toString(), userRatings);
                }

                if(userRatings.containsKey(storeup.getCourseId().toString())) {
                    userRatings.put(storeup.getCourseId().toString(), userRatings.get(storeup.getCourseId().toString())+1.0);
                } else {
                    userRatings.put(storeup.getCourseId().toString(), 1.0);
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
