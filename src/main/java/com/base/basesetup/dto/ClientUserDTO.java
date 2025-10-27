package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor 

public class ClientUserDTO {

	private Long id;
	private String clientUser;
	private String clientUserCode;
	private String clintUserAdminName;
	private String clientUserType;
	private String clientUserMail;
	private String phoneNo;
	private String conntactPerson;
	private String userName;
	private String password;
	private boolean active; 
	private boolean cancel;
	private Long orgId;
	private String createdBy;
	private Role role;
	private String clintAdminName;

}

