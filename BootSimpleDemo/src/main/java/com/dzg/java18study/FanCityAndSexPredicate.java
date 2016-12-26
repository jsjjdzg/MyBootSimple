package com.dzg.java18study;

import com.dzg.entity.Fan;

public class FanCityAndSexPredicate implements FanPredicate{

	@Override
	public boolean test(Fan f) {
		Runnable r = () -> {System.out.println("Hello World");};
		r.run();
		return "苏州".equals(f.getCity()) && "1".equals(f.getSex());
	}

}
