package provider;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.data.domain.Pageable;

//import com.unind.base.dbconnection.enumeration.DataBaseTypeEnum;

/**
 * @version 1.0
 * @author tanxiang
 * @description 鏌ヨ甯姪绫�
 */
public class QueryHelper {

	protected static Pattern addCountPattern = Pattern.compile("select(.*?)from(.*?)", Pattern.CASE_INSENSITIVE);
	// protected static Pattern removeOrderPattern = Pattern.compile("order
	// by.*?(asc|desc)", Pattern.CASE_INSENSITIVE);
	// 鍚敤璐┆妯″紡
	protected static Pattern removeOrderPattern = Pattern.compile("order by.*(asc|desc)", Pattern.CASE_INSENSITIVE);

	protected static Pattern removeFetchPattern = Pattern.compile(".*?(left|right).*?join.*(fetch).*?", Pattern.CASE_INSENSITIVE);

	protected static final String REGEX_REPLACE_STRING = "[$^*()+|{.?";

	/**
	 * HQL鏌ヨ鏉′欢璁剧疆鍙傛暟
	 * 
	 * @param query
	 * @param parameterList
	 */
	public static void setQueryParameterList(Query query, SQLParameter<Parameter> parameterList) {
		if (null == parameterList) {
			return;
		}
		for (Parameter parameter : parameterList.toList()) {
			setQueryParameter(query, parameter);
		}
	}

	/**
	 * HQL鏌ヨ鏉′欢璁剧疆鍙傛暟
	 * 
	 * @param query
	 * @param parameterList
	 */
	@SuppressWarnings("rawtypes")
	public static void setQueryParameter(Query query, Parameter parameter) {
		if (parameter.getValue() instanceof Object[]) {
			query.setParameterList(parameter.getName(), (Object[]) parameter.getValue());
		} else if (parameter.getValue() instanceof Collection) {
			query.setParameterList(parameter.getName(), (Collection) parameter.getValue());
		} else {
			query.setParameter(parameter.getName(), parameter.getValue());
		}
	}

	/**
	 * SQL鏌ヨ鏉′欢璁剧疆鍙傛暟
	 * 
	 * @param query
	 * @param parameterList
	 */
	public static void setSQLQueryParameterList(SQLQuery query, SQLParameter<Parameter> parameterList) {
		if (null == parameterList) {
			return;
		}
		String[] arr = query.getNamedParameters();
		for (Parameter parameter : parameterList.toList()) {
			if (ArrayUtils.contains(arr, parameter.getName())) {
				setSQLQueryParameter(query, parameter);
			}
		}
	}

	/**
	 * SQL鏌ヨ鏉′欢璁剧疆鍙傛暟
	 * 
	 * @param query
	 * @param parameterList
	 */
	@SuppressWarnings("rawtypes")
	public static void setSQLQueryParameter(SQLQuery query, Parameter parameter) {
		if (parameter.getValue() instanceof Object[]) {
			query.setParameterList(parameter.getName(), (Object[]) parameter.getValue());
		} else if (parameter.getValue() instanceof Collection) {
			query.setParameterList(parameter.getName(), (Collection) parameter.getValue());
		} else {
			query.setParameter(parameter.getName(), parameter.getValue());
		}
	}

	/**
	 * 鏌ヨ璁板綍hql
	 * 
	 * @param hql
	 * @return
	 */
	public static String getCountHQL(String dataBaseType, String hql) {
		if (StringUtils.equals(DataBaseTypeEnum.MICROSOFT_SQL_SERVER.stringValue(), dataBaseType)) {
			// sql server
			return findAndReplace(hql);
		} else if (StringUtils.equals(DataBaseTypeEnum.MYSQL.stringValue(), dataBaseType)) {
			return findAndReplace(hql);
		} else if (StringUtils.equals(DataBaseTypeEnum.ORACLE.stringValue(), dataBaseType)) {
			Matcher matcher = addCountPattern.matcher(hql);
			if (matcher.matches() && matcher.groupCount() > 1) {
				String matcherStr = matcher.group(1);
				if(StringUtils.contains(matcherStr, "(") || StringUtils.contains(matcherStr, ")")) {
					matcherStr = matcher.group(1).replace("(", "\\(").replace(")", "\\)");
				}
				hql = hql.replaceFirst(matcherStr, " count(*) ");
			}
			matcher = removeFetchPattern.matcher(hql);
			if (matcher.matches() && matcher.groupCount() > 0) {
				hql =  hql.replaceAll(matcher.group(2), "");
			}
			
			matcher = removeOrderPattern.matcher(hql);
			if (matcher.matches() && matcher.groupCount() > 0) {
				return matcher.group(1);
			}
		} else {
			Matcher matcher = addCountPattern.matcher(hql);
			if (matcher.matches() && matcher.groupCount() > 1) {
				hql = hql.replaceFirst(matcher.group(1), " count(*) ");
			}
			matcher = removeOrderPattern.matcher(hql);
			if (matcher.matches() && matcher.groupCount() > 0) {
				return matcher.group(1);
			}
		}
		return hql;
	}

