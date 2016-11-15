delete from board_like;
delete from comment;
delete from auto_login;
delete from board;
delete from user;
insert into user (user_idx, id, name, email, password, img, access, admin_yn, reg_date, up_date) values ('1', 'sungjin', 'hong', 'sungjin@naver.com', '96cae35ce8a9b0244178bf28e4966c2ce1b8385723a96a6b838858cdd6ca0a1epersonProject', '/img/user/default.png', 'Y', 'N', '2016-2-2', '2016-2-2');
insert into user (user_idx, id, name, email, password, img, access, admin_yn, reg_date, up_date) values ('2', 'sungjine', 'sungjine', 'sungjine@naver.com', '96cae35ce8a9b0244178bf28e4966c2ce1b8385723a96a6b838858cdd6ca0a1epersonProject', '/img/user/default.png', 'N', 'N', '2016-2-2', '2016-2-2');
insert into user (user_idx, id, name, email, password, img, access, admin_yn, reg_date, up_date) values ('3', 'admin', 'admin', 'admin@naver.com', '96cae35ce8a9b0244178bf28e4966c2ce1b8385723a96a6b838858cdd6ca0a1epersonProject', '/img/user/default.png', 'Y', 'Y', '2016-2-2', '2016-2-2');
insert into board (board_idx, title, content, hit_count, writer, reg_date, up_date) values ('1', 'title', 'content', '0', '1', '2016-2-2', '2016-2-2');
insert into auto_login (auto_login_idx, user_idx, reg_date, auto_login_id) values ('1', '1', '2016-7-24', 'asdasdasd');
insert into auto_login (auto_login_idx, user_idx, reg_date, auto_login_id) values ('2', '1', '2016-7-24', 'asdasdasdasdasd');
insert into comment (comment_idx, comment, circle, step, depth, board, writer, reg_date, up_date) values ('1', 'comment1', '1', '0', '0', '1' , '1', '2016-2-2', '2016-2-2');
insert into comment (comment_idx, comment, circle, step, depth, board, writer, reg_date, up_date) values ('2', 'comment2', '1', '1', '1', '1' , '1', '2016-2-2', '2016-2-2');
insert into comment (comment_idx, comment, circle, step, depth, board, writer, reg_date, up_date) values ('3', 'comment3', '1', '2', '1', '1' , '1', '2016-2-2', '2016-2-2');
insert into comment (comment_idx, comment, circle, step, depth, board, writer, reg_date, up_date) values ('4', 'comment4', '1', '3', '2', '1' , '1', '2016-2-2', '2016-2-2');
insert into comment (comment_idx, comment, circle, step, depth, board, writer, reg_date, up_date) values ('5', 'comment5', '5', '0', '0', '1' , '1', '2016-2-2', '2016-2-2');
insert into comment (comment_idx, comment, circle, step, depth, board, writer, reg_date, up_date) values ('6', 'comment6', '1', '4', '1', '1' , '1', '2016-2-2', '2016-2-2');
insert into comment (comment_idx, comment, circle, step, depth, board, writer, reg_date, up_date) values ('7', 'comment7', '1', '5', '1', '1' , '1', '2016-2-2', '2016-2-2');
insert into comment (comment_idx, comment, circle, step, depth, board, writer, reg_date, up_date) values ('8', 'comment8', '5', '1', '1', '1' , '1', '2016-2-2', '2016-2-2');
insert into board_like (idx, board_idx, user_idx) values ('1', '1', '2');
ALTER TABLE user AUTO_INCREMENT=4;
ALTER TABLE board AUTO_INCREMENT=2;
ALTER TABLE auto_login AUTO_INCREMENT=3;
ALTER TABLE comment AUTO_INCREMENT=9;
ALTER TABLE board_like AUTO_INCREMENT=2;