package com.entity.view;

import com.entity.SyslogEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import java.io.Serializable;
import com.utils.EncryptUtil;
 

/**
 * 系统日志
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author 
 * @email 
 * @date 2024-03-05 18:16:19
 */
@TableName("syslog")
public class SyslogView  extends SyslogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public SyslogView(){
	}
 
 	public SyslogView(SyslogEntity syslogEntity){
 	try {
			BeanUtils.copyProperties(this, syslogEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}


}
