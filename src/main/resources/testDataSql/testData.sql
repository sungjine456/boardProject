delete from comment;
delete from auto_login;
delete from board;
delete from user;
insert into user values ('1', 'sungjin@naver.com', 'sungjin', '홍길동', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92personProject', 'img/user/default.png', '2016-2-2', '2016-2-2');
insert into board values ('1', 'title', 'content', '1', '2016-2-2', '2016-2-2');
insert into auto_login values ('1', '192.168.0.1', '2016-2-2', 'O');
insert into comment values ('1', 'comment', '0', '0', '0', '1' , '1', '2016-2-2', '2016-2-2');