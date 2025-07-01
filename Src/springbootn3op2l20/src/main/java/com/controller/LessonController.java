package com.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.dto.ChapterWithLessonsDTO;
import com.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;
import com.annotation.SysLog;

import com.entity.LessonEntity;       // 导入 Lesson 实体类
import com.entity.view.LessonView;     // 导入 LessonView (如果你的项目有)
import com.service.LessonService;      // 导入 LessonService
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;

/**
 * 课程节信息
 * 后端接口
 * @author
 * @email
 * @date 2025-06-26 14:00:00 (示例日期，请根据实际生成时间调整)
 */
@RestController
@RequestMapping("/lessons") // 定义该控制器下所有接口的基础URL路径为 /lessons
public class LessonController {

    @Autowired // 自动注入 LessonService 的实例
    private LessonService lessonService;

    /**
     * 后端列表查询 (带分页)
     * URL: /lessons/page
     * @param params 查询参数 (如 page, limit, title等)
     * @param lesson 课程节实体，用于精确查询或模糊查询
     * @param request HTTP请求对象
     * @return 封装在 R 对象中的、包含课程节列表的JSON数据
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, LessonEntity lesson,
                  HttpServletRequest request){
        EntityWrapper<LessonEntity> ew = new EntityWrapper<LessonEntity>();
        PageUtils page = lessonService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, lesson), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 前端列表查询 (带分页，通常无需认证)
     * URL: /lessons/list
     * @param params 查询参数
     * @param lesson 课程节实体
     * @param request HTTP请求对象
     * @return 封装在 R 对象中的、包含课程节列表的JSON数据
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, LessonEntity lesson,
                  HttpServletRequest request){
        EntityWrapper<LessonEntity> ew = new EntityWrapper<LessonEntity>();
        PageUtils page = lessonService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, lesson), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 完整列表查询 (不带分页)
     * URL: /lessons/lists
     * @param lesson 课程节实体，用于精确查询
     * @return 封装在 R 对象中的、包含所有课程节的JSON数据
     */
    @RequestMapping("/lists")
    public R list(LessonEntity lesson){
        EntityWrapper<LessonEntity> ew = new EntityWrapper<LessonEntity>();
        ew.allEq(MPUtil.allEQMapPre(lesson, "lesson")); // 注意这里的别名 "lesson"
        return R.ok().put("data", lessonService.selectListView(ew));
    }

    /**
     * 查询单个课程节 (通过Wrapper)
     * URL: /lessons/query
     * @param lesson 课程节实体，包含查询条件
     * @return 封装在 R 对象中的、包含单个课程节详情的JSON数据
     */
    @RequestMapping("/query")
    public R query(LessonEntity lesson){
        EntityWrapper<LessonEntity> ew = new EntityWrapper<LessonEntity>();
        ew.allEq(MPUtil.allEQMapPre(lesson, "lesson")); // 注意这里的别名 "lesson"
        LessonView lessonView = lessonService.selectView(ew);
        return R.ok("查询课程节信息成功").put("data", lessonView);
    }

    /**
     * 后端详情查询 (通过ID)
     * URL: /lessons/info/{id}
     * @param id 课程节ID
     * @return 封装在 R 对象中的、包含完整课程节信息的JSON
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        LessonEntity lesson = lessonService.selectById(id);
        // 如果 lessons 表有 clicknum 字段并需要统计浏览量，可以在这里增加：
        // lesson.setClicknum(lesson.getClicknum() + 1);
        // lessonService.updateById(lesson);
        // lesson = lessonService.selectView(new EntityWrapper<LessonEntity>().eq("id", id));
        return R.ok().put("data", lesson);
    }

    /**
     * 前端详情查询 (通过ID，通常无需认证)
     * URL: /lessons/detail/{id}
     * @param id 课程节ID
     * @return 封装在 R 对象中的、包含完整课程节信息的JSON
     */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        LessonEntity lesson = lessonService.selectById(id);

