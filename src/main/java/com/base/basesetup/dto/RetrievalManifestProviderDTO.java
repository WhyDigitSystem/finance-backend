package com.base.basesetup.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetrievalManifestProviderDTO {

	private Long id;
	
	private String finYear;

	private String transactionNo;

	private LocalDate transactionDate;

	private LocalDate dispatchDate;

	private String transactionType;

	private String sender;

	private String senderAddress;

	private String receiver;

	private String receiverAddress;

	private String senderGst;

	private String transporterName;

	private String vechileNo;

	private String driverPhoneNo;

	private boolean active;

	private boolean cancel;

	private String createdBy;

	private Long orgId;
	
	private List<RetrievalManifestProviderDetailsDTO> retrievalManifestProviderDetailsDTO;

}

