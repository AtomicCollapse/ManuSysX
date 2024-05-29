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