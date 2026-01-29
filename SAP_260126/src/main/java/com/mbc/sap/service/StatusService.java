package com.mbc.sap.service;

import java.util.List;

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
	
	public List<StatusDto> getstatus(String auth, String stationId) {
		return dao.getstatus(auth, stationId);
	}
}
