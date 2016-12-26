package com.dzg.java18study;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dzg.entity.Fan;

@Service("fan18Service")
public class Fan18Service {
	
	//行为参数化第一次代码
	public List<Fan> filterFans(List<Fan> fansOld,FanCityAndSexPredicate fanTest){
		List<Fan> fansNew  = new ArrayList<Fan>();
		for(Fan f : fansOld){
			if(fanTest.test(f))
				fansNew.add(f);
		}
		return fansNew;
	}
	
}
