package com.bocloud.emaas.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bocloud.emaas.common.model.Param;
import com.bocloud.emaas.common.model.Sign;

public class ParamHelper {

	public static List<Param> getParams(Map<String,Object> map,Sign sign){
		List<Param> list = new ArrayList<Param>();
		Param param = new Param(map,sign);
		list.add(param);
		return list;
	}
}
