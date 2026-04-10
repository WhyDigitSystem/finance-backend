package com.base.basesetup.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueManifestProviderDTO {

	private Long id;
	
	private String finYear;
	
	private String branch;

	private LocalDate dispatchDate;

	private String transactionType;
	
	private String fromWarehouse;

	private String warehouseAddress;

	private String sender;

	private String senderAddress;

	private String receiver;

	private String receiverAddress;

	private String receiverGst;

	private String amountInWords;

	private Long amount;

	private String transporterName;

	private String vehicleNo;

	private String driverPhoneNo;

	private boolean active;

	private boolean cancel;

	private String createdBy;
	
	private String receiverName;

	private Long orgId;

	private String locationUnit;
	
	private String branchCode;
	
	private LocalDate transactionDate;

	private String refNo;
	
	private List<IssueManifestProviderDetailsDTO> issueManifestProviderDetailsDTO;
	

}

