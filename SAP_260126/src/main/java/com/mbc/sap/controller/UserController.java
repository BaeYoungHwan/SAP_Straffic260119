package com.mbc.sap.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mbc.sap.dto.UserDto;
import com.mbc.sap.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
public class UserController {

	@Autowired
	UserService service;
	
	// 유저테이블에서 로그인을 위한 ID, PW 체크 기능
	@PostMapping("check_login") // 기능_기능설명
	public UserDto login(UserDto dto, HttpSession session) {
		// System.out.println("UserController login " + new Date());
		// System.out.println("전달된 데이터: " + dto.toString()); // 데이터 유입 확인용
		
		UserDto user = service.login(dto);
		System.out.println(service.login(dto));
		System.out.println(user);
		if(user != null) {
			session.setAttribute("login", user);
		}
		
		return user;
	}
	
	// 회원가입시 user_id 중복확인 
	@PostMapping("check_userid")
	public String check_id(String user_id) {
		// System.out.println("UserController check_id " + new Date());
		
		boolean isS = service.idcheck(user_id);
		String message = "YES";
		
		if(isS) {
			message = "NO";
		}
		return message;
	}
	
	// 회원가입 성공시
	@PostMapping("add_regi")
	public String add_regi(UserDto dto) {
		// System.out.println("UserController add_regi " + new Date());
		
		boolean isS = service.adduser(dto);
		String message = "YES";
		
		if(!isS) {
			message = "NO";
		}
		return message;
	}
	
	// 회원가입시 station_name을 입력하면 station_id를 불러오기
	@PostMapping("search_stationid")
	public List<String> search_stationid(UserDto dto){
		// System.out.println("UserController search_stationid " + new Date());
	
		return service.search_stationid(dto);
	}
	
	// 몇호선을 선택하면 해당 호선의 역이 출력
	@GetMapping("search_stationname")
	public List<Map<String, Object>> search_stationName(@RequestParam("line_name") String line_name){
		// System.out.println("UserController search_station " + new Date());
		
		return service.getstationname(line_name);
	}
    @PostMapping("update_user") 
    public String update_user(@RequestBody UserDto dto) {
        
        System.out.println("전달받은 데이터 확인: " + dto.toString()); 
        
        boolean isS = service.update_user(dto);
        
        if(isS) {
            return "YES";
        } else {
            return "NO";
        }
    }
	
}
