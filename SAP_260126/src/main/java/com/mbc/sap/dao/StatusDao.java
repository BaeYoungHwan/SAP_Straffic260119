package com.mbc.sap.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.mbc.sap.dto.StatusDto;

@Mapper
@Repository
public interface StatusDao {
	
	List<StatusDto> getAllStations();
	
	List<StatusDto> getstatus(
	        @Param("station_id") String station_id, 
	        @Param("line_name") String line_name
	    );
	
		
}
