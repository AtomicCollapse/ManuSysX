DROP TABLE IF EXISTS t_pms_comp;
CREATE TABLE "pms"."t_pms_comp" (
  "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL DEFAULT gen_random_uuid(),
  "insert_time" timestamp(6) NOT NULL,
  "insert_user" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
  "update_time" timestamp(6),
  "update_user" varchar(40) COLLATE "pg_catalog"."default",
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "code" varchar(10) COLLATE "pg_catalog"."default",
  CONSTRAINT "t_pms_comp_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "pms"."t_pms_comp"
  OWNER TO "postgres";

COMMENT ON COLUMN "pms"."t_pms_comp"."id" IS '主键';

COMMENT ON COLUMN "pms"."t_pms_comp"."insert_time" IS '插入时间';

COMMENT ON COLUMN "pms"."t_pms_comp"."insert_user" IS '插入人';

COMMENT ON COLUMN "pms"."t_pms_comp"."update_time" IS '更新时间';

COMMENT ON COLUMN "pms"."t_pms_comp"."update_user" IS '更新人';

COMMENT ON COLUMN "pms"."t_pms_comp"."name" IS '企业名称';

COMMENT ON COLUMN "pms"."t_pms_comp"."code" IS '企业十位信用代码';

COMMENT ON TABLE "pms"."t_pms_comp" IS '企业表';

DROP TABLE IF EXISTS t_pms_dept;
CREATE TABLE "pms"."t_pms_dept" (
  "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL DEFAULT gen_random_uuid(),
  "insert_time" timestamp(6) NOT NULL,
  "insert_user" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
  "update_time" timestamp(6),
  "update_user" varchar(40) COLLATE "pg_catalog"."default",
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "head_id" varchar(36) COLLATE "pg_catalog"."default",
  CONSTRAINT "t_pms_dept_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "pms"."t_pms_dept"
  OWNER TO "postgres";

COMMENT ON COLUMN "pms"."t_pms_dept"."id" IS '主键';

COMMENT ON COLUMN "pms"."t_pms_dept"."insert_time" IS '插入时间';

COMMENT ON COLUMN "pms"."t_pms_dept"."insert_user" IS '插入人';

COMMENT ON COLUMN "pms"."t_pms_dept"."update_time" IS '更新时间';

COMMENT ON COLUMN "pms"."t_pms_dept"."update_user" IS '更新人';

COMMENT ON COLUMN "pms"."t_pms_dept"."name" IS '部门名称';

COMMENT ON COLUMN "pms"."t_pms_dept"."head_id" IS '所属企业ID';

COMMENT ON TABLE "pms"."t_pms_dept" IS '部门表';

DROP TABLE IF EXISTS t_pms_role;
CREATE TABLE "pms"."t_pms_role" (
  "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL DEFAULT gen_random_uuid(),
  "insert_time" timestamp(6) NOT NULL,
  "insert_user" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
  "update_time" timestamp(6),
  "update_user" varchar(40) COLLATE "pg_catalog"."default",
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "type" varchar(1) COLLATE "pg_catalog"."default",
	"head_id" varchar(36) COLLATE "pg_catalog"."default",
  CONSTRAINT "t_pms_role_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "pms"."t_pms_role"
  OWNER TO "postgres";

COMMENT ON COLUMN "pms"."t_pms_role"."id" IS '主键';

COMMENT ON COLUMN "pms"."t_pms_role"."insert_time" IS '插入时间';

COMMENT ON COLUMN "pms"."t_pms_role"."insert_user" IS '插入人';

COMMENT ON COLUMN "pms"."t_pms_role"."update_time" IS '更新时间';

COMMENT ON COLUMN "pms"."t_pms_role"."update_user" IS '更新人';

COMMENT ON COLUMN "pms"."t_pms_role"."name" IS '角色名称';

COMMENT ON COLUMN "pms"."t_pms_role"."type" IS '角色类型 0-管理员；1-一般用户';

COMMENT ON COLUMN "pms"."t_pms_role"."head_id" IS '所属部门 ID';

COMMENT ON TABLE "pms"."t_pms_role" IS '角色表';


DROP TABLE IF EXISTS t_pms_user;
CREATE TABLE "pms"."t_pms_user" (
  "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL DEFAULT gen_random_uuid(),
  "insert_time" timestamp(6) NOT NULL,
  "insert_user" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
  "update_time" timestamp(6),
  "update_user" varchar(40) COLLATE "pg_catalog"."default",
  "account" varchar(40) COLLATE "pg_catalog"."default",
  "password" varchar(30) COLLATE "pg_catalog"."default",
  "comp_id" varchar(36) COLLATE "pg_catalog"."default",
  "dept_id" varchar(36) COLLATE "pg_catalog"."default",
	"role_id" varchar(36) COLLATE "pg_catalog"."default",
  CONSTRAINT "t_pms_user_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "pms"."t_pms_user"
  OWNER TO "postgres";

COMMENT ON COLUMN "pms"."t_pms_user"."id" IS '主键';

COMMENT ON COLUMN "pms"."t_pms_user"."insert_time" IS '插入时间';

COMMENT ON COLUMN "pms"."t_pms_user"."insert_user" IS '插入人';

COMMENT ON COLUMN "pms"."t_pms_user"."update_time" IS '更新时间';

COMMENT ON COLUMN "pms"."t_pms_user"."update_user" IS '更新人';

COMMENT ON COLUMN "pms"."t_pms_user"."account" IS '账号';

COMMENT ON COLUMN "pms"."t_pms_user"."password" IS '密码';

COMMENT ON COLUMN "pms"."t_pms_user"."comp_id" IS '所属企业ID';
COMMENT ON COLUMN "pms"."t_pms_user"."dept_id" IS '所属部门ID';
COMMENT ON COLUMN "pms"."t_pms_user"."role_id" IS '所属角色ID';

COMMENT ON TABLE "pms"."t_pms_user" IS '用户表';

DROP TABLE IF EXISTS t_pms_function;
CREATE TABLE "pms"."t_pms_function" (
  "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL DEFAULT gen_random_uuid(),
  "insert_time" timestamp(6) NOT NULL,
  "insert_user" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
  "update_time" timestamp(6),
  "update_user" varchar(40) COLLATE "pg_catalog"."default",

  "name" varchar(40) COLLATE "pg_catalog"."default",
  "code" varchar(40) COLLATE "pg_catalog"."default",
  "url" varchar(255) COLLATE "pg_catalog"."default",
  "type" varchar(1) COLLATE "pg_catalog"."default",
  "group" varchar(20) COLLATE "pg_catalog"."default",
  CONSTRAINT "t_pms_function_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "pms"."t_pms_function"
  OWNER TO "postgres";

COMMENT ON COLUMN "pms"."t_pms_function"."id" IS '主键';

COMMENT ON COLUMN "pms"."t_pms_function"."insert_time" IS '插入时间';

COMMENT ON COLUMN "pms"."t_pms_function"."insert_user" IS '插入人';

COMMENT ON COLUMN "pms"."t_pms_function"."update_time" IS '更新时间';

COMMENT ON COLUMN "pms"."t_pms_function"."update_user" IS '更新人';

COMMENT ON COLUMN "pms"."t_pms_function"."name" IS '功能名称';
COMMENT ON COLUMN "pms"."t_pms_function"."code" IS '菜单名称';
COMMENT ON COLUMN "pms"."t_pms_function"."url" IS '目标路由';
COMMENT ON COLUMN "pms"."t_pms_function"."type" IS '功能类型 0-按钮；1-页面';
COMMENT ON COLUMN "pms"."t_pms_function"."group" IS '功能分组';

COMMENT ON TABLE "pms"."t_pms_function" IS '菜单按钮表';

DROP TABLE IF EXISTS t_pms_role_rel;
CREATE TABLE "pms"."t_pms_role_rel" (
  "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL DEFAULT gen_random_uuid(),
  "insert_time" timestamp(6) NOT NULL,
  "insert_user" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
  "update_time" timestamp(6),
  "update_user" varchar(40) COLLATE "pg_catalog"."default",

  "role_id" varchar(36) COLLATE "pg_catalog"."default",
  "function_id" varchar(36) COLLATE "pg_catalog"."default",

  CONSTRAINT "t_pms_role_rel_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "pms"."t_pms_role_rel"
  OWNER TO "postgres";

COMMENT ON COLUMN "pms"."t_pms_role_rel"."id" IS '主键';

COMMENT ON COLUMN "pms"."t_pms_role_rel"."insert_time" IS '插入时间';

COMMENT ON COLUMN "pms"."t_pms_role_rel"."insert_user" IS '插入人';

COMMENT ON COLUMN "pms"."t_pms_role_rel"."update_time" IS '更新时间';

COMMENT ON COLUMN "pms"."t_pms_role_rel"."update_user" IS '更新人';

COMMENT ON COLUMN "pms"."t_pms_role_rel"."role_id" IS '角色ID';
COMMENT ON COLUMN "pms"."t_pms_role_rel"."function_id" IS '功能ID';

COMMENT ON TABLE "pms"."t_pms_role_rel" IS '权限关联表';


DROP TABLE IF EXISTS t_infra_user_info;
CREATE TABLE "pms"."t_infra_user_info" (
  "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL DEFAULT gen_random_uuid(),
  "insert_time" timestamp(6) NOT NULL,
  "insert_user" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
  "update_time" timestamp(6),
  "update_user" varchar(40) COLLATE "pg_catalog"."default",
  "user_name" varchar(40) COLLATE "pg_catalog"."default",
  "password" varchar(32) COLLATE "pg_catalog"."default",
  "email" varchar(40) COLLATE "pg_catalog"."default",
  "user_type" numeric(1,0),
  CONSTRAINT "t_infra_user_info_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "pms"."t_infra_user_info"
  OWNER TO "postgres";

COMMENT ON COLUMN "pms"."t_infra_user_info"."id" IS '主键';

COMMENT ON COLUMN "pms"."t_infra_user_info"."insert_time" IS '插入时间';

COMMENT ON COLUMN "pms"."t_infra_user_info"."insert_user" IS '插入人';

COMMENT ON COLUMN "pms"."t_infra_user_info"."update_time" IS '更新时间';

COMMENT ON COLUMN "pms"."t_infra_user_info"."update_user" IS '更新人';

COMMENT ON COLUMN "pms"."t_infra_user_info"."user_name" IS '用户名称';

COMMENT ON COLUMN "pms"."t_infra_user_info"."password" IS '密码';

COMMENT ON COLUMN "pms"."t_infra_user_info"."email" IS '邮箱';

COMMENT ON COLUMN "pms"."t_infra_user_info"."user_type" IS '用户类型 0-管理员;1-会员';

COMMENT ON TABLE "pms"."t_infra_user_info" IS '用户信息表';

DROP TABLE if EXISTS t_infra_file;
CREATE TABLE "infra"."t_infra_file" (
  "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL DEFAULT gen_random_uuid(),
  "insert_time" timestamp(6) NOT NULL,
  "insert_user" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
  "update_time" timestamp(6),
  "update_user" varchar(40) COLLATE "pg_catalog"."default",

  "file_name" varchar(255) COLLATE "pg_catalog"."default",
  "suffix" varchar(10) COLLATE "pg_catalog"."default",
  "path" varchar(255) COLLATE "pg_catalog"."default",
  "access_path" varchar(255) COLLATE "pg_catalog"."default",

  CONSTRAINT "t_infra_file_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "infra"."t_infra_file"
  OWNER TO "postgres";

COMMENT ON COLUMN "infra"."t_infra_file"."id" IS '主键';

COMMENT ON COLUMN "infra"."t_infra_file"."insert_time" IS '插入时间';

COMMENT ON COLUMN "infra"."t_infra_file"."insert_user" IS '插入人';

COMMENT ON COLUMN "infra"."t_infra_file"."update_time" IS '更新时间';

COMMENT ON COLUMN "infra"."t_infra_file"."update_user" IS '更新人';

COMMENT ON COLUMN "infra"."t_infra_file"."file_name" IS '文件名';
COMMENT ON COLUMN "infra"."t_infra_file"."suffix" IS '文件后缀';
COMMENT ON COLUMN "infra"."t_infra_file"."path" IS '文件存储在服务器中的相对路径';
COMMENT ON COLUMN "infra"."t_infra_file"."access_path" IS '文件公网访问路径';

COMMENT ON TABLE "infra"."t_infra_file" IS '附件表';

DROP TABLE if EXISTS t_infra_import_config;
CREATE TABLE "infra"."t_infra_import_config" (
  "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL DEFAULT gen_random_uuid(),
  "insert_time" timestamp(6) NOT NULL,
  "insert_user" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
  "update_time" timestamp(6),
  "update_user" varchar(40) COLLATE "pg_catalog"."default",
  "system_code" varchar(20) COLLATE "pg_catalog"."default",
  "task_code" varchar(40) COLLATE "pg_catalog"."default",
  "task_desc" varchar(40) COLLATE "pg_catalog"."default",
  "mapping_json" json,
  CONSTRAINT "t_infra_import_config_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "infra"."t_infra_import_config"
  OWNER TO "postgres";

COMMENT ON COLUMN "infra"."t_infra_import_config"."id" IS '主键';

COMMENT ON COLUMN "infra"."t_infra_import_config"."insert_time" IS '插入时间';

COMMENT ON COLUMN "infra"."t_infra_import_config"."insert_user" IS '插入人';

COMMENT ON COLUMN "infra"."t_infra_import_config"."update_time" IS '更新时间';

COMMENT ON COLUMN "infra"."t_infra_import_config"."update_user" IS '更新人';

COMMENT ON COLUMN "infra"."t_infra_import_config"."system_code" IS '系统编码';

COMMENT ON COLUMN "infra"."t_infra_import_config"."task_code" IS '任务编码';

COMMENT ON COLUMN "infra"."t_infra_import_config"."task_desc" IS '任务描述';

COMMENT ON COLUMN "infra"."t_infra_import_config"."mapping_json" IS 'Excel字段和实体属性的映射关系json串';

COMMENT ON TABLE "infra"."t_infra_import_config" IS '异步导入配置表';


DROP TABLE if EXISTS t_infra_import_task;
CREATE TABLE "infra"."t_infra_import_task" (
  "id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL DEFAULT gen_random_uuid(),
  "insert_time" timestamp(6) NOT NULL,
  "insert_user" varchar(40) COLLATE "pg_catalog"."default" NOT NULL,
  "update_time" timestamp(6),
  "update_user" varchar(40) COLLATE "pg_catalog"."default",
  "system_code" varchar(20) COLLATE "pg_catalog"."default",
  "task_code" varchar(40) COLLATE "pg_catalog"."default",
  "status" varchar(1) COLLATE "pg_catalog"."default",
  "origin_file_path" varchar(255) COLLATE "pg_catalog"."default",
  "correct_file_path" varchar(255) COLLATE "pg_catalog"."default",
  "wrong_file_path" varchar(255) COLLATE "pg_catalog"."default",
  "error_message" varchar(255) COLLATE "pg_catalog"."default",

  CONSTRAINT "t_infra_import_task_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "infra"."t_infra_import_task"
  OWNER TO "postgres";

COMMENT ON COLUMN "infra"."t_infra_import_task"."id" IS '主键';

COMMENT ON COLUMN "infra"."t_infra_import_task"."insert_time" IS '插入时间';

COMMENT ON COLUMN "infra"."t_infra_import_task"."insert_user" IS '插入人';

COMMENT ON COLUMN "infra"."t_infra_import_task"."update_time" IS '更新时间';

COMMENT ON COLUMN "infra"."t_infra_import_task"."update_user" IS '更新人';

COMMENT ON COLUMN "infra"."t_infra_import_task"."system_code" IS '系统编码';

COMMENT ON COLUMN "infra"."t_infra_import_task"."task_code" IS '任务编码';
COMMENT ON COLUMN "infra"."t_infra_import_task"."status" IS '任务状态';
COMMENT ON COLUMN "infra"."t_infra_import_task"."origin_file_path" IS '原始文件在文件服务器上的相对路径';
COMMENT ON COLUMN "infra"."t_infra_import_task"."correct_file_path" IS '校验通过数据文件在文件服务器上的相对路径';
COMMENT ON COLUMN "infra"."t_infra_import_task"."wrong_file_path" IS '校验失败数据文件在文件服务器上的相对路径';
COMMENT ON COLUMN "infra"."t_infra_import_task"."error_message" IS '导入异常原因';

COMMENT ON TABLE "infra"."t_infra_import_task" IS '异步导入任务表';