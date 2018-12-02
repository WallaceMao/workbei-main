# 增加uuid
ALTER TABLE `team` ADD COLUMN `uuid` varchar(128) NULL AFTER `id`;
ALTER TABLE `account` ADD COLUMN `uuid` varchar(128) NULL AFTER `id`;
ALTER TABLE `department` ADD COLUMN `uuid` varchar(128) NULL AFTER `id`;

# 增加唯一约束，如果已经存在，需要做修改
ALTER TABLE `team` ADD UNIQUE INDEX `uq_team`(`uuid`) USING BTREE;
ALTER TABLE `account` ADD UNIQUE INDEX `uq_account`(`uuid`) USING BTREE;
ALTER TABLE `department` ADD UNIQUE INDEX `uq_department`(`uuid`) USING BTREE;
ALTER TABLE `team_data` ADD UNIQUE INDEX `uq_team_data`(`team_id`) USING BTREE;
ALTER TABLE `team_user` ADD UNIQUE INDEX `uq_team_user`(`team_id`) USING BTREE;
ALTER TABLE `team_user_role` ADD UNIQUE INDEX `uq_team_user_role`(`team_id`, `user_id`, `role`) USING BTREE;
ALTER TABLE `user_dept` ADD UNIQUE INDEX `uq_user_dept`(`department_id`, `user_id`) USING BTREE;
ALTER TABLE `user_dept_ascription` ADD UNIQUE INDEX `uq_user_dept_ascription`(`department_id`, `user_id`) USING BTREE;
ALTER TABLE `user_display_order` ADD UNIQUE INDEX `uq_user_display_order`(`user_id`) USING BTREE;
ALTER TABLE `user_function_setting` ADD UNIQUE INDEX `uq_user_function_setting`(`user_id`) USING BTREE;
ALTER TABLE `user_guide` ADD UNIQUE INDEX `uq_user_guide`(`user_id`) USING BTREE;
ALTER TABLE `user_oauth` ADD UNIQUE INDEX `uq_user_oauth`(`account_id`) USING BTREE;
ALTER TABLE `user_register` ADD UNIQUE INDEX `uq_user_register`(`account_id`) USING BTREE;
ALTER TABLE `user_ui_setting` ADD UNIQUE INDEX `uq_user_ui_setting`(`user_id`) USING BTREE;

#  修改user表的username为128位
ALTER TABLE `user` MODIFY COLUMN `username` varchar(128);