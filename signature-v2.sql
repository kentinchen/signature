create schema if not exists signature collate utf8mb4_general_ci;

use signature;

create table if not exists apple
(
	id int auto_increment
		primary key,
	account varchar(255) null comment 'apple开发者帐号',
	count int not null comment '已有设备数量',
	p8 text not null comment '私钥',
	iss text not null comment '在Store Connect上可以点击复制 iss ID',
	kid text not null comment 'your own key ID',
	cerId varchar(255) not null comment '授权证书id',
	bundleIds varchar(255) not null comment '开发者后台的通配证书id',
	create_time timestamp default CURRENT_TIMESTAMP null comment '帐号添加时间',
	p12 varchar(255) null comment 'p12文件地址',
	is_use tinyint(1) default 1 not null comment '帐号是否可用'
)
comment '帐号';

create table if not exists comment
(
	id int auto_increment
		primary key,
	package_id int null comment 'ipaId',
	content text not null comment '评论内容',
	create_time timestamp default CURRENT_TIMESTAMP null
)
comment '评论';

create table if not exists device
(
	id int auto_increment
		primary key,
	udid varchar(255) not null comment '设备UDID',
	apple_id int not null comment '此设备所使用的帐号id',
	device_id varchar(255) not null comment '设备id',
	is_use tinyint(1) default 1 not null comment '当前设备所处帐号是否可用',
	create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
	constraint device_apple_id_fk
		foreign key (apple_id) references apple (id)
)
comment '设备';

create table if not exists user
(
	id int auto_increment comment '用户id'
		primary key,
	level int default 0 not null comment '等级 0:待管理员审核；1:超级管理员；2:普通用户',
	username varchar(50) not null comment '用户名',
	password varchar(100) not null comment '密码',
	email varchar(100) null comment '邮箱',
	create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
	constraint user_email_uindex
		unique (email),
	constraint user_username_uindex
		unique (username)
)
comment '用户表';

create table if not exists package
(
	id int auto_increment
		primary key,
	name varchar(30) not null comment '包名',
	icon varchar(255) null comment '图标',
	imgs text null comment '预览图',
	user_id int null comment '用户id',
	version varchar(30) null comment '版本',
	bundle_identifier varchar(100) not null comment '安装包id',
	link varchar(100) not null comment '下载地址',
	mobileconfig varchar(255) null comment '获取UDID证书名称',
	download_count int default 0 not null comment '已有下载次数',
	use_device int default 0 not null comment '已使用设备量',
	total_device int default 0 not null comment '总可用设备量',
	build_version varchar(30) not null comment '编译版本号',
	mini_version varchar(30) not null comment '最小支持版本',
	summary text null comment '简介',
	create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
	constraint package_url_uindex
		unique (link),
	constraint package_user_id_fk
		foreign key (user_id) references user (id)
)
comment '安装包';

create table if not exists device_package
(
	id int auto_increment
		primary key,
	package_id int not null comment 'ipaId',
	device_id int not null comment '设备id',
	is_use tinyint(1) default 1 null comment '设备是否可用',
	create_time timestamp default CURRENT_TIMESTAMP null comment '创建时间',
	constraint device_package_device_id_fk
		foreign key (device_id) references device (id),
	constraint device_package_package_id_fk
		foreign key (package_id) references package (id)
)
comment '设备与ipa关联表';

