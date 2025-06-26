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
@RequestMapping("/learning-progress")
public class LearningProgressController {

    @Autowired
    private LearningProgressService learningProgressService;

    /**
     * 前端上报学习进度
     * 请求体需要包含 lessonId 和 progressSeconds
     * @param progressEntity 包含进度的JSON对象
     * @param request 用于获取当前登录的用户ID
     * @return
     */
    @PostMapping("/update")
    public R updateProgress(@RequestBody LearningProgressEntity progressEntity, HttpServletRequest request) {
        // 从Session或Token中获取当前登录的用户ID (与你项目中其他Controller保持一致)
        // 注意：这里的"userId"必须和你们登录时存入session的键名完全一致
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            return R.error(401, "用户未登录");
        }

        // 将用户ID设置到实体中
        progressEntity.setUserId(userId);

        if (progressEntity.getLessonId() == null || progressEntity.getProgressSeconds() == null) {
            return R.error("缺少课时ID或进度秒数");
        }

        boolean success = learningProgressService.updateOrSaveUserProgress(progressEntity);

        return success ? R.ok("进度更新成功") : R.error("进度更新失败");
    }

    /**
     * 获取当前登录用户的所有学习进度
     * @param request
     * @return
     */
    @GetMapping("/my-progress")
    public R getMyProgress(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            return R.error(401, "用户未登录");
        }
        List<LearningProgressEntity> progressList = learningProgressService.getProgressByUserId(userId);
        return R.ok().put("data", progressList);
    }
}