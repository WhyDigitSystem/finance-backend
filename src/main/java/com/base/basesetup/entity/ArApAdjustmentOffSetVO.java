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

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "arapadjustmentoffset")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArApAdjustmentOffSetVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "arapadjustmentoffsetgen")
	@SequenceGenerator(name = "arapadjustmentoffsetgen", sequenceName = "arapadjustmentoffsetseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "arapadjustmentoffsetid")
	private Long id;
	@Column(name = "docid",length = 50)
	private String docId;
	@Column(name = "docdate")
	private LocalDate docDate=LocalDate.now();
	@Column(name = "subledgertype",length = 30)
	private String subLedgerType;
	@Column(name = "subledgername",length = 50)
	private String subLedgerName;
	@Column(name = "subledgercode",length=20)
	private String subLedgerCode;
	@Column(name = "receiptpaymentdocid",length = 50)
	private String receiptPaymentDocId;
	@Column(name = "receiptpaymentdocdate")
	private LocalDate receiptPaymentDocDate;
	@Column(name = "currency",length=10)
	private String currency;
	@Column(name = "exrate",precision = 10, scale = 2 )
	private BigDecimal exRate;
	@Column(name = "amount",precision = 10, scale = 2)
	private BigDecimal amount;
	@Column(name = "supplierRefNo",length = 50)
	private String supplierRefNo;
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
	@Column(name = "screencode",length = 5)
	private String screenCode="OFS";
	@Column(name = "screenname",length = 30)
	private String screenName="ARAP ADJUSTMENTOFFSET";
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
	private boolean cancel;
	@Column(name = "cancelremarks",length=50)
	private String cancelRemarks;
	@Column(name = "createdby",length = 25)
	private String createdBy;
	@Column(name = "modifiedby",length=25)
	private String updatedBy;
	
	@OneToMany(mappedBy = "arApAdjustmentOffSetVO",cascade = CascadeType.ALL)
	@JsonManagedReference
	List<ArApOffSetInvoiceDetailsVO> arApAdjustmentInvoiceDetailsVO;
	
	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
