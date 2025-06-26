package com.entity.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.entity.ChapterEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("courses") // 对应新的 `courses` 表
public class CourseVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title; // 课程标题
    private String description; // 课程介绍
    private String coverImageUrl; // 封面图片
    private String kechengleixing; // 课程类型 (保留旧字段或改造为type_id)
    private Date addtime;

    // 这个字段不存在于数据库表中，但用于在查询后存放关联的章节列表
    @TableField(exist = false)
    private List<ChapterEntity> chapters;
}
