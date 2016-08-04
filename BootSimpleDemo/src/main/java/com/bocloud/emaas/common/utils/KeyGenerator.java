package com.bocloud.emaas.common.utils;

import java.util.UUID;

/**
 * 主键生成器
 * 
 * @author dmw
 *
 */
public class KeyGenerator {

	public static synchronized String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
