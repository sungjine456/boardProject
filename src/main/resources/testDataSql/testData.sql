delete from board_like;
delete from comment;
delete from auto_login;
delete from board;
delete from user;
insert into user values ('1', 'sungjin@naver.com', 'sungjin', '홍길동', '96cae35ce8a9b0244178bf28e4966c2ce1b8385723a96a6b838858cdd6ca0a1epersonProject', 'img/user/default.png', '2016-2-2', '2016-2-2');
insert into user values ('2', 'sungjine@naver.com', 'sungjine', 'sungjine', '96cae35ce8a9b0244178bf28e4966c2ce1b8385723a96a6b838858cdd6ca0a1epersonProject', 'img/user/default.png', '2016-2-2', '2016-2-2');
insert into board values ('1', 'title', 'content', '0', '1', '2016-2-2', '2016-2-2');
insert into auto_login values ('1', '1', '2016-7-24', 'asdasdasd');
insert into auto_login values ('2', '1', '2016-7-24', 'asdasdasdasdasd');
insert into comment values ('1', 'comment1', '1', '0', '0', '1' , '1', '2016-2-2', '2016-2-2');
insert into comment values ('2', 'comment2', '1', '1', '1', '1' , '1', '2016-2-2', '2016-2-2');
insert into comment values ('3', 'comment3', '1', '2', '1', '1' , '1', '2016-2-2', '2016-2-2');
insert into comment values ('4', 'comment4', '1', '3', '2', '1' , '1', '2016-2-2', '2016-2-2');
insert into comment values ('5', 'comment5', '5', '0', '0', '1' , '1', '2016-2-2', '2016-2-2');
insert into comment values ('6', 'comment6', '1', '4', '1', '1' , '1', '2016-2-2', '2016-2-2');
insert into comment values ('7', 'comment7', '1', '5', '1', '1' , '1', '2016-2-2', '2016-2-2');
insert into comment values ('8', 'comment8', '5', '1', '1', '1' , '1', '2016-2-2', '2016-2-2');
insert into board_like values ('1', '1', '2');