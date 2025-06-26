package com.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("chapters")
public class ChapterEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long courseId; // 关联的课程ID
    private String title; // 章节标题
    private Integer chapterOrder; // 章节顺序

    // 同样，这个字段用于存放关联的课时列表
    @TableField(exist = false)
    private List<LessonEntity> lessons;
}