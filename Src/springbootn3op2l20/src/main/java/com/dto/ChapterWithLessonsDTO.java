// src/main/java/com/dto/ChapterWithLessonsDTO.java
package com.dto;

import com.entity.ChapterEntity; // 假设你的 Chapter 表对应的实体类是 ChapterEntity
import com.entity.LessonEntity;   // 假设你的 Lessons 表对应的实体类是 LessonEntity
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 章节及其包含的课时列表的 DTO
 */
@EqualsAndHashCode(callSuper = true) // 如果要继承 ChapterEntity，需要此注解处理 equals/hashCode
@Data
public class ChapterWithLessonsDTO extends ChapterEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    // 当前章节下的所有课时列表
    private List<LessonEntity> lessons;
}
