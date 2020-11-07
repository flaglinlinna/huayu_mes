package provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.utils.Collections3;

/*import com.unind.base.data.Paginator;
import com.unind.base.dbconnection.enumeration.DataBaseTypeEnum;
import com.unind.base.dbconnection.query.Parameter;
import com.unind.base.dbconnection.query.QueryHelper;
import com.unind.base.dbconnection.query.SQLParameter;*/

/**
 * 基础数据库操作业务类
 * <p>封装spring data jpa、spring jdbc等数据源实现分页hql、sql查询功能</p>
 * @author tanxiang
 *
 */
public class BaseOprService  {

	@Autowired
	private EntityManager entityManager;

	public <T> Page<T> findPageByHql(String hql, List<Parameter> params, Pageable pageable) {
		String countHql = QueryHelper.getCountHQL(DataBaseTypeEnum.ORACLE.stringValue(), hql);
		long total = this.countByHql(countHql, params);
		if(total == 0) {
			return new PageImpl<T>(new ArrayList<T>(), pageable, total);
		}
		List<T> list = this.findByHql(hql, params, pageable);
		return new PageImpl<T>(list, pageable, total);
	}

	/**
	 * 查询记录总数
	 *
	 * @param hql
	 * @param params
	 * @return
	 */
	public long countByHql(String hql, List<Parameter> params) {
		Query query = entityManager.createQuery(hql);

		Parameter parameter;
		for (int i = 0; i < params.size(); i++) {
			parameter = params.get(i);
			query.setParameter(parameter.getName(), parameter.getValue());
		}
		return query.getResultList().size();
		//return ((Number) query.getSingleResult()).longValue();
	}

	public Object findOneByHql(String hql, List<Parameter> params) {
		List<?> list = findByHql(hql, params);
		if(null==list || list.isEmpty()) {
			return null;
		}
		return list.iterator().next();
	}

	/**
	 * 查询记录列表
	 *
	 * @param hql
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findByHql(String hql, List<Parameter> params) {
		Query query = entityManager.createQuery(hql);
		if (null == params || params.size() == 0) {
			return query.getResultList();
		}

		Parameter parameter;
		for (int i = 0; i < params.size(); i++) {
			parameter = params.get(i);
			query.setParameter(parameter.getName(), parameter.getValue());
		}
		return query.getResultList();
	}

	/**
	 * 分页查询记录列表
	 *
	 * @param hql		hql查询语句
	 * @param params	绑定变量值
	 * @param pageable	分页参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findByHql(String hql, List<Parameter> params, Pageable pageable) {
		/*String countHql = QueryHelper.getCountHQL(DataBaseTypeEnum.ORACLE.stringValue(), hql);
		long total = this.countByHql(countHql, params);
		if(total == 0 ) {
			return new ArrayList<T>();
		}*/
		Query query = entityManager.createQuery(hql);
		if (null == params || params.size() == 0) {
			query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
			query.setMaxResults(pageable.getPageSize());
			return query.getResultList();
		}

