package com.utils;

import com.app.base.data.ApiResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Transactional(propagation = Propagation.REQUIRED)
public class PrcImpl  {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> doPrc(String param,String prc_name){
        List<String> resultList = (List<String>) jdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "{call "+prc_name+"(?,?)}";// 调用的sql
                CallableStatement cs = con.prepareCall(storedProc);
                cs.setString(1, param);
                cs.registerOutParameter(2,java.sql.Types.VARCHAR);// 注册输出参数 返回类型
                return cs;
            }
        }, new CallableStatementCallback() {
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                List<String> result = new ArrayList<String>();
                cs.execute();
                result.add(cs.getString(2));
                return result;
            }
        });

        return resultList;
    }

}
