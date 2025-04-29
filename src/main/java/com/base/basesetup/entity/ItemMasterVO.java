package com.base.basesetup.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "itemmaster")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemMasterVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itemmastergen")
	@SequenceGenerator(name = "itemmastergen", sequenceName = "itemmasterseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "itemmasterid")
	private Long id;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "branch",length = 30)
	private String branch;
	@Column(name = "branchcode",length = 10)
	private String branchCode;
	@Column(name = "finyear",length =10)
	private String finYear;
	@Column(name = "createdby")
	private String createdBy;
	@Column(name = "modifiedby")
	private String modifiedBy;
	@Column(name = "active")
	private boolean active=true;
	@Column(name = "cancel")
	private boolean cancel=false;
	@Column(name = "cancelremarks")
	private String cancelRemarks;
	@Column(name = "screencode",length = 30)
	private String screenCode="ITM";
	@Column(name = "screenname",length = 30)
	private String screenName="ITEM MASTER";
	
  
	@Column(name = "custpartno")
	private String custPartNo;
	@Column(name = "dupchk")
	private String dupChk;
	@Column(name = "hsncode",length = 15)
	private String hsnCode;
	
	@Column(name = "itemtype")
	private String itemType;
	@Column(name = "partdesc")
	private String partDesc;
	@Column(name = "partno")
	private String partNo;
	@Column(name = "unit",length = 25)
	private String unit;
	@Column(name = "customer")
	private String customer;
	@Column(name = "weight")
	private BigDecimal weight;

	
	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
