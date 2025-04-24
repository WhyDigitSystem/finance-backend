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
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "receipt")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "receiptgen")
	@SequenceGenerator(name = "receiptgen", sequenceName = "receiptseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "receiptid")
	private Long id;

	@Column(name = "taxamt", length = 30)
	private BigDecimal taxAmt;

	@Column(name = "branch", length = 25)
	private String branch;

	@Column(name = "branchcode", length = 20)
	private String branchCode;

	@Column(name = "customer", length = 50)
	private String customer;

	@Column(name = "client", length = 50)
	private String client;

	@Column(name = "createdby", length = 25)
	private String createdBy;

	@Column(name = "modifyby", length = 25)
	private String updatedBy;

	@Column(name = "active")
	private boolean active;

	@Column(name = "cancel")
	private boolean cancel;

	@Column(name = "cancelremarks", length = 25)
	private String cancelRemarks;

	@Column(name = "finyear", length = 5)
	private String finYear;

	@Column(name = "screencode", length = 5)
	@Builder.Default
	private String screenCode = "RT";

	@Column(name = "screenname", length = 25)
	@Builder.Default
	private String screenName = "RECEIPT";

	@Column(name = "receiptType", length = 50)
	private String receiptType;

	@Column(name = "docid", length = 50)
	private String docId;

	@Column(name = "docdate")
	@Builder.Default
	private LocalDate docDate = LocalDate.now();

	@Column(name = "type", length = 50)
	private String type;

	@Column(name = "customername", length = 150)
	private String customerName;

	@Column(name = "customercode", length = 50)
	private String customerCode;

	@Column(name = "bankcashacc", length = 50)
	private String bankCashAcc;

	@Column(name = "receiptamt", precision = 10, scale = 2)
	private BigDecimal receiptAmt;

	@Column(name = "currency", length = 10)
	private String currency;

	@Column(name = "currencyAmt", precision = 10, scale = 2)
	private BigDecimal currencyAmt;

	@Column(name = "bankchargeacc", length = 50)
	private String bankChargeAcc;

	@Column(name = "bankcharges", precision = 10, scale = 2)
	private BigDecimal bankCharges;

	@Column(name = "incurrencybnkchargs", length = 10)
	private String inCurrencyBnkChargs;

	@Column(name = "tdsamt", precision = 10, scale = 2)
	private BigDecimal tdsAmt;

	@Column(name = "incurrencytdsamt", length = 10)
	private String inCurrencyTdsAmt;

	@Column(name = "chequebank", length = 20)
	private String chequeBank;

	@Column(name = "receiptType1", length = 20)
	private String receiptType1;

	@Column(name = "chequeutino", length = 100)
	private String chequeUtiNo;

	@Column(name = "chequeutidate")
	private LocalDate chequeUtiDate;

	@Column(name = "receivedfrom", length = 100)
	private String receivedFrom;

	@Column(name = "remarks")
	private String remarks;

	// Additional fields
	@Column(name = "orgid")
	private Long orgId;

	@Column(name = "currentamount", precision = 10, scale = 2)
	private BigDecimal currencyAmount;

	@Column(name = "netamount", precision = 10, scale = 2)
	private BigDecimal netAmount;
	
	@Column(name = "onaccount", precision = 10, scale = 2)
	private BigDecimal onAccount;

	@OneToMany(mappedBy = "receiptVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	List<ReceiptInvDetailsVO> receiptInvDetailsVO;

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
