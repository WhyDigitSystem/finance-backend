package com.base.basesetup.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomersDTO {
	
	private Long id;
	private long orgId;
	private String customerName;
	private String customerCode;
	private String gstIn;
	private String panNo;
	private boolean active;
	private String createdBy;
	
	private List<CustomersStateDTO> customersStateDTO;
	private List<CustomersAddressDTO> customersAddressDTO;
	private List<CustomerSalesPersonDTO> customerSalesPersonDTO;
	
	

}
