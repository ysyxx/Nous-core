package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("lessons")
public class LessonEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long chapterOrder;
    private String title; // 课时标题
    private Integer lessonOrder; // 课时顺序
    private Integer courseId; // 课程ID
    private String lessonType; // 课时类型: 'video', 'article'
    private String videoUrl; // 视频地址
    private Integer durationSeconds; // 时长（秒）
    private String contentForAi; // 用于AI问答的文本内容
    private String pdfUrl;
}