        return R.ok().put("data", lesson);
    }

    /**
     * 保存/新增课程节 (后端接口)
     * URL: /lessons/save
     * @param lesson 待保存的课程节实体
     * @param request HTTP请求对象
     * @return 封装在 R 对象中的操作结果
     */
    @RequestMapping("/add")
    @SysLog("新增课程节信息") // 记录系统日志
    public R save(@RequestBody LessonEntity lesson, HttpServletRequest request){
        ValidatorUtils.validateEntity(lesson); // 数据校验
        lessonService.insert(lesson);
        return R.ok();
    }


    /**
     * 修改课程节信息
     * URL: /lessons/update
     * @param lesson 待修改的课程节实体 (ID必须存在)
     * @param request HTTP请求对象
     * @return 封装在 R 对象中的操作结果
     */
    @RequestMapping("/update")
    @Transactional // 开启事务
    @SysLog("修改课程节信息")
    public R update(@RequestBody LessonEntity lesson, HttpServletRequest request){
        ValidatorUtils.validateEntity(lesson);
        lessonService.updateById(lesson); // 根据ID全部更新
        return R.ok();
    }

    /**
     * 删除课程节
     * URL: /lessons/delete
     * @param ids 待删除的课程节ID数组
     * @return 封装在 R 对象中的操作结果
     */
    @RequestMapping("/delete")
    @SysLog("删除课程节信息")
    public R delete(@RequestBody Long[] ids){
        lessonService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 获取指定课程节的视频URL
     * 通过 chapter_id 和 lesson_order 确定课时，并根据 lesson_type 判断是否有视频。
     * URL: /lessons/getVideoUrl
     * @param chapterId 章节ID
     * @param lessonOrder 课时顺序
     * @return R 对象，成功时包含 "videoUrl" 字段，失败时返回错误信息
     */
    @RequestMapping("/getVideoUrl")
    public R getVideoUrl(@RequestParam("chapterId") Long chapterId,
                         @RequestParam("lessonOrder") Integer lessonOrder,
                         @RequestParam("courseId") Integer courseId) {
        EntityWrapper<LessonEntity> ew = new EntityWrapper<>();
        ew.eq("chapter_id", chapterId);
        ew.eq("lesson_order", lessonOrder);
        ew.eq("course_id", courseId);

        LessonEntity lesson = lessonService.selectOne(ew);

        if (lesson == null) {
            return R.error("未找到对应的课程节。");
        }

        // 根据 lesson_type 判断是否有视频
        if ("video".equalsIgnoreCase(lesson.getLessonType())) {
            if (lesson.getVideoUrl() != null && !lesson.getVideoUrl().isEmpty()) {
                return R.ok().put("videoUrl", lesson.getVideoUrl());
            } else {
                return R.error("该视频课程节没有对应的视频URL。");
            }
        } else {
            return R.error("该课程节类型不是视频，无法提供视频URL。");
        }
    }


    /**
     * 获取一门课程所有课的信息
     * */
//    @GetMapping("/listByCourseId")
//    public R listByCourseId(@RequestParam("courseId") Long courseId) {
//        if (courseId == null) {
//            return R.error("课程ID不能为空。");
//        }
//        // 调用新的 Service 方法
//        List<ChapterWithLessonsDTO> courseStructure = lessonService.getCourseStructureByCourseId(courseId);
//        return R.ok().put("data", courseStructure);
//    }
    @GetMapping("/listByCourseId")

    public R listByCourseId(@RequestParam("courseId") Long courseId) {

        if (courseId == null) {

            return R.error("课程ID不能为空。");

        }

// 使用 EntityWrapper 构建查询条件，按 course_session 升序排列

        List<LessonEntity> lessonList = lessonService.selectList(

                new EntityWrapper<LessonEntity>()

                        .eq("course_id", courseId)

                        .orderBy("chapter_order", true)

                        .orderBy("lesson_order", true)

        );

        return R.ok().put("data", lessonList);

    }
}
