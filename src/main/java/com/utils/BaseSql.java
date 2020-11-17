package com.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Pageable;

public class BaseSql {
	
	@PersistenceContext
	private EntityManager em;// 类似hibernate session

	//给子类用的
	private EntityManager getEntityManager() {
	    return em;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> createSQLQuery(String sql, Map<String, Object> paramMap, Class<T> cls) {
		Session session = getEntityManager().unwrap(Session.class);
		SQLQuery query = session.createSQLQuery(sql);

		if (cls != null) {
			query.addEntity(cls);
		}
		if (paramMap != null && paramMap.size() > 0) {
			for (String key : paramMap.keySet()) {
				query.setParameter(key, paramMap.get(key));
			}
		}
		// query.setResultTransformer(null==cls? Transformers.ALIAS_TO_ENTITY_MAP :
		// Transformers.aliasToBean(cls));
		List<T> list = query.list();
		List<Map<String, Object>> maplist = (List<Map<String, Object>>) list;

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> createSQLQuery(String sql, Map<String, Object> paramMap) {
		Session session = getEntityManager().unwrap(Session.class);
		SQLQuery query = session.createSQLQuery(sql);


		if (paramMap != null && paramMap.size() > 0) {
			for (String key : paramMap.keySet()) {
				query.setParameter(key, paramMap.get(key));
			}
		}
		// query.setResultTransformer(null==cls? Transformers.ALIAS_TO_ENTITY_MAP :
		// Transformers.aliasToBean(cls));
		List<Object[]> list = query.list();
		return list;
	}
	

}
