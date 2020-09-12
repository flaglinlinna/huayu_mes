package com.system.user.dao;

import com.system.user.entity.SysUser;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * 用户表
 */
public interface SysUserDao extends CrudRepository<SysUser, Long>, JpaSpecificationExecutor<SysUser> {

	public int countByIsDelAndBsCode(Integer isDel, String bsCode);

	public int countByIsDelAndBsCodeAndIdNot(Integer isDel, String bsCode, Long id);

    public SysUser findByIsDelAndBsCode(Integer isDel, String bsCode);

    public SysUser findById(long id);

//    public int countByFcode(String userCode);
//
//    public SysUser findByFcode(String userCode);
//
//    public List<SysUser> findByFid(String fid);
//    
//    @Query(value = " call p_production_plan_check(:inParam1,:inParam2,:inParam3,:inParam4)", nativeQuery = true)
//    List<Map<String, Object>> pPlanCheck(@Param("inParam1") String calStart,@Param("inParam2") String calEnd,@Param("inParam3") String workshopcode,@Param("inParam4") String orderno);

    @Query(value = "select s.fcode,s.fname,s.fpassword,s.fcompany,s.ffactory from sys_user s where  upper(s.fcode) =?1 ", nativeQuery = true)
    public List<Map<String, Object>> findByUserCode(String usercode);
    
    @Query(value = "select m.param_value pv from mes_sys_params m where m.param_code='AppVersion' ", nativeQuery = true)
    public List<Map<String, Object>> queryAppVersion();
    
    @Query(value = "select m.param_value pv from mes_sys_params m where m.param_code='AppUrl' ", nativeQuery = true)
    public List<Map<String, Object>> queryApkUrl();
    
    @Query(value = "select m.param_value pv from mes_sys_params m where m.param_code='AppSize' ", nativeQuery = true)
    public List<Map<String, Object>> queryAppSize();
    
    @Modifying
    @Transactional
	@Query(value = "update sys_user i set i.fpassword=?2 where upper(i.fcode) =?1 ", nativeQuery = true)
	public void updatePwsByUserCode(String usercode,String pwd);
    /**
     *  User.pluslIO自定义存储过程的名字
     * @param arg
     * @return
     */
/*    @Procedure(name = "User.plusl")
    String entityAnnotatedCustomNamedProcedurePluslIO(@Param("C_USER_NO") String c_User_No,@Param("c_MachType") String c_MachType);*/
    @Procedure(name = "User.plusl")
    Integer entityAnnotatedCustomNamedProcedurePluslIO(@Param("arg") String arg);
    
    
    @Procedure(name="test")
    int createPolicy(@Param("a")int a);
    
    
}
