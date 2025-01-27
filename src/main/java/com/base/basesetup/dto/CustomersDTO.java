package com.base.basesetup.dto;

import java.math.BigDecimal;
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
	private String gstIn;
	private String panNo;
	private boolean active;
	private String createdBy;
	private Long creditDays;
	private BigDecimal creditLimit;
	private String creditTerms;
	private String taxRegistered;
	private String bussinessType;
	private String bussinessCategory;
	private String accountsType;
	private boolean approved;
	
	private List<CustomersStateDTO> customersStateDTO;
	private List<CustomersAddressDTO> customersAddressDTO;
	private List<CustomerSalesPersonDTO> customerSalesPersonDTO;
	private List<CustomerCurrencyMappingDTO> customerCurrencyMappingDTO;
	
	

}
