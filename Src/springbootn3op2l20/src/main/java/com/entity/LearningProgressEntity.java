package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("learning_progress")
public class LearningProgressEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private Long lessonId; // 关联到最细的课时ID

    private Boolean completed;
    private Integer lastPosition;

    private Integer progressSeconds;
    private float progressPercent;

    // 注意：这些字段不在 learning_progress 表中，但会在联表查询时填充
    private Integer lessonOrder; // 对应 lessons 表的 lesson_order
    private Long chapterId;     // 对应 lessons 表的 chapter_id
}
