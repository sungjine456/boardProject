delete from comment;
delete from auto_login;
delete from board;
delete from user;
insert into user values ('1', 'sungjin@naver.com', 'sungjin', '홍길동', '96cae35ce8a9b0244178bf28e4966c2ce1b8385723a96a6b838858cdd6ca0a1epersonProject', 'img/user/default.png', '2016-2-2', '2016-2-2');
insert into board values ('1', 'title', 'content', '1', '2016-2-2', '2016-2-2');
insert into auto_login values ('1', '1', '2016-7-24', 'asdasdasd');
insert into auto_login values ('2', '1', '2016-7-24', 'asdasdasdasdasd');
insert into comment values ('1', 'comment', '0', '0', '0', '0', '1' , '1', '2016-2-2', '2016-2-2');