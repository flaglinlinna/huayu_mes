package com.web.report.service.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.web.report.service.CommonService;

@Service(value = "CommonService")
@Transactional(propagation = Propagation.REQUIRED)
public class CommonImpl  implements CommonService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

}
