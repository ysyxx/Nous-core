package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.enums.IdType;


/**
 * 课程类型
 * 数据库通用操作实体类（普通增删改查）
 * @author
 * @email
 * @date 2024-03-05 18:16:19
 */
@Data
@TableName("lessontype")
public class CourseTypeEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


    public CourseTypeEntity() {

    }

    public CourseTypeEntity(T t) {
        try {
            BeanUtils.copyProperties(this, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 课程类型
     */

    private String courseType;


    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;


}
