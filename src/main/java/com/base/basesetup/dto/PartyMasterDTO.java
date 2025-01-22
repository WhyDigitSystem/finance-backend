package com.base.basesetup.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyMasterDTO {
	private Long id;
	private String partyType;
	private String partyName;
	private String gstPartyName;
	private String customerType;
	private String company;
	private String agentName;
	private String accountType;
	private String bussinessType;
	private String carrierCode;
	private String supplierType;
	private String salesPerson;
	private String customerCoord;
	private String accountName;
	private String gstRegistered;
	private String gstIn;
	private BigDecimal creditLimit;
	private Long creditDays;
	private String panNo;
	private String controllingOff;
	private String currency;
	private String panName;
	private String airwayBillNo;
	private String airLineCode;
	private String tanNo;
	private String bussinessCate;
	private String country;
	private String caf;
	private String remarks;
	private String compoundScheme;
	private String psuGovOrg;
	private String nameOfBank;
	private String addressBank;
	private String accountNo;
	private String accType;
	private String IfscCode;
	private String Swift;
	// Additional fields
	private String branch;
	private String branchCode;
	private String createdBy;
	private boolean active;
	private boolean cancel;
	private String cancelRemarks;
	private String finYear;
	private Long orgId;
	
	private String creditTerms;
    
    List<PartyStateDTO> partyStateDTO;
    List<PartyAddressDTO> partyAddressDTO;
    List<PartyDetailsOfDirectorsDTO> partyDetailsOfDirectorsDTO;
    List<PartySpecialTDSDTO> partySpecialTDSDTO;
    List<PartyChargesExemptionDTO> partyChargesExemptionDTO;
    List<PartyCurrencyMappingDTO> partyCurrencyMappingDTO;
    List<PartySalesPersonTaggingDTO> partySalesPersonTaggingDTO;
    List<PartyTdsExemptedDTO> partyTdsExemptedDTO;
    List<PartyPartnerTaggingDTO> partyPartnerTaggingDTO;
    PartyVendorEvaluationDTO partyVendorEvaluationDTO;








}