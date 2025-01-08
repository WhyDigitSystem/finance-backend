package com.base.basesetup.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "partymaster")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyMasterVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partymastergen")
	@SequenceGenerator(name = "partymastergen", sequenceName = "partymasterseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "partymasterid")
	private Long id;
	    
    @Column(name = "partytype", length = 15)
    private String partyType;

    @Column(name = "partycode", length = 20)
    private String partyCode;

    @Column(name = "partyname", length = 150)
    private String partyName;

    @Column(name = "gstpartyname", length = 150)
    private String gstPartyName;

    @Column(name = "customertype", length = 50)
    private String customerType;

    @Column(name = "company", length = 150)
    private String company;

    @Column(name = "agentname", length = 150)
    private String agentName;

    @Column(name = "accounttype", length = 15)
    private String accountType;

    @Column(name = "bussinesstype", length = 15)
    private String bussinessType;

    @Column(name = "carriercode", length = 25)
    private String carrierCode;

    @Column(name = "suppliertype", length = 15)
    private String supplierType;

    @Column(name = "salesperson", length = 25)
    private String salesPerson;

    @Column(name = "customercoord", length = 25)
    private String customerCoord;

    @Column(name = "accountname", length = 50)
    private String accountName;

    @Column(name = "gstregistered", length = 5)
    private String gstRegistered;

    @Column(name = "gstin", length = 15)
    private String gstIn;

    @Column(name = "creditlimit", precision = 10, scale = 2)
    private BigDecimal creditLimit;

    @Column(name = "creditdays", precision = 5)
    private Long creditDays;

    @Column(name = "panno", length = 15)
    private String panNo;

    @Column(name = "controllingoff", length = 50)
    private String controllingOff;

    @Column(name = "currency", length = 10)
    private String currency;

    @Column(name = "panname", length = 50)
    private String panName;

    @Column(name = "airwaybillno", length = 30)
    private String airwayBillNo;

    @Column(name = "airlinecode", length = 30)
    private String airLineCode;

    @Column(name = "tanno", length = 15)
    private String tanNo;

    @Column(name = "bussinesscate", length = 30)
    private String bussinessCate;

    @Column(name = "country", length = 15)
    private String country;

    @Column(name = "caf", length = 10)
    private String caf;

    @Column(name = "remarks", length = 150)
    private String remarks;

    @Column(name = "compoundscheme", length = 5)
    private String compoundScheme;

    @Column(name = "psugovorg", length = 5)
    private String psuGovOrg;

    @Column(name = "nameofbank", length = 150)
    private String nameOfBank;

    @Column(name = "addressbank", length = 150)
    private String addressBank;

    @Column(name = "accountno", length = 25)
    private String accountNo;

    @Column(name = "acctype", length = 15)
    private String accType;

    @Column(name = "ifsccode", length = 15)
    private String IfscCode;

    @Column(name = "swift", length = 15)
    private String Swift;

    // Additional fields with column mappings
    @Column(name = "branch", length = 25)
    private String branch;

    @Column(name = "branchcode", length = 20)
    private String branchCode;

    @Column(name = "createdby", length = 25)
    private String createdBy;

    @Column(name = "modifyby", length = 25)
    private String updatedBy;

    @Column(name = "active")
    private boolean active;

    @Column(name = "cancel")
    private boolean cancel;

    @Column(name = "cancelremarks", length = 50)
    private String cancelRemarks;

    @Column(name = "finyear", length = 5)
    private String finYear;

    @Column(name = "screencode", length = 5)
    private String screenCode = "PM";

    @Column(name = "screenname", length = 25)
    private String screenName = "PARTYMASTER";

    @Column(name = "orgid")
    private Long orgId;
	
	@OneToMany(mappedBy = "partyMasterVO",cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<PartyStateVO> partyStateVO;
	

	@OneToMany(mappedBy = "partyMasterVO",cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<PartyAddressVO> partyAddressVO;
	
	@OneToMany(mappedBy = "partyMasterVO",cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<PartyDetailsOfDirectorsVO> partyDetailsOfDirectorsVO;
	
	@OneToMany(mappedBy = "partyMasterVO",cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<PartySpecialTDSVO> partySpecialTDSVO;
	
	@OneToMany(mappedBy = "partyMasterVO",cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<PartyChargesExemptionVO> partyChargesExemptionVO;
	
	@OneToMany(mappedBy = "partyMasterVO",cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<PartyCurrencyMappingVO> partyCurrencyMappingVO;
	
	@OneToMany(mappedBy = "partyMasterVO",cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<PartySalesPersonTaggingVO> partySalesPersonTaggingVO;
	
	@OneToMany(mappedBy = "partyMasterVO",cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<PartyTdsExemptedVO> partyTdsExemptedVO;
	
	@OneToMany(mappedBy = "partyMasterVO",cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<PartyPartnerTaggingVO> partyPartnerTaggingVO;
	
	@OneToOne(mappedBy = "partyMasterVO",cascade = CascadeType.ALL,orphanRemoval = true)
	@JsonManagedReference
	private PartyVendorEvaluationVO partyVendorEvaluationVO;
	
	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
	
	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}
}
