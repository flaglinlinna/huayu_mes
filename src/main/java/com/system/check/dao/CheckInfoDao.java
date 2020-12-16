package com.system.check.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.system.check.entity.CheckInfo;


public interface CheckInfoDao extends CrudRepository<CheckInfo, Long>, JpaSpecificationExecutor<CheckInfo> {
	@Query(value = "select p.*  from "+ CheckInfo.TABLE_NAME +" p "+
			" where p.bs_Record_Id = (?1) and p.bs_check_Code= (?2) and p.is_del=0 order by p.id ", nativeQuery = true)
public List<CheckInfo> findAllByRecordId(Long id, String checkCode);
	
		@Query(value = "select p.*  from "+ CheckInfo.TABLE_NAME +" p "+
			" where p.bs_Record_Id = (?1) and p.bs_check_Code= (?2) and p.is_del=0 and p.modified_Time is  null ", nativeQuery = true)
public List<CheckInfo> findNotByRecordId(Long id, String checkCode);
	
		@Query(value = "select p.*  from "+ CheckInfo.TABLE_NAME +" p "+
			" where p.bs_Record_Id = (?1) and p.bs_check_Code= (?2) and p.is_del=0 and p.modified_Time is not null ", nativeQuery = true)
public List<CheckInfo> findLastByRecordId(Long id, String checkCode);
	
	@Query(value = "select p.*  from "+ CheckInfo.TABLE_NAME +" p "+
			" where p.bs_Record_Id = (?1) and p.bs_check_Code= (?2) and p.bs_Check_Grade=(?3) and p.is_del=0  ", nativeQuery = true)
public List<CheckInfo> findByRecordIdAndStep(Long id, String checkCode, int step);
	
	/**
	  报价审批专业-允许重复提交审批 
	  */
	 /*@Query(value = "select p.*  from "+ CheckInfo.TABLE_NAME +" p "+
			" where p.bs_Record_Id = (?1) and p.bs_check_Code= (?2) and p.is_del=0 order by p.id ", nativeQuery = true)
public List<CheckInfo> findAllByRecordIdAndStatus(Long id,String checkCode);
	
	*//**
	 * 获取供应商审批信息
	 * @param id
	 * @param checkCode
	 * @return
	 *//*
	@Query(value = "select  p.id,p.bs_check_name,p.bs_check_comments,w.bs_flow_name,p.bs_modified_time,p.bs_check_des  from "+ CheckInfo.TABLE_NAME +" p  "+
			" left join srm_workflow w on w.bs_flow_code = p.bs_check_code where p.bs_Record_Id = (?1) and (p.bs_check_Code='ASSESS' or p.bs_check_Code='REGISTER') and p.is_del=0 order by p.id ", nativeQuery = true)
public List<Map<String, Object>> findSuppByRecordId(Long id);
	
	*//**
	 * 驳回重新提交时，预评估的审批记录删掉
	 * @param ids
	 *//*
	@Modifying
	@Query("update CheckInfo i set i.bsIsDel=1 where bsRecordId =(?1)  and i.bsCheckCode='ASSESS' ")
	public void deleteById(Long ids);
	
	*//**
	 * 供应商获取最新的修改意见
	 * @param ids
	 *//*
	@Query(value = "select * from (select t.* from (select tt.* from srm_check_info tt ,srm_supplier_info s where tt.bs_check_code='RETURN' and tt.bs_record_id=(?1) and s.id=tt.bs_record_id and s.bs_supp_status=1) t  order by t.id desc) where rownum=1 ", nativeQuery = true)
	public List<CheckInfo>  getSuppRemark(Long ids);

	public List<CheckInfo> findByBsIsDelAndBsRecordIdAndBsCheckCodeOrderByIdDesc(Integer bsIsDel, Long bsRecordId, String bsCheckCode);
	
	
	@Modifying
	@Query("update CheckFilesSrm t set t.bsIsDel=1   where t.bsRecordId=(?1) and bsCheckId in (?2)")
	public void updateBsIsDelByBsRecordId(Long id,Long[] bsCheckIds);
	*/
	
}
