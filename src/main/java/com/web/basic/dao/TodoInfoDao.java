package com.web.basic.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.web.basic.entity.TodoInfo;


public interface TodoInfoDao extends CrudRepository<TodoInfo, Long>, JpaSpecificationExecutor<TodoInfo>{
	public TodoInfo findById(long id);
    @Modifying
    @Query("update TodoInfo t set t.bsStatus='1' where t.id=?1 and t.bsStatus='0'")
    public void closeById(Long id);

    @Modifying
    @Query("update TodoInfo t set t.bsStatus='1' where t.bsUserId=?1 and t.bsReferId=?2")
    public void closeByBsUserIdAndBsReferId(Long bsUserId, Long bsReferId);

    @Modifying
    @Query("update TodoInfo t set t.bsStatus='1' where t.bsReferId=?1")
    public void closeByBsAndBsReferId(Long bsReferId);

    @Modifying
    @Query("update TodoInfo t set t.bsStatus='1' where t.bsReferId=?1 and t.bsStatus='0'")
    public void closeByBsReferId(Long bsReferId);

    @Modifying
    @Query("update TodoInfo t set t.bsStatus='1' where t.bsReferId=?1 and t.bsType=?2 and t.bsStatus='0'")
    public void closeByBsReferIdAndBsType(Long bsReferId, Integer bsType);

    @Modifying
    @Query(value = "update "+TodoInfo.TABLE_NAME+" t set t.bs_status = '1' where t.bs_refer_id = ?2 and t.bs_status = '0' and t.bs_user_id " +
            "in (select v.pk_user from sys_user_roles_agg v where v.pk_role = ?1)", nativeQuery = true)
    public void closeByRoleIdAndBsReferId(Long roleId, Long bsReferId);

    public int countByIsDelAndBsUserId(Integer isDel, Long bsUserId);

    public int countByIsDelAndBsUserIdAndBsStatus(Integer isDel, Long bsUserId, int bsStatus);

}
