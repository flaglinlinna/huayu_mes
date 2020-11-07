package com.system.user.dao;

import com.system.user.entity.SysUser;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	public int countByDelFlagAndUserCode(Integer delFlag, String userCode);

	public int countByDelFlagAndUserCodeAndIdNot(Integer delFlag, String userCode, Long id);

    public SysUser findByDelFlagAndUserCode(Integer delFlag, String userCode);

    public SysUser findById(long id);


//    @Query(value = "select * from sys_user su left join SYS_USER_ROLE sur on su.ID =sur.USER_ID" +
//            " where su.DEL_FLAG = 0 and sur.ROLE_ID = ?1 and sur.DEL_FLAG = 0",
//            countQuery = "select count(*) from sys_user su left join SYS_USER_ROLE sur on su.ID =sur.USER_ID" +
//                    " where su.DEL_FLAG = 0 and sur.ROLE_ID = 5152 and sur.DEL_FLAG = 0",
//            nativeQuery = true)

    @Query( value = "select u.* from  sys_user u left join  sys_user_role r on r.user_id=u.id  where u.del_flag=0 and u.status=0 and r.role_id=?1 and r.del_flag=0 ",
            countQuery = "select count(u.id)c from  sys_user u left join  sys_user_role r on r.user_id=u.id  where u.del_flag=0 and u.status=0 and r.role_id=?1 and r.del_flag=0 ",
            nativeQuery = true)
    public Page<Map<String, Object>> getListByRoleId(Long roleId, Pageable pageable);

//    public int countByFcode(String userCode);
//
//    public SysUser findByFcode(String userCode);
//
//    public List<SysUser> findByFid(String fid);
//
//    @Query(value = " call p_production_plan_check(:inParam1,:inParam2,:inParam3,:inParam4)", nativeQuery = true)
//    List<Map<String, Object>> pPlanCheck(@Param("inParam1") String calStart,@Param("inParam2") String calEnd,@Param("inParam3") String workshopcode,@Param("inParam4") String orderno);

    @Query(value = "select s.ID,s.USER_CODE,s.USER_NAME,s.PASSWORD,s.COMPANY,s.FACTORY from sys_user s where  s.USER_CODE =?1 and DEL_FLAG='0'", nativeQuery = true)
    public List<Map<String, Object>> findByUserCode(String usercode);

    @Query(value = "select m.param_value pv from mes_sys_params m where m.param_code='AppVersion' ", nativeQuery = true)
    public List<Map<String, Object>> queryAppVersion();

    @Query(value = "select m.param_value pv from mes_sys_params m where m.param_code='AppUrl' ", nativeQuery = true)
    public List<Map<String, Object>> queryApkUrl();

    @Query(value = "select m.param_value pv from mes_sys_params m where m.param_code='AppSize' ", nativeQuery = true)
    public List<Map<String, Object>> queryAppSize();

    @Modifying
    @Transactional
	@Query(value = "update sys_user i set i.fpassword=?2 where i.fcode =?1 ", nativeQuery = true)
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
