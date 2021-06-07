package com.web.produce.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.produce.entity.CardData;
import com.web.produce.entity.PatchCard;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface PatchCardDao extends CrudRepository<PatchCard, Long>,JpaSpecificationExecutor<PatchCard>{

	public List<PatchCard> findAll();
	
	public List<PatchCard> findByDelFlag(Integer delFlag);
	
	public PatchCard findById(long id);
	
	public int countByDelFlagAndEmpIdAndSignTimeAndSignDate(Integer delFlag,Long empId,String signTime,String signDate);
	
	@Query(value = "select c.class_no,c.id,c.class_name from MES_BASE_CLASS_TYPE c where c.del_flag=0 ", nativeQuery = true)
    public List<Map<String, Object>> queryClass();

//	TO_DATE(?4, 'SYYYY-MM-DD HH24:MI:SS')
	@Modifying
	@Query(value = "INSERT INTO MES_ATT_SIGN_CARD(ID,EMP_ID,TASK_NO, CARD_TYPE, SIGN_DATE, CHECK_STATUS,  CREATE_BY," +
			" CREATE_DATE, DEL_FLAG, CLASS_ID, LINE_ID, HOUR_TYPE, SIGN_TIME, " +
			"  WORK_DATE) VALUES (seq_mes_att_sign_card.nextval, " +
			" ?1,?2,?3,?4,?5,?6,?7,0,?8,?9,?10,?11,?12)",nativeQuery = true)
	public Integer insertWithSeq(Long empId,String taskNo,String cardType,String signDate,Integer checkStatus,Long createBy,
					Date createDate,Long classId, Long lineId, String hourType,String signTime,String workDate);
}
