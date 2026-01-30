package com.mbc.sap.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mbc.sap.dto.StatusDto;
import com.mbc.sap.service.StatusService;

@RestController
public class StatusController {
	
	@Autowired
	StatusService service;
	
	// 검색시 해당 역의 대한 현황 List로 불러오기
	@GetMapping("get_status")
    public ResponseEntity<List<StatusDto>> getStatus(@RequestParam Map<String, Object> params) {
        // System.out.println("조회 요청 파라미터: " + params);

        List<StatusDto> list = service.getStatusList(params);
        
        return ResponseEntity.ok(list);
    }
	
	
	// DB에 저장된 모든역의 ID,이름,호선을 검색에 이용하기 위해 가져오기
	@GetMapping("get_allstations")
	public ResponseEntity<List<StatusDto>> getAllStations() {
		// System.out.println("StatusController getallstations 실행: " + new Date());
		List<StatusDto> list = service.getAllStations();
		
		return ResponseEntity.ok(list);
	}
}
