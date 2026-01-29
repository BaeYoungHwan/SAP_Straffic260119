package com.mbc.sap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mbc.sap.dto.StatusDto;
import com.mbc.sap.service.StatusService;

@RestController
public class StatusController {
	
	@Autowired
	StatusService service;
	
	@GetMapping("getstatus")
	public ResponseEntity<List<StatusDto>> getstatus(
			@RequestParam("auth") String auth,
			@RequestParam("station_id") String station_id){
		
		List<StatusDto> list = service.getstatus(auth, station_id);
		
		return ResponseEntity.ok(list);
	}
}
