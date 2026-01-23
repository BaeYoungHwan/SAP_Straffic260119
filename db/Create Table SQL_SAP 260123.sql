-- 물품보관함별 역 Table 생성
-- CREATE TABLE storage.local(
-- 	local_id 		VARCHAR(10) 	primary key,
-- 	local_name 		VARCHAR(100) 	not null,
-- 	station_id 		VARCHAR(10)		not null,
-- 	station_name 	VARCHAR(100)	not null,
-- 	line_name 		VARCHAR(10)		not null,
-- 	created_at 		TIMESTAMP 		DEFAULT CURRENT_TIMESTAMP

--);

-- 사용현황 테이블 생성
-- CREATE TABLE storage.locker_usage (
--     usage_id 		SERIAL 			PRIMARY KEY,
--     station_name 	VARCHAR(100) 	NOT NULL,
-- 	local_id 		VARCHAR(10)		NOT NULL,
-- 	local_name 		VARCHAR(100)	not null,
-- 	able_small		int,
-- 	able_middle		int,
-- 	able_large		int,
--     observed_at 	TIMESTAMP NOT NULL,
--     created_at 		TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
-- 	FOREIGN KEY (local_id) REFERENCES storage.local(local_id)
-- );

-- 역별 물품보관함 설치 수량 테이블 생성
-- CREATE TABLE storage.locker_inventory (
--     inventory_id 	SERIAL		 	PRIMARY KEY,
--     line_name 		VARCHAR(10)		not null,
-- 	local_id 		VARCHAR(10) 	not null,  
--     local_name 		VARCHAR(100)	not null,
--     local_small		int,
-- 	local_middle	int,
-- 	local_large		int,
--     created_at 		TIMESTAMP 		DEFAULT CURRENT_TIMESTAMP,
-- 	FOREIGN KEY (local_id) REFERENCES storage.local(local_id)
	
-- );

-- 편의시설 테이블 생성
-- CREATE TABLE storage.local_Amenities(
-- 	station_id 			VARCHAR(10)		primary key,
-- 	station_name 		VARCHAR(100)	not null,
-- 	line_name 			VARCHAR(10)		not null,
-- 	elevator 			VARCHAR(10)		not null,
-- 	wheelchairlift 		VARCHAR(10)		not null,
-- 	parking 			VARCHAR(10)		not null,
-- 	complaint 			VARCHAR(10)		not null,
-- 	exchange 			VARCHAR(10)		not null,
-- 	trainreservation 	VARCHAR(10)		not null,
-- 	culturalspace 		VARCHAR(10)		not null,
-- 	meeting 			VARCHAR(10)		not null,
-- 	lactation 			VARCHAR(10)		not null,
-- 	created_at 			TIMESTAMP 		DEFAULT CURRENT_TIMESTAMP
-- );

-- drop table storage.local_amenities;

select * from storage.local_amenities;

select * from storage.locker_usage order by locker_usage.created_at desc

-- 유저 테이블 생성
-- CREATE TABLE storage.user(
-- 	user_id				VARCHAR(20)		primary key,
-- 	user_pw				VARCHAR(30)		not null,
-- 	station_id 			VARCHAR(10)		not null,
-- 	station_name 		VARCHAR(100)	not null,
-- 	line_name 			VARCHAR(10)		not null,
-- 	user_name			VARCHAR(10)		not null,
-- 	user_email			VARCHAR(50),
-- 	auth				VARCHAR(10)		not null
	
-- );

-- drop table storage.user;

select * from storage.user;
select * from storage.insident_comment;

-- 장애/발생 테이블 생성
-- Create Table storage.insident(

-- 	insident_id				SERIAL		 	PRIMARY KEY,
-- 	insident_title			VARCHAR(30)		not null,
-- 	insident_line_name		VARCHAR(10),
-- 	insident_station_id 	VARCHAR(10)		not null,
-- 	insident_station_name	VARCHAR(100),
-- 	insident_content		VARCHAR(1000),
-- 	insident_status			VARCHAR(10)		not null,
-- 	user_id					VARCHAR(20)		not null,
-- 	user_name				VARCHAR(10),
-- 	create_at				TIMESTAMP 		DEFAULT CURRENT_TIMESTAMP,
-- 	complete_at				TIMESTAMP,
-- 	FOREIGN KEY (user_id) REFERENCES storage.user(user_id)
	
-- );


-- 댓글 테이블 생성
-- Create Table storage.insident_comment(

-- 	comment_id				SERIAL		 	PRIMARY KEY,
-- 	insident_id				int				not null,
-- 	comment_content			VARCHAR(1000)	not null,
-- 	user_id					VARCHAR(20)		not null,
-- 	user_name				VARCHAR(10),
-- 	create_at				TIMESTAMP 		DEFAULT CURRENT_TIMESTAMP,
-- 	FOREIGN KEY (user_id) REFERENCES storage.user(user_id),
-- 	FOREIGN KEY (insident_id) REFERENCES storage.insident(insident_id)
	
-- );





