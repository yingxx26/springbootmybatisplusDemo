create table student(
	id int(11) primary key auto_increment,
	sname varchar(36) default null comment'姓名',
	sno varchar(100) default null comment'学号',
	sex tinyint(2) default null comment'性别,0男，1女',
	hobby varchar(100) default null comment'爱好',
	email varchar(100) default null comment'邮箱'
 );

 insert into student values(null,'小明','10001',0,'打篮球','xiaoming@163.com');
 insert into student values(null,'小红','10002',1,'打羽毛球','xiaohong@163.com');
 insert into student values(null,'小白','10003',0,'打篮球','xiaobai@163.com');
 insert into student values(null,'小黑','10004',0,'打保龄球','xiaohei@163.com');
 insert into student values(null,'小胖','10005',0,'打乒乓球','xiaopang@163.com');
 insert into student values(null,'小新','10001',1,'打高尔夫球','xiaoxing@163.com');

 create table teacher(
	id int(11) primary key auto_increment,
	tname varchar(36) default null comment'姓名',
	tno varchar(100) default null comment'工号',
	sex tinyint(2) default null comment'性别',
  cource varchar(36) default null comment'教的课程'
 );

  insert into teacher values(null,'李小刚','t001',0,'体育');
	insert into teacher values(null,'王春心','t002',1,'语文');
	insert into teacher values(null,'李明','t003',0,'数学');
	insert into teacher values(null,'小黑','t004',0,'物理');
