package com.bocloud.emaas.common.utils;

import java.util.List;

import com.bocloud.emaas.common.model.GridBean;

public class GridHelper {

	public static GridBean getBean(int page, int rows, int total, List<?> list) {
		int pages = 0;
		if (total % rows == 0) {
			pages = total / rows;
		} else {
			pages = total / rows + 1;
		}
		pages = pages < 1 ? 1 : pages;
		return new GridBean(page, pages, total, list);
	}
}
