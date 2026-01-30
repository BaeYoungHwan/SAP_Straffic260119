package com.mbc.sap.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mbc.sap.dao.StatusDao;
import com.mbc.sap.dto.StatusDto;

@Service
@Transactional
public class StatusService {
	
	@Autowired
	StatusDao dao;
	
	public List<StatusDto> getStatusList(Map<String, Object> params) {
        String stationId = (String) params.get("station_id");
        String lineName = (String) params.get("line_name");
        return dao.getstatus(stationId, lineName);
    }
	
	public List<StatusDto> getAllStations() {
		return dao.getAllStations();
	}
	
}
