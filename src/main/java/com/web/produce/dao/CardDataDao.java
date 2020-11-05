package com.web.produce.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.produce.entity.CardData;

import java.util.List;
import java.util.Map;


public interface CardDataDao extends CrudRepository<CardData, Long>,JpaSpecificationExecutor<CardData>{

	public List<CardData> findAll();
	
	public List<CardData> findByDelFlag(Integer delFlag);
	
	public List<CardData> findByDelFlagAndEmpIdAndDevClockIdAndCardDateAndCardTimeAndFstatus(Integer delFlag,Long emppId,Long devClockId,String carDate,String cardTime,Integer fstatus);
	
	public List<CardData> findByDelFlagAndEmpIdAndDevClockIdAndCardDateAndCardTime(Integer delFlag,Long emppId,Long devClockId,String carDate,String cardTime);
	
	public CardData findById(long id);
	
	@Query(value = "select c.class_no,c.id,c.class_name from MES_BASE_CLASS_TYPE c where c.del_flag=0 ", nativeQuery = true)
    public List<Map<String, Object>> queryClass();
	
	@Query(value = "SELECT ROWNUM NUM1 ,A.* FROM (SELECT E.EMP_CODE,E.EMP_NAME,D.DEV_IP,C.CARD_DATE,C.CARD_TIME,DECODE(C.FSTATUS,1,'有效','失效')FSTATUS FROM MES_ATT_CARDDATA C LEFT JOIN MES_BASE_EMPLOYEE E ON C.EMP_ID=E.ID"+
" LEFT JOIN MES_BASE_DEV_CLOCK D ON C.DEV_CLOCK_ID=D.ID "+
 " WHERE C.DEL_FLAG=0 ORDER BY C.CARD_DATE DESC,C.CARD_TIME DESC )A", nativeQuery = true)
    public List<Map<String, Object>> queryExport();
	
	@Query(value = "SELECT D.* FROM MES_ATT_CARDDATA D LEFT JOIN MES_BASE_DEV_CLOCK C ON D.DEV_CLOCK_ID = C.ID "+
" LEFT JOIN MES_BASE_EMPLOYEE E ON E.ID = D.EMP_ID WHERE D.DEL_FLAG=0 AND D.CARD_DATE=?1 AND D.CARD_TIME=?2 AND E.EMP_CODE=?3 AND C.DEV_CODE=?4 ", nativeQuery = true)
	 public List<Map<String, Object>> findBySnAndTime(String cdate,String ctime,String ecode,String sn);
	
	
}