		Parameter parameter;
		for (int i = 0; i < params.size(); i++) {
			parameter = params.get(i);
			query.setParameter(parameter.getName(), parameter.getValue());
		}
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());
		return query.getResultList();
	}

	/**
	 * 原生sql,查询一条
	 *
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> findOneBySql(String sql, Map<String, Object> paramMap) {
		List<Map<String, Object>> list = createSQLQuery(sql, null, paramMap);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 原生sql,查询数据
	 *
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> findBySql(String sql, Map<String, Object> paramMap) {
		return createSQLQuery(sql, null, paramMap);
	}

	/**
	 * 原生sql,查询数据
	 *
	 * @param sql		sql查询语句
	 * @param paramMap	传入参数
	 * @param cls		查询结果要封装的实体类
	 * @return
	 */
	public <T> T findOneBySql(String sql, Map<String, Object> paramMap, Class<T> cls) {
		List<T> list = createSQLQuery(sql, new PageRequest(0, 1), paramMap, cls);
		if(null!=list && !list.isEmpty()) {
			return list.iterator().next();
		}
		return null;
	}

	/**
	 * 原生sql,查询数据
	 *
	 * @param sql		sql查询语句
	 * @param paramMap	传入参数
	 * @param cls		查询结果要封装的实体类
	 * @return
	 */
	public <T> List<T> findBySql(String sql, Map<String, Object> paramMap, Class<T> cls) {
		return createSQLQuery(sql, null, paramMap, cls);
	}

	/**
	 * 原生sql,分页查询数据
	 *
	 * @param sql		sql查询语句
	 * @param pageable	分页参数
	 * @param paramMap	绑定变量参数值
	 * @return
	 */
	public List<Map<String, Object>> findBySql(String sql, Pageable pageable, Map<String, Object> paramMap) {
		return createSQLQuery(sql, pageable, paramMap);
	}

	/**
	 * 原生sql,分页查询数据
	 *
	 * @param sql		sql查询语句
	 * @param pageable	分页参数
	 * @param paramMap	绑定变量参数值
	 * @return
	 */
	public Page<Map<String, Object>> findPageBySql(String sql, Pageable pageable, Map<String, Object> paramMap) {
		return findPageBySql(sql, pageable, paramMap, null);
	}

	/**
	 * 原生sql,分页查询数据
	 *
	 * @param sql		sql查询语句
	 * @param pageable	分页参数
	 * @param paramMap	绑定变量参数值
	 * @param cls		查询结果要封装的实体类
	 * @return
	 */
	public <T> Page<T> findPageBySql(String sql, Pageable pageable, Map<String, Object> paramMap, Class<T> cls) {
		List<T> list = new ArrayList<T>();
		long total = 0L;
		if (pageable != null) {
			total = countBySql(sql, paramMap);
			if (total > 0) {
				list = createSQLQuery(sql, pageable, paramMap, cls);
			}
		} else {
			list = createSQLQuery(sql, pageable, paramMap, cls);
			if (list != null) {
				total = list.size();
			}
		}
		PageImpl<T> page = new PageImpl<T>(list, pageable, total);
		return page;
	}

	/**
	 * 原生sql,查询记录数
	 *
	 * @param sql
	 * @param paramMap
	 * @return
	 */
	public long countBySql(String sql, Map<String, Object> paramMap) {
		StringBuffer sbuf = new StringBuffer("select count(*) total from ( " + sql + " ) t");
		List<Map<String, Object>> dblist = createSQLQuery(sbuf.toString(), null, paramMap);
		long total = 0;
		if (dblist != null && dblist.size() != 0) {
			total = Long.parseLong(dblist.get(0).entrySet().iterator().next().getValue().toString());
		}
		return total;
	}

	/**
	 * 调用sql查询返回map对象
	 *
	 * @param sql		sql查询语句
	 * @param pageable	分页参数
	 * @param params	绑定变量参数值
	 * @return
	 */
	private List<Map<String, Object>> createSQLQuery(String sql, Pageable pageable, Map<String, Object> paramMap) {
		return this.createSQLQuery(sql, pageable, paramMap, null);
	}

	/**
	 * 调用sql查询返回map对象
	 *
	 * @param sql		sql查询语句
	 * @param pageable	分页参数
	 * @param params	绑定变量参数值
	 * @param cls		查询结果要封装的实体类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> List<T> createSQLQuery(String sql, Pageable pageable, Map<String, Object> paramMap, Class<T> cls) {
		Session session = entityManager.unwrap(Session.class);
		SQLQuery query = session.createSQLQuery(sql);

		if (pageable != null) {
			query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
			query.setMaxResults(pageable.getPageSize());
		}

		if (paramMap != null && paramMap.size() > 0) {
			for (String key : paramMap.keySet()) {
				if(paramMap.get(key) instanceof Object[]) {
					query.setParameterList(key, (Object[])paramMap.get(key));
				}else if(paramMap.get(key) instanceof Collection) {
					query.setParameterList(key, (Collection<?>)paramMap.get(key));
				}else {
					query.setParameter(key, paramMap.get(key));
				}
			}
		}
		if(null==cls) {
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		}else {
//			query.addEntity(cls);
			query.setResultTransformer(Transformers.aliasToBean(cls));
		}

		List<T> list = query.list();
//		List<Map<String, Object>> maplist = (List<Map<String, Object>>) list;
		return list;
	}

	/**
	 * 调用sql查询返回map对象
	 *
	 * @param sql			sql查询语句
	 * @param sqlParameter	绑定变量参数值
	 * @return
	 */
	public List<Map<String, Object>> findBySql(String sql, SQLParameter<Parameter> sqlParameter) {
		return this.findBySql(sql, sqlParameter, null);
	}


	/**
	 * 调用sql查询返回指定对象 使用JPA
	 *
	 * @param sql			sql查询语句
	 * @param sqlParameter	绑定变量参数值
	 * @param cls		查询结果要封装的实体类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findBySqlByJpa(String sql, SQLParameter<Parameter> sqlParameter, Class<T> cls) {
		Query query = entityManager.createNativeQuery(sql.toString());
		query.unwrap(SQLQuery.class).setResultTransformer(null==cls?Transformers.ALIAS_TO_ENTITY_MAP : Transformers.aliasToBean(cls));
		if (null!=sqlParameter && sqlParameter.getPageSize() < SQLParameter.DEFAULT_PAGESIZE) {
			query.setFirstResult(sqlParameter.getPage() * sqlParameter.getPageSize());
			query.setMaxResults(sqlParameter.getPageSize());
		}
		if (null!=sqlParameter && sqlParameter.toList().size() > 0) {
			for (Parameter parameter : sqlParameter.toList()) {
				query.setParameter(parameter.getName(), parameter.getValue());
			}
		}
		List<T> result;
	    try {
	        // 执行Query
	        result = query.getResultList();
	    } catch (Exception e) {
	        result = null;
	    }
		return result;
	}

	/**
	 * 调用sql查询返回指定对象
	 *
	 * @param sql			sql查询语句
	 * @param sqlParameter	绑定变量参数值
	 * @param cls		查询结果要封装的实体类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findBySql(String sql, SQLParameter<Parameter> sqlParameter, Class<T> cls) {
		Session session = entityManager.unwrap(Session.class);
		SQLQuery query = session.createSQLQuery(sql);

		if (null!=sqlParameter && sqlParameter.getPageSize() < SQLParameter.DEFAULT_PAGESIZE) {
			query.setFirstResult(sqlParameter.getPage() * sqlParameter.getPageSize());
			query.setMaxResults(sqlParameter.getPageSize());
		}

		if (null!=sqlParameter && sqlParameter.toList().size() > 0) {
			for (Parameter parameter : sqlParameter.toList()) {
				if(parameter.getValue() instanceof Object[]) {
					query.setParameterList(parameter.getName(), (Object[])parameter.getValue());
				}else if(parameter.getValue() instanceof Collection) {
					query.setParameterList(parameter.getName(), (Collection<?>)parameter.getValue());
				}else {
					query.setParameter(parameter.getName(), parameter.getValue());
				}
			}
		}

		if (null==cls) {
			query.setResultTransformer(null==cls?Transformers.ALIAS_TO_ENTITY_MAP : Transformers.aliasToBean(cls));
		}else {
			query.addEntity(cls);
		}

		List<T> list = query.list();
//		List<Map<String, Object>> maplist = (List<Map<String, Object>>) list;
		return list;
	}

	public static <T> Specification<T> and(final Collection<SearchFilter> filters, final Class<T> entityClazz) {
		return bySearchFilter(filters, entityClazz, "and");
	}

	public static <T> Specification<T> or(final Collection<SearchFilter> filters, final Class<T> entityClazz) {
		return bySearchFilter(filters, entityClazz, "or");
	}

	/**
     * 扩展查询条件封装：默认仅支持and连接，添加连接操作字段，可支持and或or
     * @param filters
     * @param entityClazz
     * @param String linkOper "and" / "or"
     * @return
     */
	public static <T> Specification<T> bySearchFilter(final Collection<SearchFilter> filters, final Class<T> entityClazz, final String linkOper) {
		return new Specification<T>() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (Collections3.isNotEmpty(filters)) {

					List<Predicate> predicates = new ArrayList<Predicate>();
					for (SearchFilter filter : filters) {
						// nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
						String[] names = StringUtils.split(filter.fieldName, ".");
						Path expression = root.get(names[0]);
						for (int i = 1; i < names.length; i++) {
							expression = expression.get(names[i]);
						}

						// logic operator
						switch (filter.operator) {
						case EQ:
							predicates.add(builder.equal(expression, filter.value));
							break;
						case LIKE:
							predicates.add(builder.like(expression, "%" + filter.value + "%"));
							break;
						case GT:
							predicates.add(builder.greaterThan(expression, (Comparable) filter.value));
							break;
						case LT:
							predicates.add(builder.lessThan(expression, (Comparable) filter.value));
							break;
						case GTE:
							predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.value));
							break;
						case LTE:
							predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.value));
							break;
						}
					}
					// 将所有条件用 and 联合起来
					if (!predicates.isEmpty()) {
						if(null != linkOper && "or".equals(linkOper.toLowerCase().trim())) {
							return builder.or(predicates.toArray(new Predicate[predicates.size()]));
						}
						return builder.and(predicates.toArray(new Predicate[predicates.size()]));
					}
				}
				return builder.conjunction();
			}
		};
	}

	/**
	 * 构造查询语句
	 * <p>
	 * 支持自定义扩展查询条件<br/>
	 * “EQ”、“NEQ”、“LIKE”、“GT”、“LT”、“GTE”、“LTE”
	 * </p>
	 * @param sqlParameter
	 * @param entityClazz
	 * @return
	 */
	public static <T> Specification<T> and(final SQLParameter<Parameter> sqlParameter, final Class<T> entityClazz) {
		return bySearchFilter(sqlParameter, entityClazz, "and");
	}

	/**
	 * 构造查询语句
	 * <p>支持自定义扩展查询条件</p>
	 * @param sqlParameter
	 * @param entityClazz
	 * @return
	 */
	public static <T> Specification<T> or(final SQLParameter<Parameter> sqlParameter, final Class<T> entityClazz) {
		return bySearchFilter(sqlParameter, entityClazz, "or");
	}

	/**
	 * 构造查询语句
	 * <p>支持自定义扩展查询条件</p>
	 * @param sqlParameter
	 * @param entityClazz
	 * @param linkOper
	 * @return
	 */
	protected static <T> Specification<T> bySearchFilter(final SQLParameter<Parameter> sqlParameter, final Class<T> entityClazz, final String linkOper) {
		return new Specification<T>() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (!sqlParameter.toList().isEmpty()) {

					List<Predicate> predicates = new ArrayList<Predicate>();
					for (Parameter parameter : sqlParameter.toList()) {
						// nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
						String[] names = StringUtils.split(parameter.getName(), ".");
						Path expression = root.get(names[0]);
						for (int i = 1; i < names.length; i++) {
							expression = expression.get(names[i]);
						}

						// logic operator
						if(StringUtils.isEmpty(parameter.getOperator())) {
							//默认使用EQ表达式进行查询
							parameter.setOperator("EQ");
//							throw new IllegalArgumentException("查询条件 operator 不能为空");
						}else {
							parameter.setOperator(parameter.getOperator().trim().toUpperCase());
						}
						switch (parameter.getOperator()) {
						case "EQ":
							predicates.add(builder.equal(expression, parameter.getValue()));
							break;
						case "NEQ":
							predicates.add(builder.notEqual(expression, parameter.getValue()));
							break;
						case "LIKE":
							predicates.add(builder.like(expression, String.valueOf(parameter.getValue())));
							break;
						case "GT":
							predicates.add(builder.greaterThan(expression, (Comparable) parameter.getValue()));
							break;
						case "LT":
							predicates.add(builder.lessThan(expression, (Comparable) parameter.getValue()));
							break;
						case "GTE":
							predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) parameter.getValue()));
							break;
						case "LTE":
							predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) parameter.getValue()));
							break;
						case "IN":
							Expression exp = root.get(parameter.getName());
							Predicate predicate = exp.in((Collection) parameter.getValue());
							predicates.add(predicate);
							break;
						}
					}
					// 将所有条件用 and 联合起来
					if (!predicates.isEmpty()) {
						if(null != linkOper && "or".equals(linkOper.toLowerCase().trim())) {
							return builder.or(predicates.toArray(new Predicate[predicates.size()]));
						}
						return builder.and(predicates.toArray(new Predicate[predicates.size()]));
					}
				}
				return builder.conjunction();
			}
		};
	}

	/**
	 * 获取分页参数
	 * @return
	 */
	protected PageRequest getPageRequest() {
		return this.getPageRequest(null);
	}

	/**
	 * 获取分页参数
	 * @param sort
	 * @return
	 */
	protected PageRequest getPageRequest(Sort sort) {
		Paginator paginator = new Paginator();
		PageRequest pageRequest = new PageRequest(paginator.getPage()-1, paginator.getRows(), sort);
		return pageRequest;
	}
}
