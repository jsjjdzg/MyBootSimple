package com.bocloud.emaas.database.core.meta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConditionDef {

	private Map<String, String> paraNameAndSubConditionClauseMap = new LinkedHashMap<String, String>();
	private Map<String, Class<?>> paraNameAndClassTypeMap = new HashMap<String, Class<?>>();
	private Map<String, List<Character>> paraNameAndLikeMatchInfoMap = new HashMap<String, List<Character>>();

	@SuppressWarnings("unused")
	public ConditionDef(Object[][] defineArr) {
		for (Object[] subDefine : defineArr) {

			Pattern pattern = Pattern.compile(":([\\w\\d_]+)");
			String currDefClause = (String) subDefine[0];
			int currDefClauseLen = currDefClause.length();
			Matcher matcher = pattern.matcher(currDefClause);
			Set<String> varNameSet = new HashSet<String>();
			int varNameCount = 0;

			char clauseMode = Condition.STANDARD_MODE;
			String oneVarName = null;
			boolean isUsedSameMatchMode = true;
			List<Character> matchModeList = new ArrayList<Character>();
			while (matcher.find()) {
				String varName = matcher.group(1);

				oneVarName = varName;
				int start = matcher.start();
				int end = matcher.end();
				char prefix = currDefClause.charAt(start - 1);

				char suffix = end >= currDefClauseLen ? ' ' : currDefClause.charAt(end);
				char subConditionMatchMode = Condition.STANDARD_MODE;
				if (prefix == '%' && suffix == '%') {
					clauseMode = subConditionMatchMode = Condition.GLOBAL_MATCH;
					matchModeList.add(clauseMode);

				} else if (prefix == '%') {
					clauseMode = subConditionMatchMode = Condition.PREFIX_MATCH;
					matchModeList.add(clauseMode);
				} else if (suffix == '%') {
					clauseMode = subConditionMatchMode = Condition.SUFFIX_MATCH;
					matchModeList.add(clauseMode);
				}

				varNameSet.add(varName);
				varNameCount++;
				if (varNameCount > 1 && matchModeList.size() >= 2) {
					int size = matchModeList.size();
					if (!matchModeList.get(size - 1).equals(matchModeList.get(size - 2))) {
						isUsedSameMatchMode = false;
					}
				}
			}

			if (varNameSet.size() != 1) {
				throw new RuntimeException("One sub condition clause must only have one var name ! clause :"
						+ currDefClause);
			}
			if (oneVarName == null) {
				throw new RuntimeException("Sub condition is not have any var name ! clause :" + currDefClause);
			}

			if (subDefine.length > 1) {
				paraNameAndClassTypeMap.put(oneVarName, (Class<?>) subDefine[1]);
			}
			if (clauseMode != Condition.STANDARD_MODE) {
				if (isUsedSameMatchMode && varNameCount == matchModeList.size()) {

					paraNameAndLikeMatchInfoMap.put(oneVarName, matchModeList.subList(0, 1));
				} else {
					currDefClause = currDefClause
							.replaceAll("%:" + oneVarName + "%", ":" + oneVarName + "_globalMatch");
					currDefClause = currDefClause.replaceAll("%:" + oneVarName, ":" + oneVarName + "_suffixMatch");
					currDefClause = currDefClause.replaceAll(":" + oneVarName + "%", ":" + oneVarName + "_prefixMatch");
					paraNameAndLikeMatchInfoMap.put(oneVarName, matchModeList);
				}
				currDefClause = currDefClause.replaceAll("'\\%", "");
				currDefClause = currDefClause.replaceAll("\\%'", "");
				currDefClause = currDefClause.replaceAll("\\%", "");
				currDefClause = currDefClause.replaceAll("'", "");
			}
			String tempClause = currDefClause.toUpperCase();
			if (tempClause.indexOf("AND") != -1 || tempClause.indexOf("OR") != -1) {
				currDefClause = "(" + currDefClause + ")";
			}
			paraNameAndSubConditionClauseMap.put(oneVarName, currDefClause);

		}

	}

	public String[] getConditionVarNames() {
		return paraNameAndSubConditionClauseMap.keySet().toArray(
				new String[paraNameAndSubConditionClauseMap.keySet().size()]);
	}

	public String getSubConditionClause(String varName) {
		return paraNameAndSubConditionClauseMap.get(varName);
	}

	public boolean isExistClassTypeInfo(String varName) {
		return paraNameAndClassTypeMap.containsKey(varName);
	}

	public Class<?> getClassTypeInfo(String varName) {
		return paraNameAndClassTypeMap.get(varName);
	}

	public boolean isExistMatchModeInfo(String varName) {
		return paraNameAndLikeMatchInfoMap.containsKey(varName);
	}

	public List<Character> getMatchModeInfo(String varName) {
		return paraNameAndLikeMatchInfoMap.get(varName);
	}

	@Override
	public String toString() {
		return "ConditionDef [paraNameAndSubConditionClauseMap=" + paraNameAndSubConditionClauseMap
				+ ", paraNameAndClassTypeMap=" + paraNameAndClassTypeMap + ", paraNameAndLikeMatchInfoMap="
				+ paraNameAndLikeMatchInfoMap + "]";
	}
	
}
