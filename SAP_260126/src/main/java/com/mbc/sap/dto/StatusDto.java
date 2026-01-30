package com.mbc.sap.dto;

public class StatusDto {
	private String station_id; 			// 역 ID
	private String station_name;		// 역명
	private String line_name;			// 호선
	
	private int incident_count;			// 장애발생건수
	private int total_lockers; 			// 물품보관함 전체 설치 개수
	private int used_lockers;			// 물품보관함 실시간 사용중 갯수
	
	// 설치 유무
	private String elevator;			// 엘레베이터 
	private String wheelchairlift;		// 휠체어리프트
	private String parking;				// 환승주차장
	private String complaint;			// 무인민원발급기
	private String exchange;			// 환전키오스크
	private String trainreservation;	// 기차예매
	private String culturalspace;		// 문화공간
	private String meeting;				// 만남의장소
	private String lactation;			// 유아 수유방
	
	public StatusDto() {
		
	}

	public StatusDto(String station_id, String station_name, String line_name, int incident_count, int total_lockers,
			int used_lockers, String elevator, String wheelchairlift, String parking, String complaint, String exchange,
			String trainreservation, String culturalspace, String meeting, String lactation) {
		super();
		this.station_id = station_id;
		this.station_name = station_name;
		this.line_name = line_name;
		this.incident_count = incident_count;
		this.total_lockers = total_lockers;
		this.used_lockers = used_lockers;
		this.elevator = elevator;
		this.wheelchairlift = wheelchairlift;
		this.parking = parking;
		this.complaint = complaint;
		this.exchange = exchange;
		this.trainreservation = trainreservation;
		this.culturalspace = culturalspace;
		this.meeting = meeting;
		this.lactation = lactation;
	}

	public String getStation_id() {
		return station_id;
	}

	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	public String getStation_name() {
		return station_name;
	}

	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}

	public String getLine_name() {
		return line_name;
	}

	public void setLine_name(String line_name) {
		this.line_name = line_name;
	}

	public int getIncident_count() {
		return incident_count;
	}

	public void setIncident_count(int incident_count) {
		this.incident_count = incident_count;
	}

	public int getTotal_lockers() {
		return total_lockers;
	}

	public void setTotal_lockers(int total_lockers) {
		this.total_lockers = total_lockers;
	}

	public int getUsed_lockers() {
		return used_lockers;
	}

	public void setUsed_lockers(int used_lockers) {
		this.used_lockers = used_lockers;
	}

	public String getElevator() {
		return elevator;
	}

	public void setElevator(String elevator) {
		this.elevator = elevator;
	}

	public String getWheelchairlift() {
		return wheelchairlift;
	}

	public void setWheelchairlift(String wheelchairlift) {
		this.wheelchairlift = wheelchairlift;
	}

	public String getParking() {
		return parking;
	}

	public void setParking(String parking) {
		this.parking = parking;
	}

	public String getComplaint() {
		return complaint;
	}

	public void setComplaint(String complaint) {
		this.complaint = complaint;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getTrainreservation() {
		return trainreservation;
	}

	public void setTrainreservation(String trainreservation) {
		this.trainreservation = trainreservation;
	}

	public String getCulturalspace() {
		return culturalspace;
	}

	public void setCulturalspace(String culturalspace) {
		this.culturalspace = culturalspace;
	}

	public String getMeeting() {
		return meeting;
	}

	public void setMeeting(String meeting) {
		this.meeting = meeting;
	}

	public String getLactation() {
		return lactation;
	}

	public void setLactation(String lactation) {
		this.lactation = lactation;
	}

	@Override
	public String toString() {
		return "StatusDto [station_id=" + station_id + ", station_name=" + station_name + ", line_name=" + line_name
				+ ", incident_count=" + incident_count + ", total_lockers=" + total_lockers + ", used_lockers="
				+ used_lockers + ", elevator=" + elevator + ", wheelchairlift=" + wheelchairlift + ", parking="
				+ parking + ", complaint=" + complaint + ", exchange=" + exchange + ", trainreservation="
				+ trainreservation + ", culturalspace=" + culturalspace + ", meeting=" + meeting + ", lactation="
				+ lactation + "]";
	}
	
	

	
	
}
