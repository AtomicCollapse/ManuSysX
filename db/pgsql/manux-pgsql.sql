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
  "password" varchar(30) COLLATE "pg_catalog"."default",
  "email" varchar(40) COLLATE "pg_catalog"."default",
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

COMMENT ON TABLE "pms"."t_infra_user_info" IS '用户信息表';