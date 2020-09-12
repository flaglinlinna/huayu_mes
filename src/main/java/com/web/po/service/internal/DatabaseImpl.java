package com.web.po.service.internal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.enumeration.BasicStateEnum;
import com.web.po.dao.DatabaseInfoDao;
import com.web.po.entity.DatabaseInfo;
import com.web.po.service.DatabaseService;

@Service(value = "DatabaseService")
@Transactional(propagation = Propagation.REQUIRED)
public class DatabaseImpl  implements DatabaseService {

	@Autowired
    private DatabaseInfoDao databaseInfoDao;

	@Override
	public ApiResponseResult getlist(String keyword, PageRequest pageRequest) throws Exception {
		// TODO Auto-generated method stub
		List<SearchFilter> filters =new ArrayList<>();
        filters.add(new SearchFilter("isDel", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));

        //查询2
        List<SearchFilter> filters1 =new ArrayList<>();
        if(StringUtils.isNotEmpty(keyword)){
            filters1.add(new SearchFilter("bsCode", SearchFilter.Operator.LIKE, keyword));
            filters1.add(new SearchFilter("bsName", SearchFilter.Operator.LIKE, keyword));
        }
        Specification<DatabaseInfo> spec = Specification.where(BaseService.and(filters, DatabaseInfo.class));
        Specification<DatabaseInfo> spec1 =  spec.and(BaseService.or(filters1, DatabaseInfo.class));
        Page<DatabaseInfo> page = databaseInfoDao.findAll(spec1, pageRequest);
        return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}

	@Override
	public ApiResponseResult testConnection(String type, String url,String username,String password) throws Exception {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(type)){
			return ApiResponseResult.failure("数据库类型不能为空");
		}
		if(StringUtils.isEmpty(url)){
			return ApiResponseResult.failure("数据库地址不能为空");
		}
		if(StringUtils.isEmpty(username)){
			return ApiResponseResult.failure("数据库账号不能为空");
		}
		if(StringUtils.isEmpty(password)){
			return ApiResponseResult.failure("数据库密码不能为空");
		}
		
		if ((url.indexOf(":") !=-1)  && (url.indexOf("/") !=-1)){
			
		}else{
			return ApiResponseResult.failure("数据库地址格式不正确!");
		}
		
		if(type.equals("oracle")){
			try {        //加载数据库驱动类  
                Class.forName("oracle.jdbc.driver.OracleDriver");  
                System.out.println("数据库驱动加载成功");  //返回加载驱动成功信息  
            }catch(ClassNotFoundException e){  
                e.printStackTrace();  
                return ApiResponseResult.failure(e.toString());
            }  
            try { 
            	url = url.replace('/',':');
            	Connection con=DriverManager.getConnection("jdbc:oracle:thin:@"+url,username,password);//通过访问数据库的URL获取数据库连接对象 ，这里后两个参数分别是数据库的用户名及密码 
                System.out.println("数据库连接成功");  //返回连接成功信息
                con.close();
                return ApiResponseResult.success("连接成功");
            }catch(Exception e) {  
                e.printStackTrace(); 
                return ApiResponseResult.failure(e.toString());
            }
		}
		
		return null;
	}

	@Override
	public ApiResponseResult add(DatabaseInfo databaseInfo) throws Exception {
		// TODO Auto-generated method stub
		databaseInfoDao.save(databaseInfo);
		return ApiResponseResult.success("新增成功");
	}

}
