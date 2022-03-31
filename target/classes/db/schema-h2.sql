DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS game;
create table user
(
    id         bigint not null comment '主键ID' primary key,
    name       varchar(30) null comment '姓名',
    age        int null comment '年龄',
    email      varchar(50) null comment '邮箱',
    start_time varchar(13) default '' null comment '开始时间'
);

create table game
(
    id        int auto_increment primary key,
    user_id   int          default 0  not null comment 'user_id 外键',
    game_name varchar(500) default '' not null comment '游戏名称',
    constraint id unique (id)
);




