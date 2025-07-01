package com.entity.vo;

import com.entity.ChapterEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;


/**
 * 课程章节
 * 前端返回视图实体辅助类
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author
 * @email
 * @date 2025-06-28
 */
@TableName("chapters")
public class ChapterVO  extends ChapterEntity implements Serializable {
    private static final long serialVersionUID = 1L;

}