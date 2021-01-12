package com.web.quote.service.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.todo.service.TodoInfoService;
import com.system.user.dao.SysUserDao;
import com.utils.BaseSql;
import com.web.basePrice.dao.ProdTypDao;
import com.web.basePrice.dao.ProfitProdDao;
import com.web.quote.dao.QuoteDao;
import com.web.quote.dao.QuoteItemBaseDao;
import com.web.quote.dao.QuoteItemDao;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteItem;
import com.web.quote.service.QuoteAllService;

@Service(value = "QuoteAllService")
@Transactional(propagation = Propagation.REQUIRED)
public class QuoteAlllmpl  extends BaseSql implements QuoteAllService {
	
	@Autowired
    private QuoteDao quoteDao;


    /**
     * 获取报价单列表
     * **/
    @Override
    @Transactional
    public ApiResponseResult getList(String keyword,String status,PageRequest pageRequest)throws Exception{

        String sql = "select distinct p.id,p.bs_Code,p.bs_Type,p.bs_Status,p.bs_Finish_Time,p.bs_Remarks,p.bs_Prod,"
                + "p.bs_Similar_Prod,p.bs_Dev_Type,p.bs_Prod_Type,p.bs_Cust_Name,p.bs_position,p.bs_Manage_fee,  " +
                "p.bs_Material,p.bs_Chk_Out_Item,p.bs_Chk_Out,p.bs_Function_Item,p.bs_Function,p.bs_Require,p.bs_Level," +
                "p.bs_Cust_Require,p.bs_status2, p.bs_status2hardware, p.bs_status2molding,p.bs_status2out,p.bs_status2packag, p.bs_status2purchase,p.bs_status2surface, p.bs_status3, p.bs_status4 from "+Quote.TABLE_NAME+" p  "
                + "  where p.del_flag=0";

        if(!StringUtils.isEmpty(status)){
            sql += "  and p.bs_Status = " + status + "";
        }
        if (StringUtils.isNotEmpty(keyword)) {
			sql += "  and INSTR((p.bsType || p.bsCode || p.bsProd),  '"
					+ keyword + "') > 0 ";
        }

        int pn = pageRequest.getPageNumber() + 1;

        String sql_page = "SELECT * FROM  (  SELECT A.*, ROWNUM RN  FROM ( " + sql + " ) A  WHERE ROWNUM <= ("
                + pn + ")*" + pageRequest.getPageSize() + "  )  WHERE RN > (" + pageRequest.getPageNumber() + ")*"
                + pageRequest.getPageSize() + " ";

        Map<String, Object> param = new HashMap<String, Object>();

        List<Object[]>  list = createSQLQuery(sql_page, param);
        long count = createSQLQuery(sql, param, null).size();

        List<Map<String, Object>> list_new = new ArrayList<Map<String, Object>>();
        for (int i=0;i<list.size();i++) {
            Object[] object=(Object[]) list.get(i);
            Map<String, Object> map1 = new HashMap<>();
            map1.put("id", object[0]);
            map1.put("bsCode", object[1]);
            map1.put("bsType", object[2]);
            map1.put("bsStatus", object[3]);
            map1.put("bsFinishTime", object[4]);
            map1.put("bsRemarks", object[5]);
            map1.put("bsProd", object[6]);
            map1.put("bsSimilarProd", object[7]);
            map1.put("bsDevType", object[8]);
            map1.put("bsProdType", object[9]);
            map1.put("bsCustName", object[10]);
            map1.put("bsPosition", object[11]);
            map1.put("bsManageFee", object[12]);
            map1.put("bsMaterial", object[13]);
            map1.put("bsChkOutItem", object[14]);
            map1.put("bsChkOut", object[15]);
            map1.put("bsFunctionItem", object[16]);
            map1.put("bsFunction", object[17]);
            map1.put("bsRequire", object[18]);
            map1.put("bsLevel", object[19]);
            map1.put("bsCustRequire", object[20]);
            
            map1.put("bsStatus2", object[21]);
            map1.put("bsStatus2hardware", object[22]);
            map1.put("bsStatus2molding", object[23]);
            map1.put("bsStatus2out", object[24]);
            map1.put("bsStatus2packag", object[25]);
            map1.put("bsStatus2purchase", object[26]);
            map1.put("bsStatus2surface", object[27]);
            map1.put("bsStatus3", object[28]);
            map1.put("bsStatus4", object[29]);
            
            list_new.add(map1);
        }

        Map map = new HashMap();
        map.put("List", DataGrid.create(list_new,  (int) count,
                pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
        map.put("Nums", quoteDao.getNumByStatus4());
        return ApiResponseResult.success().data(map);
    }


}
