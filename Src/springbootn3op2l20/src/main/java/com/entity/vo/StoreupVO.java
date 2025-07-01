package com.entity.vo;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.entity.StoreupEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;


/**
 * 收藏表
 * @author
 * @email
 * @date 2024-03-05 18:16:19
 */
public class StoreupVO  implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long userId;

	private Long courseId;


	private int thumbs_up;


	private int thumbs_down;


	private int favorite;

}
