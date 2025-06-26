// CourseController.java
package com.controller;

// 导入我们自己定义的实体、服务和统一响应工具类
import com.entity.CourseEntity;
import com.service.CourseService;
import com.utils.R; // 这是你项目中用于封装JSON响应的工具类

// 导入Spring的注解
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 声明这是一个RESTful风格的控制器，所有方法默认返回JSON
@RequestMapping("/courses") // 定义该控制器下所有接口的基础URL路径为 /courses
public class CourseController {

    @Autowired // 自动注入CourseService的实例
    private CourseService courseService;

    /**
     * 定义获取课程详情的API接口
     * URL: /courses/detail/{id}  例如: /courses/detail/21
     * @param id 从URL路径中获取的课程ID
     * @return 封装在R对象中的、包含完整课程信息的JSON
     */
    @RequestMapping("/detail/{id}")
    public R getCourseDetails(@PathVariable("id") Long id) {
        // 调用Service层的方法来获取数据
        CourseEntity courseDetails = courseService.getCourseWithDetails(id);

        // 进行简单的判断
        if (courseDetails == null) {
            return R.error("课程不存在");
        }

        // 使用R.ok()方法，将成功获取的数据放入"data"字段，并返回
        return R.ok().put("data", courseDetails);
    }

    // 你可以在这里继续添加其他课程相关的API，比如课程列表、搜索等
}