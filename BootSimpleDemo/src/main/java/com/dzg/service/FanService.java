package com.dzg.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.dzg.entity.Fan;

/**
 * 
 * FanService测试JDBC类
 * 
 * @author DZG
 * @since V1.0 2016年7月29日
 */
@Service("fanService")
public class FanService {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Fan> getFans(){
		String sql = "SELECT id,nickname,city,sex,province FROM weixin_fan limit 1,10";
		return jdbcTemplate.query(sql, new RowMapper<Fan>(){

			@Override
			public Fan mapRow(ResultSet rs, int rowNum) throws SQLException {
				Fan fan = new Fan();
				fan.setId(rs.getString("id"));
				fan.setSex(rs.getString("sex"));
				fan.setCity(rs.getString("city"));
				fan.setProvince(rs.getString("province"));
				return fan;
			}
			
		});
	}
}
