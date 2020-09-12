
insert into sys_user(id,version,created_time,is_del,modified_time,pk_created_by,pk_modified_by,user_code,user_comment,
user_email,user_mobile,user_name,user_password,user_status,user_type,user_is_super) values
('1001',0,'2018-11-27 09:08:21',0,null,null,null,'admin',null,'admin@163.com',
'18003046436','管理员','E10ADC3949BA59ABBE56E057F20F883E',0,0,0);

insert into sys_role(id,version,created_time,is_del,modified_time,role_code,role_comment,role_name)
values ('1001',0,'2018-11-22 10:19:29',0,null,'admin','超级管理员','超级管理员');
insert into sys_role(id,version,created_time,is_del,modified_time,role_code,role_comment,role_name)
values ('1002',0,'2018-11-22 11:04:44',0,null,'supplier_manager','市场部经理','市场部经理');
insert into sys_role(id,version,created_time,is_del,modified_time,role_code,role_comment,role_name)
values ('1003',0,'2018-11-29 09:59:26',0,null,'purchase','采购部','采购部');
insert into sys_role(id,version,created_time,is_del,modified_time,role_code,role_comment,role_name)
values ('1004',0,'2018-12-07 09:34:12',0,null,'supplier_grade_tobe','待审核状态的供应商用户','待审核供应商用户');
insert into sys_role(id,version,created_time,is_del,modified_time,role_code,role_comment,role_name)
values ('1005',0,'2018-12-07 09:35:14',0,null,'supplier_grade_pass','审核通过的供应商用户','合格供应商用户');

insert into t_user_roles_map(id,version,created_time,is_del,modified_time,role_code,user_id) values ('1001','admin','1001',0,null,0,null);