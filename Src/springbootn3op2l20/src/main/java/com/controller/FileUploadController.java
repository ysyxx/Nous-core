package com.controller;

// 导入我们新的实体类和服务
import com.entity.CourseEntity;
import com.entity.LessonEntity;
import com.service.CourseService;
import com.service.LessonService;
// 导入其他必要的类
import com.utils.OBSUtil;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    @Autowired
    private OBSUtil obsUtil;

    @Autowired
    private CourseService courseService;

    @Autowired
    private LessonService lessonService;

    /**
     * 为指定的【课时】上传视频文件
     * @param lessonId 课时的ID，通过URL路径传入
     * @param file 视频文件
     * @return 包含视频URL的响应
     */
    @PostMapping("/lesson/video/{lessonId}")
    public R uploadLessonVideo(@PathVariable Long lessonId, @RequestParam("file") MultipartFile file) {
        // 【修正点】使用 .selectById()
        LessonEntity lesson = lessonService.selectById(lessonId);
        if (lesson == null) {
            return R.error("ID为 " + lessonId + " 的课时不存在。");
        }
        if (!"video".equals(lesson.getLessonType())) {
            return R.error("该课时不是视频类型，无法上传视频。");
        }

        try {
            String fileName = "lessons/videos/" + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            String videoUrl = obsUtil.uploadFile(fileName, file.getInputStream());

            lesson.setVideoUrl(videoUrl);
            lessonService.updateById(lesson);

            return R.ok("视频上传成功").put("url", videoUrl);

        } catch (IOException e) {
            return R.error("视频文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 为指定的【课程】上传封面图片
     * @param courseId 课程的ID，通过URL路径传入
     * @param file 图片文件
     * @return 包含图片URL的响应
     */
    @PostMapping("/course/cover/{courseId}")
    public R uploadCourseCoverImage(@PathVariable Long courseId, @RequestParam("file") MultipartFile file) {
        // 【修正点】使用 .selectById()
        CourseEntity course = courseService.selectById(courseId);
        if (course == null) {
            return R.error("ID为 " + courseId + " 的课程不存在。");
        }

        try {
            String fileName = "courses/covers/" + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            String imageUrl = obsUtil.uploadFile(fileName, file.getInputStream());

            course.setCoverImageUrl(imageUrl);
            courseService.updateById(course);

            return R.ok("课程封面上传成功").put("url", imageUrl);

        } catch (IOException e) {
            return R.error("图片文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 【示例】为指定的【文章课时】上传PDF文件作为其内容
     */
    @PostMapping("/lesson/pdf/{lessonId}")
    public R uploadLessonPdf(@PathVariable Long lessonId, @RequestParam("file") MultipartFile file) {
        // 【修正点】使用 .selectById()
        LessonEntity lesson = lessonService.selectById(lessonId);
        if (lesson == null) {
            return R.error("ID为 " + lessonId + " 的课时不存在。");
        }
        if (!"article".equals(lesson.getLessonType())) {
            return R.error("该课时不是文章类型，无法上传PDF。");
        }

        try {
            String fileName = "lessons/pdfs/" + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
            String pdfUrl = obsUtil.uploadFile(fileName, file.getInputStream());

            // 假设 LessonEntity 中有一个 attachmentUrl 字段用于存放附件地址
            // lesson.setAttachmentUrl(pdfUrl);
            // 同时，你可能需要一个后台任务来读取这个PDF，提取文本，然后更新到 lesson.getContentForAi() 字段
            lessonService.updateById(lesson);

            return R.ok("PDF附件上传成功").put("url", pdfUrl);

        } catch (IOException e) {
            return R.error("PDF文件上传失败: " + e.getMessage());
        }
    }
}