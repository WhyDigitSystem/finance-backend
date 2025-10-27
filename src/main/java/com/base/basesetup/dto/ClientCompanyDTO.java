package com.base.basesetup.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientCompanyDTO {

	private Long id;
	private Long orgId;
	private String clientName;
	private String clientCode;
	private String phone;
	private String email;
	private String webSite;
	private String createdBy;
	private boolean active;
	private String bussinessType;
	private String clientGLCode;
	private String clientYear;
	private Long startNo;
	private String turnOver;
	private String levelOfService;
	private String repPerson;
	private String currency;
	private LocalDate yearStartDate;
	private LocalDate yearEndDate;
	private String userName;
	private String password="HzNOsmwTefzQ4WWqCURfjDYdOzoyMT4nlgQk6p77fso=";
	
	private List<ClientCompanyReportAccessDTO> clientCompanyReportAccessDTO;
	
	private List<ClientUnitDTO> clientUnitDTO;
	
	private List<ClientSegmentDTO>clientSegmentDTO;

	
}
