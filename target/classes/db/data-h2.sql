DELETE FROM user;
DELETE FROM game;

INSERT INTO game (id, user_id, game_name)
VALUES (1, 0, '王者荣耀');
INSERT INTO game (id, user_id, game_name)
VALUES (2, 1, '吃鸡');
INSERT INTO game (id, user_id, game_name)
VALUES (3, 0, '绝地求生');


INSERT INTO user (id, name, age, email, start_time) VALUES (0, 'yuhao', 25, 'danyuhao@qq.com', '1648535477000');
INSERT INTO user (id, name, age, email, start_time) VALUES (1, 'lvjin', 26, 'lvjin@qq.com', '1648535477000');
INSERT INTO user (id, name, age, email, start_time) VALUES (2, 'zhangshan', 22, 'zhangshan@qq.com', '1648449077000');
INSERT INTO user (id, name, age, email, start_time) VALUES (3, 'yuhao', 23, 'yuhao@qq.com', '1648535477000');
INSERT INTO user (id, name, age, email, start_time) VALUES (4, 'yuhao', 23, 'yuhao@qq.com', '1647498677000');