	public static String findAndReplace(String str) {
		Matcher matcher = addCountPattern.matcher(str.trim());
		if (matcher.matches() && matcher.groupCount() > 0) {
			if (StringUtils.equals(matcher.group(1).trim(), "*")) {
				str = str.replaceFirst("\\*", " count(*) ");
			} else {
				str = str.replaceFirst(exceptSpecialChar(matcher.group(1)), " count(*) ");
			}
		} else {
			str = "select count(*) " + str;
		}
		matcher = removeOrderPattern.matcher(str);
		while (matcher.find()) {
			str = str.replace(matcher.group(), "");
		}
		return str;
	}

	public static String generateSqlServer(String src) {
		StringBuffer result = new StringBuffer();
		Matcher matcher = removeOrderPattern.matcher(src);
		while (matcher.find()) {
			if (matcher.hitEnd()) {

			}
		}

		return result.toString();
	}

	public static String exceptSpecialChar(String regex) {
		// [$^*()+|{[.?*+|{ //[$^*()+|{.?
		String charStr = null;
		for (int i = 0; i < REGEX_REPLACE_STRING.length(); i++) {
			charStr = String.valueOf(REGEX_REPLACE_STRING.charAt(i));
			if (regex.contains(charStr)) {
				regex = regex.replace(charStr, "\\" + charStr);
			}
		}
		return regex;
	}

	/**
	 * 鏍规嵁鏁版嵁搴撶被鍨嬭幏鍙栧垎椤祍ql
	 * 
	 * @param dataBaseType
	 * @param sql
	 * @return
	 */
	public static String getCountSQL(String dataBaseType, String sql) {
		if (StringUtils.equals(DataBaseTypeEnum.MICROSOFT_SQL_SERVER.stringValue(), dataBaseType)) {
			// sql server
			String countSQL = sql;
			Matcher matcher = removeOrderPattern.matcher(sql);
			if (matcher.find()) {
				countSQL = sql.replace(matcher.group(), "");
			}
			StringBuilder sqlSB = new StringBuilder();
			sqlSB.append("select count(*) from (").append(countSQL).append(") t");
			return sqlSB.toString();
		} else if (StringUtils.equals(DataBaseTypeEnum.MYSQL.stringValue(), dataBaseType)) {
			String countSQL = sql;
			Matcher matcher = removeOrderPattern.matcher(sql);
			if (matcher.find()) {
				countSQL = sql.replace(matcher.group(), "");
			}
			StringBuilder sqlSB = new StringBuilder();
			sqlSB.append("select count(*) from (").append(countSQL).append(") t");
			return sqlSB.toString();
		} else if (StringUtils.equals(DataBaseTypeEnum.ORACLE.stringValue(), dataBaseType)) {
			String countSQL = sql;
			Matcher matcher = removeOrderPattern.matcher(sql);
			if (matcher.find()) {
				countSQL = sql.replace(matcher.group(), "");
			}
			StringBuilder sqlSB = new StringBuilder();
			sqlSB.append("select count(*) from (").append(countSQL).append(") t");
			return sqlSB.toString();
		} else {
			String countSQL = sql;
			Matcher matcher = removeOrderPattern.matcher(sql);
			if (matcher.find()) {
				countSQL = sql.replace(matcher.group(), "");
			}
			StringBuilder sqlSB = new StringBuilder();
			sqlSB.append("select count(*) from (").append(countSQL).append(")");
			return sqlSB.toString();
		}
	}

