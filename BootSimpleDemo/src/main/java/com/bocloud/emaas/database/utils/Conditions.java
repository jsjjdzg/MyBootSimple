package com.bocloud.emaas.database.utils;

import java.util.Map;
import java.util.Set;

import com.bocloud.emaas.database.core.meta.ConditionDef;

/**常用查询条件
 * @author mingwei.dmw
 *
 */
public class Conditions {
	
	public static ConditionDef simpleCondition(String pref,String paramName){
		return new ConditionDef(new Object[][]{{pref+" = :"+paramName}});
	}
	
	public static ConditionDef complexCondition(Map<String,String> conditionMap) throws Exception{
		if(null == conditionMap||conditionMap.isEmpty()){
			throw new Exception("Empty condition Map!!!");
		}
		Object[][] defineArr = new Object[conditionMap.keySet().size()][];
		Set<String> keys = conditionMap.keySet();
		int i=0;
		for(String key : keys){
			Object[] item = {key+" = :"+conditionMap.get(key)};
			defineArr[i]=item;
			i++;
		}
		ConditionDef conditionDef = new ConditionDef(defineArr);
		return conditionDef;
	}
	
}
