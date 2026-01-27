package com.mbc.sap.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.mbc.sap.dto.UserDto;

@Mapper
@Repository
public interface UserDao {
	UserDto login(UserDto dto);
	int idcheck(String user_id);
	int adduser(UserDto dto);
	List<String> search_stationid(UserDto dto);
	List<Map<String, Object>> getstationname(String line_name);
	
}
