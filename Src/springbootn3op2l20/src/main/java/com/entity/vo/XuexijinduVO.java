package com.entity.vo;

import com.entity.XuexijinduEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
 

/**
 * 学习进度
 * @author 
 * @email 
 * @date 2024-03-05 18:16:19
 */
public class XuexijinduVO  implements Serializable {
	private static final long serialVersionUID = 1L;

	 			
	/**
	 * 课程类型
	 */
	
	private String kechengleixing;
		
	/**
	 * 课程图片
	 */
	
	private String kechengtupian;
		
	/**
	 * 学习时长
	 */
	
	private Integer xuexishizhang;
		
	/**
	 * 学习进度
	 */
	
	private String xuexijindu;
		
	/**
	 * 学习效果
	 */
	
	private String xuexixiaoguo;
		
	/**
	 * 已完成内容
	 */
	
	private String yiwanchengneirong;
		
	/**
	 * 未完成内容
	 */
	
	private String weiwanchengneirong;
		
	/**
	 * 用户账号
	 */
	
	private String yonghuzhanghao;
		
	/**
	 * 用户姓名
	 */
	
	private String yonghuxingming;
				
	
	/**
	 * 设置：课程类型
	 */
	 
	public void setKechengleixing(String kechengleixing) {
		this.kechengleixing = kechengleixing;
	}
	
	/**
	 * 获取：课程类型
	 */
	public String getKechengleixing() {
		return kechengleixing;
	}
				
	
	/**
	 * 设置：课程图片
	 */
	 
	public void setKechengtupian(String kechengtupian) {
		this.kechengtupian = kechengtupian;
	}
	
	/**
	 * 获取：课程图片
	 */
	public String getKechengtupian() {
		return kechengtupian;
	}
				
	
	/**
	 * 设置：学习时长
	 */
	 
	public void setXuexishizhang(Integer xuexishizhang) {
		this.xuexishizhang = xuexishizhang;
	}
	
	/**
	 * 获取：学习时长
	 */
	public Integer getXuexishizhang() {
		return xuexishizhang;
	}
				
	
	/**
	 * 设置：学习进度
	 */
	 
	public void setXuexijindu(String xuexijindu) {
		this.xuexijindu = xuexijindu;
	}
	
	/**
	 * 获取：学习进度
	 */
	public String getXuexijindu() {
		return xuexijindu;
	}
				
	
	/**
	 * 设置：学习效果
	 */
	 
	public void setXuexixiaoguo(String xuexixiaoguo) {
		this.xuexixiaoguo = xuexixiaoguo;
	}
	
	/**
	 * 获取：学习效果
	 */
	public String getXuexixiaoguo() {
		return xuexixiaoguo;
	}
				
	
	/**
	 * 设置：已完成内容
	 */
	 
	public void setYiwanchengneirong(String yiwanchengneirong) {
		this.yiwanchengneirong = yiwanchengneirong;
	}
	
	/**
	 * 获取：已完成内容
	 */
	public String getYiwanchengneirong() {
		return yiwanchengneirong;
	}
				
	
	/**
	 * 设置：未完成内容
	 */
	 
	public void setWeiwanchengneirong(String weiwanchengneirong) {
		this.weiwanchengneirong = weiwanchengneirong;
	}
	
	/**
	 * 获取：未完成内容
	 */
	public String getWeiwanchengneirong() {
		return weiwanchengneirong;
	}
				
	
	/**
	 * 设置：用户账号
	 */
	 
	public void setYonghuzhanghao(String yonghuzhanghao) {
		this.yonghuzhanghao = yonghuzhanghao;
	}
	
	/**
	 * 获取：用户账号
	 */
	public String getYonghuzhanghao() {
		return yonghuzhanghao;
	}
				
	
	/**
	 * 设置：用户姓名
	 */
	 
	public void setYonghuxingming(String yonghuxingming) {
		this.yonghuxingming = yonghuxingming;
	}
	
	/**
	 * 获取：用户姓名
	 */
	public String getYonghuxingming() {
		return yonghuxingming;
	}
			
}
