package com.utils;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springside.modules.utils.Collections3;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 基础数据操作业务类
 */
public class BaseService {

//    @Autowired
//    private EntityManager entityManager;

    /**
     * 扩展查询条件：and连接
     * @param filters
     * @param entityClazz
     * @param <T>
     * @return
     */
    public static <T> Specification<T> and(final Collection<com.utils.SearchFilter> filters, final Class<T> entityClazz) {
        return bySearchFilter(filters, entityClazz, "and");
    }

    /**
     * 扩展查询条件：or连接
     * @param filters
     * @param entityClazz
     * @param <T>
     * @return
     */
    public static <T> Specification<T> or(final Collection<com.utils.SearchFilter> filters, final Class<T> entityClazz) {
        return bySearchFilter(filters, entityClazz, "or");
    }

    /**
     * 扩展查询条件封装：默认仅支持and连接，添加连接操作字段，可支持and或or
     * @param filters
     * @param entityClazz
     * @param linkOper "and" / "or"
     * @param <T>
     * @return
     */
    public static <T> Specification<T> bySearchFilter(final Collection<com.utils.SearchFilter> filters, final Class<T> entityClazz, final String linkOper) {
        return new Specification<T>() {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                if (Collections3.isNotEmpty(filters)) {

                    List<Predicate> predicates = new ArrayList<Predicate>();
                    for (com.utils.SearchFilter filter : filters) {
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
                            case NEQ:
                                predicates.add(builder.notEqual(expression, filter.value));
                                break;
                            case LIKE:
                                predicates.add(builder.like(expression, "%" + filter.value + "%"));
                                break;
                            case LLIKE:
                                predicates.add(builder.like(expression, filter.value + "%"));
                                break;
                            case RLIKE:
                                predicates.add(builder.like(expression, "%" + filter.value));
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
                            case NULL:
                                predicates.add(builder.isNull(expression));
                                break;
                            case NOTNULL:
                                predicates.add(builder.isNotNull(expression));
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

//    /**
//     * 扩展查询条件：and连接
//     * @param filters
//     * @param entityClazz
//     * @param <T>
//     * @return
//     */
//    public static <T> Specification<T> and(final Collection<org.springside.modules.persistence.SearchFilter> filters, final Class<T> entityClazz) {
//        return bySearchFilter(filters, entityClazz, "and");
//    }
//
//    /**
//     * 扩展查询条件：or连接
//     * @param filters
//     * @param entityClazz
//     * @param <T>
//     * @return
//     */
//    public static <T> Specification<T> or(final Collection<org.springside.modules.persistence.SearchFilter> filters, final Class<T> entityClazz) {
//        return bySearchFilter(filters, entityClazz, "or");
//    }
//
//    /**
//     * 扩展查询条件封装：默认仅支持and连接，添加连接操作字段，可支持and或or
//     * @param filters
//     * @param entityClazz
//     * @param linkOper "and" / "or"
//     * @param <T>
//     * @return
//     */
//    public static <T> Specification<T> bySearchFilter(final Collection<org.springside.modules.persistence.SearchFilter> filters, final Class<T> entityClazz, final String linkOper) {
//        return new Specification<T>() {
//            @SuppressWarnings({ "unchecked", "rawtypes" })
//            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
//                if (Collections3.isNotEmpty(filters)) {
//
//                    List<Predicate> predicates = new ArrayList<Predicate>();
//                    for (org.springside.modules.persistence.SearchFilter filter : filters) {
//                        // nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
//                        String[] names = StringUtils.split(filter.fieldName, ".");
//                        Path expression = root.get(names[0]);
//                        for (int i = 1; i < names.length; i++) {
//                            expression = expression.get(names[i]);
//                        }
//
//                        // logic operator
//                        switch (filter.operator) {
//                            case EQ:
//                                predicates.add(builder.equal(expression, filter.value));
//                                break;
//                            case LIKE:
//                                predicates.add(builder.like(expression, "%" + filter.value + "%"));
//                                break;
//                            case GT:
//                                predicates.add(builder.greaterThan(expression, (Comparable) filter.value));
//                                break;
//                            case LT:
//                                predicates.add(builder.lessThan(expression, (Comparable) filter.value));
//                                break;
//                            case GTE:
//                                predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.value));
//                                break;
//                            case LTE:
//                                predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.value));
//                                break;
//                        }
//                    }
//                    // 将所有条件用 and 联合起来
//                    if (!predicates.isEmpty()) {
//                        if(null != linkOper && "or".equals(linkOper.toLowerCase().trim())) {
//                            return builder.or(predicates.toArray(new Predicate[predicates.size()]));
//                        }
//                        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
//                    }
//                }
//                return builder.conjunction();
//            }
//        };
//    }

//    /**
//     * 原生sql,查询数据
//     *
//     * @param sql
//     * @param paramMap
//     * @return
//     */
//    public List<Map<String, Object>> findBySql(String sql, Map<String, Object> paramMap) {
//        return createSQLQuery(sql, null, paramMap);
//    }
//
//    /**
//     * 调用sql查询返回map对象
//     *
//     * @param sql		sql查询语句
//     * @param pageable	分页参数
//     * @param paramMap	绑定变量参数值
//     * @return
//     */
//    private List<Map<String, Object>> createSQLQuery(String sql, Pageable pageable, Map<String, Object> paramMap) {
//        return this.createSQLQuery(sql, pageable, paramMap, null);
//    }
//
//    /**
//     * 调用sql查询返回map对象
//     *
//     * @param sql		sql查询语句
//     * @param pageable	分页参数
//     * @param paramMap	绑定变量参数值
//     * @param cls		查询结果要封装的实体类
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    public <T> List<T> createSQLQuery(String sql, Pageable pageable, Map<String, Object> paramMap, Class<T> cls) {
//        Session session = entityManager.unwrap(Session.class);
//        SQLQuery query = session.createSQLQuery(sql);
//
//        if (pageable != null) {
//            query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
//            query.setMaxResults(pageable.getPageSize());
//        }
//
//        if (paramMap != null && paramMap.size() > 0) {
//            for (String key : paramMap.keySet()) {
//                query.setParameter(key, paramMap.get(key));
//            }
//        }
//        if(null==cls) {
//            query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//        }else {
//            query.setResultTransformer(Transformers.aliasToBean(cls));
//        }
//
//        List<T> list = query.list();
////		List<Map<String, Object>> maplist = (List<Map<String, Object>>) list;
//        return list;
//    }
//
//    /**
//     * 原生sql,分页查询数据
//     *
//     * @param sql		sql查询语句
//     * @param pageable	分页参数
//     * @param paramMap	绑定变量参数值
//     * @param cls		查询结果要封装的实体类
//     * @return
//     */
//    public <T> Page<T> findPageBySql(String sql, Pageable pageable, Map<String, Object> paramMap, Class<T> cls) {
//        List<T> list = new ArrayList<T>();
//        long total = 0L;
//        if (pageable != null) {
//            total = countBySql(sql, paramMap);
//            if (total > 0) {
//                list = createSQLQuery(sql, pageable, paramMap, cls);
//            }
//        } else {
//            list = createSQLQuery(sql, pageable, paramMap, cls);
//            if (list != null) {
//                total = list.size();
//            }
//        }
//        PageImpl<T> page = new PageImpl<T>(list, pageable, total);
//        return page;
//    }
//
//    /**
//     * 原生sql,查询记录数
//     *
//     * @param sql
//     * @param paramMap
//     * @return
//     */
//    public long countBySql(String sql, Map<String, Object> paramMap) {
//        StringBuffer sbuf = new StringBuffer("select count(*) total from ( " + sql + " ) t");
//        List<Map<String, Object>> dblist = createSQLQuery(sbuf.toString(), null, paramMap);
//        long total = 0;
//        if (dblist != null && dblist.size() != 0) {
//            total = Long.parseLong(dblist.get(0).entrySet().iterator().next().getValue().toString());
//        }
//        return total;
//    }
}
