package com.bocloud.emaas.common.utils;

import java.util.ArrayList;
import java.util.List;

public class EnityTransfer<N, O> {
	@SuppressWarnings("unchecked")
	public List<N> oldToNew(List<O> meta) {
		List<N> list = new ArrayList<>();
		for (O p : meta) {
			list.add((N) p);
		}
		return list;
	}
}
