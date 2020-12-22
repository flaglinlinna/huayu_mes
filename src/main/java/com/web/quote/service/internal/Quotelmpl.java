package com.web.quote.service.internal;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.web.basePrice.dao.BjWorkCenterDao;
import com.web.basePrice.dao.ItemTypeWgDao;
import com.web.basePrice.dao.UnitDao;
import com.web.basePrice.entity.BjWorkCenter;
import com.web.basePrice.entity.ItemTypeWg;
import com.web.basePrice.entity.Unit;
import com.web.quote.dao.QuoteBomDao;
import com.web.quote.entity.QuoteBom;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.todo.entity.TodoInfo;
import com.system.todo.service.TodoInfoService;
import com.system.user.dao.SysUserDao;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.dao.ProfitProdDao;
import com.web.basePrice.entity.ProfitProd;
import com.web.basic.entity.Client;
import com.web.basic.entity.Defective;
import com.web.quote.dao.QuoteDao;
import com.web.quote.dao.QuoteItemBaseDao;
import com.web.quote.dao.QuoteItemDao;
import com.web.quote.entity.Quote;
import com.web.quote.entity.QuoteItem;
import com.web.quote.entity.QuoteItemBase;
import com.web.quote.service.QuoteService;
import org.springframework.web.multipart.MultipartFile;

@Service(value = "QuoteService")
@Transactional(propagation = Propagation.REQUIRED)
public class Quotelmpl implements QuoteService {
	
	@Autowired
    private QuoteDao quoteDao;
	@Autowired
    private QuoteItemDao quoteItemDao;
	@Autowired
	private ProfitProdDao profitProdDao;
	@Autowired
    private QuoteItemBaseDao quoteItemBaseDao;
	@Autowired
    private TodoInfoService todoInfoService;

	@Autowired
	private SysUserDao sysUserDao;
	
	/**
     * 新增报价单
     */
    @Override
    @Transactional
	public ApiResponseResult add(Quote quote)throws Exception{
    	if(quote == null){
            return ApiResponseResult.failure("报价单不能为空！");
        }
    	//1:生成报价编号
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = sdf.format(new Date());
        quote.setBsCode("EQ-" + dateStr);  //编号格式：EQ-年月日时分秒
        
    	quote.setCreateDate(new Date());
    	quote.setCreateBy(UserUtil.getSessionUser().getId());
    	quote = quoteDao.save(quote);
    	//2:建立子表-编码-项目名-代办人-开始/结束时间-进度状态【需有基础信息】
    	List<QuoteItem> lqi = new ArrayList<QuoteItem>();
    	//获取配置的待办项目
    	List<QuoteItemBase> lqb = quoteItemBaseDao.findByDelFlagAndBsStyle(0,"item");
    	for(QuoteItemBase qb:lqb){
    		QuoteItem qi = new QuoteItem();
    		qi.setPkQuote(quote.getId());
    		qi.setBsCode(qb.getBsCode());
    		qi.setBsName(qb.getBsName());
    		qi.setToDoBy(qb.getToDoBy());
    		qi.setBsPerson(qb.getBsPerson());
    		qi.setCreateDate(new Date());
    		qi.setCreateBy(UserUtil.getSessionUser().getId());
    		qi.setBsStyle(qb.getBsStyle());
    		if(qb.getBsCode().equals("A001") || qb.getBsCode().equals("A002")){
    			qi.setBsBegTime(new Date());//20201218-fyx
    		}
    		lqi.add(qi);
    	}
    	quoteItemDao.saveAll(lqi);
    	//3:发送待办
    	for(QuoteItemBase qb:lqb){
    		TodoInfo todoInfo = new TodoInfo();
    		todoInfo.setBsType(1);//待办事项都是1
    		todoInfo.setBsUserId(qb.getToDoBy());
    		todoInfo.setBsTitle("新增报价-"+qb.getBsName()+"的资料待录入");
    		todoInfo.setBsContent(quote.getBsCode()+"-"+quote.getBsCustName()+ "的报价单");
    		todoInfo.setBsRouter(qb.getBsRouter()+quote.getId());
    		todoInfo.setBsReferId(quote.getId()); //关联ID
    		todoInfo.setBsModel(qb.getBsName());

    		todoInfoService.add(todoInfo);
    	}
    	
        return ApiResponseResult.success("报价单新增成功！").data(lqi);
	}
    /**
     * 获取产品利润率维护表
     * **/
    
    public ApiResponseResult getProfitProd()throws Exception{
    	List<ProfitProd> list=profitProdDao.findByDelFlag(0);
    	return ApiResponseResult.success().data(list);
    }
    
