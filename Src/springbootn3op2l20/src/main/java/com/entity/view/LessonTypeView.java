package com.entity.view;

import com.baomidou.mybatisplus.annotations.TableName;
import com.entity.LessonTypeEntity;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;


/**
 * 课程类型
 * 后端返回视图实体辅助类
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author
 * @email
 * @date 2024-03-05 18:16:19
 */
@TableName("lesson_type")
public class LessonTypeView  extends LessonTypeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public LessonTypeView(){
    }

    public LessonTypeView(LessonTypeEntity lessonTypeEntity){
        try {
            BeanUtils.copyProperties(this, lessonTypeEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
