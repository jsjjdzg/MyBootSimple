package com.dzg.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bocloud.emaas.common.model.Param;
import com.bocloud.emaas.common.model.Sign;
import com.bocloud.emaas.database.core.intf.impl.JdbcGenericDao;
import com.bocloud.emaas.database.utils.SQLHelper;
import com.dzg.dao.FanDao;
import com.dzg.entity.Fan;

@SuppressWarnings("rawtypes")
@Repository
public class FanDaoImpl extends JdbcGenericDao implements FanDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Fan> getFans() throws Throwable {
		String sql = "SELECT id,city,sex,province,subscribe_time FROM weixin_fan f WHERE 1=1 ";
		List<Param> params = new ArrayList<Param>();
		Param p1 = new Param();
		
		p1.setSign(Sign.EQ);
		Map<String,Object> paramP = new HashMap<String,Object>();
		paramP.put("city", "苏州");
		p1.setParam(paramP);
		params.add(p1);
		
		Map<String,String> sorter = new HashMap<String,String>();
		sorter.put("subscribe_time", "");//这里排序 写排序的字段
		sql = SQLHelper.buildSql(sql, 1, 100, params, sorter, "f");
		Map<String,Object> paramMap = SQLHelper.getParam(params);
		System.err.println(sql);
		return this.list(Fan.class, sql,paramMap);
	}

	
}
