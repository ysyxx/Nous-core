package com.entity.view;

import com.entity.LessonEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;

/**
 * 课程节视图实体类
 * （通常用于展示层，可以包含关联查询的字段）
 * @author
 * @email
 * @date 2025-06-26 14:00:00 (示例日期，请根据实际生成时间调整)
 */
@TableName("lessons") // 视图实体也通常会关联到原表名
public class LessonView extends LessonEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public LessonView(){
    }

    public LessonView(LessonEntity lessonEntity) {
        try {
            BeanUtils.copyProperties(this, lessonEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 如果你需要添加来自关联表的字段，例如章节名称、课程名称，可以在这里添加
    // private String chapterTitle;
    // private String courseTitle;

    // 对应的getter和setter方法
    // public String getChapterTitle() { return chapterTitle; }
    // public void setChapterTitle(String chapterTitle) { this.chapterTitle = chapterTitle; }
    // public String getCourseTitle() { return courseTitle; }
    // public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }
}
