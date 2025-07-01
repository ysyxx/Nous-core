// 文件路径: src/main/java/com/controller/LearningProgressController.java

package com.controller;

// 导入我们自己定义的实体、服务和统一响应工具类
import com.entity.LearningProgressEntity;
import com.service.LearningProgressService;
import com.utils.R; // <-- 【关键】补上这一行对 R 类的导入

// 导入Spring的注解
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// 导入Java Servlet的类
import javax.servlet.http.HttpServletRequest;

// 导入Java工具类
import java.util.List;

@RestController
@RequestMapping("/learningprogress")
public class LearningProgressController {

    @Autowired
    private LearningProgressService learningProgressService;


    /**
     * 获取当前登录用户的所有学习进度
     * @param request
     * @return
     */
    @GetMapping("/myProgress")
    public R getMyProgress(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            return R.error(401, "用户未登录");
        }
        // 调用新的 Service 方法
        List<LearningProgressEntity> progressList = learningProgressService.getProgressWithLessonInfoByUserId(userId);
        return R.ok().put("data", progressList);
    }

    @GetMapping("/byCourseId")
    public R getProgressByCourseId(@RequestParam("courseId") Long courseId, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            return R.error(401, "用户未登录");
        }
        if (courseId == null) {
            return R.error("courseId 不能为空");
        }
        // 调用新的 Service 方法，传入 courseId 和 userId
        List<LearningProgressEntity> progressList = learningProgressService.getProgressWithLessonInfoByCourseIdAndUserId(courseId, userId);
        return R.ok().put("data", progressList);
    }
    private Long getCurrentUserId(HttpServletRequest request) {
        Object userIdObj = request.getSession().getAttribute("userId");
        if (userIdObj != null) {
            return Long.valueOf(userIdObj.toString());
        }
        // 或者从你的 TokenService 获取
        // String token = request.getHeader("token");
        // return tokenService.getUserId(token);
        return null; // 或者抛出异常
    }


    /**
     * 保存或更新学习进度
     * 前端 POST 请求，传入 LearningProgressEntity 对象（包含 lessonId, progressSeconds, isCompleted 等）
     */
    @RequestMapping("/saveOrUpdate")
    public R saveOrUpdate(@RequestBody LearningProgressEntity learningProgress, HttpServletRequest request) {

        Long userId = getCurrentUserId(request);
        if (userId == null) {
            return R.error("用户未登录或无法获取用户ID");
        }
        learningProgress.setUserId(userId); // 设置当前用户ID

        // 1. 查询是否存在该用户对该课程的学习进度
        LearningProgressEntity existingProgress = learningProgressService.getProgressByUserIdAndLessonId(userId, learningProgress.getLessonId());

        if (existingProgress != null) {
            if(existingProgress.getProgressSeconds()<=learningProgress.getProgressSeconds()){
                existingProgress.setProgressSeconds(learningProgress.getProgressSeconds());
            }

            existingProgress.setCompleted(learningProgress.getCompleted());
            existingProgress.setProgressPercent(learningProgress.getProgressPercent());
            existingProgress.setLastPosition(learningProgress.getLastPosition());

            learningProgressService.updateById(existingProgress);
        } else {
            // 3. 如果不存在，则插入新进度
            learningProgressService.insert(learningProgress);
        }

        return R.ok("学习进度保存成功");
    }

    /**
     * 获取特定课程的学习进度
     * 前端 GET 请求，传入 lessonId
     */
    @RequestMapping("/info")
    public R getProgressInfo(@RequestParam Long lessonId, HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        if (userId == null) {
            return R.error("用户未登录或无法获取用户ID");
        }

        LearningProgressEntity progress = learningProgressService.getProgressByUserIdAndLessonId(userId, lessonId);
        if (progress != null) {
            return R.ok().put("data", progress);
        } else {
            return R.ok().put("data", null).put("msg", "该课程无学习进度记录"); // 返回空数据表示无记录
        }
    }
}
