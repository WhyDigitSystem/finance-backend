package com.base.basesetup.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Embedded;

import com.fasterxml.jackson.annotation.JsonGetter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
	private Long usersId;
	private String userName;
	private String employeeName; 
	private String employeeCode; 
	private String email; 
	private String userType;
	private Long orgId;
	private String customer;
	private String warehouse;
	private String branch;
	private String client;
	private String finYear;
	private boolean loginStatus;
	private boolean active;
	private boolean allIndiaAcces;
	private List<Map<String, Object>> roleVO;
//	private List<Map<String, Object>> responsibilityVO;
//	private List<Map<String, Object>> screensVO;
	
    @Embedded
    private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
    private Date accountRemovedDate;
    private String token;
    private String tokenId;
	

    @JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}
    // Setter method to accept List<Map<String, Object>>
   
   
}