    /**
     * 获取报价单列表
     * **/
    @Override
    @Transactional
    public ApiResponseResult getList(String keyword,String status,PageRequest pageRequest)throws Exception{
    	// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		if(!StringUtils.isEmpty(status)){
			filters.add(new SearchFilter("bsStatus", SearchFilter.Operator.EQ, Integer.parseInt(status)));
		}
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("bsType", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("bsCode", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("bsProd", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<Quote> spec = Specification.where(BaseService.and(filters, Quote.class));
		Specification<Quote> spec1 = spec.and(BaseService.or(filters1, Quote.class));
		Page<Quote> page = quoteDao.findAll(spec1, pageRequest);

		Map map = new HashMap();
		map.put("List", DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
		map.put("Nums", quoteDao.getNumByStatus());
		return ApiResponseResult.success().data(map);
		/*return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));*/
    }
    /**
     * 获取报价单-项目列表
     * **/
    @Override
    @Transactional
    public ApiResponseResult getItemPage(Long id,String bsStatus)throws Exception{
    	List<QuoteItem> list=quoteItemDao.findByDelFlagAndPkQuoteAndBsStyle(0,id,bsStatus);
    	return ApiResponseResult.success().data(list);
    }
    
    /**
     * 变更报价单状态
     * **/
    @Override
    @Transactional
    public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception{
    	if(id == null){
            return ApiResponseResult.failure("报价单ID不能为空！");
        }
        if(bsStatus == null){
            return ApiResponseResult.failure("请正确设置报价单状态！");
        }
        Quote o = quoteDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该报价单不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setBsStatus(bsStatus);
        quoteDao.save(o);
        return ApiResponseResult.success("设置报价单状态成功！").data(o);
    }
    
    /**
     * 编辑报价单
     * **/
    @Override
    @Transactional
    public ApiResponseResult edit(Quote quote)throws Exception{
    	if(quote == null){
            return ApiResponseResult.failure("报价单不能为空！");
        }
        if(quote.getId() == null){
            return ApiResponseResult.failure("报价单ID不能为空！");
        }
        Quote o = quoteDao.findById((long) quote.getId());
        if(o == null){
            return ApiResponseResult.failure("该报价单不存在！");
        }
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setBsType(quote.getBsType());
        o.setBsFinishTime(quote.getBsFinishTime());
        o.setBsRemarks(quote.getBsRemarks());
        o.setBsSimilarProd(quote.getBsSimilarProd());
        o.setPkProfitProd(quote.getPkProfitProd());
        o.setBsCustName(quote.getBsCustName());
        o.setBsPosition(quote.getBsPosition());
        o.setBsMaterial(quote.getBsMaterial());
        o.setBsChkOutItem(quote.getBsChkOutItem());
        o.setBsChkOut(quote.getBsChkOut());
        o.setBsFunctionItem(quote.getBsFunctionItem());
        o.setBsFunction(quote.getBsFunction());
        o.setBsRequire(quote.getBsRequire());
        o.setBsLevel(quote.getBsLevel());
        o.setBsCustRequire(quote.getBsCustRequire());
        quoteDao.save(o);
        return ApiResponseResult.success("编辑成功！");
    }
    
    /**
     * 获取单张报价单部分内容
     * **/
    public ApiResponseResult getSingle(Long id)throws Exception{
    	if(id == null){
            return ApiResponseResult.failure("报价单ID不能为空！");
        }
        Quote o = quoteDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该报价单不存在！");
        }
        String cDate="";
        if(o.getCreateDate()!=null){
        	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             cDate = sdf.format(o.getCreateDate());//格式化创建时间
        }
       
        Map<String, Object> map = new HashMap<>();
        map.put("id", o.getId());
        map.put("bsType", o.getBsType());
        map.put("bsCode", o.getBsCode());
        map.put("bsProd", o.getBsProd());
        map.put("createDate", cDate);
        map.put("createBy", sysUserDao.findById((long) o.getCreateBy()).getUserName());
        return ApiResponseResult.success().data(map);
    }

    /**
     * 根据ID获取全部内容
     */
    @Override
    @Transactional
    public ApiResponseResult getSingleAll(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("报价单ID不能为空！");
        }
        Quote o = quoteDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该报价单不存在！");
        }
        return ApiResponseResult.success().data(o);
    }
    
	@Override
	public ApiResponseResult doItemFinish(String code, Long quoteId) throws Exception {
		// TODO Auto-generated method stub
		//单个项目完成后，执行
		//1.1 修改状态
		List<QuoteItem> lqi = quoteItemDao.findByDelFlagAndPkQuoteAndBsCode(0,quoteId,code);
		if(lqi.size() == 0){
			return ApiResponseResult.failure("报价单不存在!");
		}
		QuoteItem q = lqi.get(0);
		q.setBsEndTime(new Date());
		quoteItemDao.save(q);
		//2.1 查询该报价单是否都已经全部提交
		//2.3修改报价单状态（如果要自动发起审批则在这个地方触发，目前还未）
		/*List<QuoteItem> lqii = quoteItemDao.findByDelFlagAndPkQuoteAndNotBsEndTime(0,quoteId);
		if(lqii.size()>0){
			
		}*/
		
		return ApiResponseResult.success();
	}



}
