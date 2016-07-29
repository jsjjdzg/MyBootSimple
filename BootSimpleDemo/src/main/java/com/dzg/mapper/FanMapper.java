package com.dzg.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.dzg.entity.Fan;

/**
 * 
 * Fan测试Mapper接口
 * 
 * @author DZG
 * @since V1.0 2016年7月29日
 */
public interface FanMapper {

	List<Fan> getFans();
	
	@Select("SELECT * FROM weixin_fan")
	List<Fan> getFans2();
	
	@Select("SELECT * FROM weixin_fan WHERE id = #{id}")
	Fan getById(int id);

    String getNameById(int id);
}
