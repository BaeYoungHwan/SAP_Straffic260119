package com.mbc.sap.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mbc.sap.dto.StatusDto;

@Mapper
@Repository
public interface StatusDao {
	
	public List<StatusDto> getstatus(
			@Param("auth") String auth,
		    @Param("user_station_id") String user_station_id);
		
}