	/**
	 * 鏍规嵁鏁版嵁搴撶被鍨嬭幏鍙栧垎椤祍ql
	 * 
	 * @param dataBaseType
	 * @param sql
	 * @return
	 */
	public static String getPaginationSQL(String dataBaseType, String sql, String orderBy, Pageable pageable) {
		if (StringUtils.equals(DataBaseTypeEnum.MICROSOFT_SQL_SERVER.stringValue(), dataBaseType)) {
			// sql server
			Matcher matcher = removeOrderPattern.matcher(sql);
			if (matcher.find()) {
				if (StringUtils.isEmpty(orderBy)) {
					orderBy = matcher.group();
				}
				sql = sql.replace(matcher.group(), "");
			}
			StringBuilder sqlSB = new StringBuilder();
			/*
			 * sqlSB.append("select * from (" + sql + ") t where t.id in (")
			 * .append("select top " + paginator.getRows() + " id from (")
			 * .append("select top " + (paginator.getPage() * paginator.getRows()) +
			 * " s.id from (" + sql + ") s order by s.id desc")
			 * .append(") t order by t.id asc") .append(") order by t.id desc");
			 */
			sqlSB.append(" select top " + pageable.getPageSize() + " * from ")
					.append(" (select row_number() over ("
							+ (StringUtils.isNotEmpty(orderBy) ? orderBy : "order by p.id desc") + ") as rownumber, p.* ")
					.append(" from (" + sql + ") p ) a ")
					.append(" where a.rownumber > " + ((pageable.getPageNumber() - 1) * pageable.getPageSize()));
			if (StringUtils.isNotEmpty(orderBy)) {
				sqlSB.append(" " + orderBy);
			}
			return sqlSB.toString();
		} else if (StringUtils.equals(DataBaseTypeEnum.MYSQL.stringValue(), dataBaseType)) {
			String tempSQL = sql;
			Matcher matcher = removeOrderPattern.matcher(sql);
			if (matcher.matches() && matcher.groupCount() > 0) {
				tempSQL = matcher.group(1);
			}
			StringBuilder sqlSB = new StringBuilder();
			sqlSB.append(tempSQL).append(orderBy)
					.append(" limit " + ((pageable.getPageNumber() - 1) * pageable.getPageSize()) + ", " + pageable.getPageSize());
			return sqlSB.toString();
		} else if (StringUtils.equals(DataBaseTypeEnum.ORACLE.stringValue(), dataBaseType)) {
			String countSQL = sql;
			Matcher matcher = removeOrderPattern.matcher(sql);
			if (matcher.matches() && matcher.groupCount() > 0) {
				countSQL = matcher.group(1);
			}
			StringBuilder sqlSB = new StringBuilder();
			sqlSB.append("select count(*) from (").append(countSQL).append(") t");
			return sqlSB.toString();
		} else {
			String countSQL = sql;
			Matcher matcher = removeOrderPattern.matcher(sql);
			if (matcher.matches() && matcher.groupCount() > 0) {
				countSQL = matcher.group(1);
			}
			StringBuilder sqlSB = new StringBuilder();
			sqlSB.append("select count(*) from (").append(countSQL).append(")");
			return sqlSB.toString();
		}
	}

	public static void main(String[] args) {
		String hql = " SELECT G.gender,COUNT(G.id)AS COUNT FROM TX_USER G GROUP BY G.gender order by g.gender asc";

		// generateSqlServer(hql);
		Matcher matcher = addCountPattern.matcher(hql);

		if (matcher.matches() && matcher.groupCount() > 0) {
			System.out.println(matcher.group(1));
			// System.out.println(hql.replace(matcher.group(1), " count(*) "));
			if (StringUtils.equals(matcher.group(1).trim(), "*")) {
				hql = hql.replaceFirst("\\*", " count(*) ");
			} else {
				hql = hql.replaceFirst(exceptSpecialChar(matcher.group(1)), " count(*) ");
			}
		} else {
			hql = "select count(*) " + hql;
		}
		matcher = removeOrderPattern.matcher(hql);
		while (matcher.find()) {
			System.out.println(matcher.group());
			hql = hql.replace(matcher.group(), "");
		}
		System.out.println(hql);
	}

}