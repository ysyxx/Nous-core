package com.entity.view;

import com.entity.ChapterEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;


/**
 * 课程章节
 * 后端返回视图实体辅助类
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author
 * @email
 * @date 2025-06-28
 */
@TableName("chapters")
public class ChapterView  extends ChapterEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    public ChapterView(){
    }

    public ChapterView(ChapterEntity chapterEntity){
        try {
            BeanUtils.copyProperties(this, chapterEntity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}