package com.base.basesetup.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aradjustmentoffset")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArAdjustmentOffSetVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aradjustmentoffsetgen")
	@SequenceGenerator(name = "aradjustmentoffsetgen", sequenceName = "aradjustmentoffsetseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "aradjustmentoffsetid")
	private Long id;
	
	@Column(name = "docid",length = 50)
	private String docId;
	@Column(name = "docdate")
	private LocalDate docDate=LocalDate.now();
	@Column(name = "receiptdocid",length = 50)
	private String receiptDocId;
	@Column(name = "receiptdocdate")
	private LocalDate receiptDocDate;
	@Column(name = "subledgertype",length = 30)
	private String subLedgerType;
	@Column(name = "subledgername",length = 50)
	private String subLedgerName;
	@Column(name = "subledgercode",length=20)
	private String subLedgerCode;
	
	@Column(name = "currency",length=10)
	private String currency;
	@Column(name = "exrate",precision = 10, scale = 2 )
	private BigDecimal exRate;
	@Column(name = "amount",precision = 10, scale = 2)
	private BigDecimal amount;
	@Column(name = "supplierRefNo",length = 50)
	private String supplierRefNo;
	
	//SUMMARY
	@Column(name = "forexgainorloss",precision = 10, scale = 2)
	private BigDecimal forexGainOrLoss;
	@Column(name = "totalsettled",precision = 10, scale = 2)
	private BigDecimal totalSettled;
	@Column(name = "roundoffamount",precision = 10, scale = 2)
	private BigDecimal roundOffAmount;
	@Column(name = "onaccount",precision = 10, scale = 2)
	private BigDecimal onAccount;
	@Column(name = "narration",length = 150)
	private String narration;
	
	@Column(name="approvestatus",length = 20)
	private String approveStatus;
	
	@Column(name="approveby",length = 20)
	private String approveBy;
	
	@Column(name = "status",length = 30)
	private String status;
    
	@DateTimeFormat(pattern = "dd-MM-yyyy hh:mm:ss a")
	@Column(name="approveon")
	private String approveOn;
	
	//Default Fields
	@Column(name = "screencode",length = 5)
	private String screenCode="ARA";
	@Column(name = "screenname",length = 30)
	private String screenName="ARADJUSTMENTOFFSET";
	@Column(name = "branch",length = 25)
	private String branch;
	@Column(name = "branchcode",length = 20)
	private String branchCode;
	@Column(name = "finyear",length =5)
	private String finYear;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "active")
	private boolean active;
	@Column(name = "cancel")
	private boolean cancel=false;
	@Column(name = "cancelremarks",length=50)
	private String cancelRemarks;
	@Column(name = "createdby",length = 25)
	private String createdBy;
	@Column(name = "modifiedby",length=25)
	private String updatedBy;
	
	@OneToMany(mappedBy = "arAdjustmentOffSetVO",cascade = CascadeType.ALL)
	@JsonManagedReference
	List<ArOffSetInvoiceDetailsVO> arOffSetInvoiceDetailsVO;
	
	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();


}
