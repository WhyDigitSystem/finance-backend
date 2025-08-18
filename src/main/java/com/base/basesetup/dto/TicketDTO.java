package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO {

	private Long id;

	private String subject;

	private String description;

	private String createdBy;

	//private byte[] screenShot;

	private String userName;

	private Long orgId;

	private String status;

}

