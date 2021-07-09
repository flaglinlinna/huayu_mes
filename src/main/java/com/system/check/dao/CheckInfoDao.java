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
			" where p.bs_Record_Id = (?1) and p.bs_check_Code= (?2) and p.del_flag=0 order by p.id ", nativeQuery = true)
	public List<CheckInfo> findAllByRecordId(Long id, String checkCode);

	//营销关联查找出被驳回的信息
	@Query(value = "select * from (select p.*  from "+ CheckInfo.TABLE_NAME +" p "+
			" where p.bs_Record_Id = (?1) and p.bs_check_Code= (?2) and p.del_flag=0  " +
			" union select t.*  from "+ CheckInfo.TABLE_NAME +" t where t.bs_Record_Id = (?1) " +
			"and t.del_flag=0 and t.bs_Check_Grade = 1 and t.bs_Step_Check_Status = 2  ) a  order by a.lastupdate_date ", nativeQuery = true)
    public List<CheckInfo> findAllByBusiness(Long id, String checkCode);

		@Query(value = "select p.*  from "+ CheckInfo.TABLE_NAME +" p "+
			" where p.bs_Record_Id = (?1) and p.bs_check_Code= (?2) and p.del_flag=0 and p.lastupdate_date is  null ", nativeQuery = true)
    public List<CheckInfo> findNotByRecordId(Long id, String checkCode);

		@Query(value = "select p.*  from "+ CheckInfo.TABLE_NAME +" p "+
			" where p.bs_Record_Id = (?1) and p.bs_check_Code= (?2) and p.del_flag=0 and p.lastupdate_date is not null ", nativeQuery = true)
    public List<CheckInfo> findLastByRecordId(Long id, String checkCode);

	@Query(value = "select p.*  from "+ CheckInfo.TABLE_NAME +" p "+
			" where p.bs_Record_Id = (?1) and p.bs_check_Code= (?2) and p.bs_Check_Grade=(?3) and p.del_flag=0  ", nativeQuery = true)
    public List<CheckInfo> findByRecordIdAndStep(Long id, String checkCode, int step);

}
