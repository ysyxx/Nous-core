package com.entity.vo;

import com.entity.LessonEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;
import java.util.Date;

/**
 * 课程节值对象/数据传输对象
 * （通常用于数据传输，例如前端提交数据或后端返回数据）
 * @author
 * @email
 * @date 2025-06-26 14:00:00 (示例日期，请根据实际生成时间调整)
 */
@Data
public class LessonVO implements Serializable {
    private static final long serialVersionUID = 1L;

    // 对应 LessonEntity 的所有字段
    private Long id;
    private Long chapterOrder;
    private String title;
    private Integer lessonOrder;
    private String lessonType;
    private String videoUrl;
    private Integer durationSeconds;
    private String contentForAi;
    private String pdfUrl;

}
