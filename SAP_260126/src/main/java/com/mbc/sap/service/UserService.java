package com.mbc.sap.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbc.sap.dao.UserDao;
import com.mbc.sap.dto.UserDto;

@Service
@Transactional
public class UserService {
	
	@Autowired
	UserDao dao;
	
	public UserDto login(UserDto dto) {
		return dao.login(dto);
	}
	
	public boolean idcheck(String user_id) {
		return dao.idcheck(user_id)>0;
	}
	
	public boolean adduser(UserDto dto) {
		return dao.adduser(dto)>0;
	}
	
	public List<String> search_stationid(UserDto dto) {
		return dao.search_stationid(dto);
	}
	
	public List<Map<String, Object>> getstationname(String line_name){
		return dao.getstationname(line_name);
	}

	public boolean update_user(UserDto dto) {

		return dao.update_user(dto)>0;
	}
}
