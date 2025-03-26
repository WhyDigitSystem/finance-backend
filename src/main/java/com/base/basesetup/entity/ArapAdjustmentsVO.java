package com.base.basesetup.entity;


import java.math.BigDecimal;
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
import com.fasterxml.jackson.annotation.JsonGetter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "arapadjustments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArapAdjustmentsVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "arapadjustmentsgen")
	@SequenceGenerator(name = "arapadjustmentsgen", sequenceName = "arapadjustmentsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "arapadjustmentsid")
	private Long id;
	@Column(name = "branch", length = 25)
	private String branch;
	@Column(name = "finyear", length = 20)
	private String finYear;
	@Column(name = "sourceid")
	private Long sourceId;
	@Column(name = "docid", length = 50)
	private String docId;
	@Column(name = "refno", length = 50)
	private String refNo;
	@Column(name = "accountname", length = 50)
	private String accountName;
	@Column(name = "currency", length = 20)
	private String currency;
	@Column(name = "acccurrency", length = 20)
	private String accCurrency;
	@Column(name = "baseamt", precision = 10, scale = 2)
	private BigDecimal baseAmt;
	@Column(name = "nativeamt", precision = 10, scale = 2)
	private BigDecimal nativeAmt;
	@Column(name = "offdocid", length = 50)
	private String offDocId;
	@Column(name = "vouchertype", length = 50)
	private String voucherType;
	@Column(name = "docdate")
	private LocalDate docDate=LocalDate.now();
	@Column(name = "refdate")
	private LocalDate refDate;
	@Column(name = "subledgercode", length = 50)
	private String subLedgerCode;
	@Column(name = "exrate", precision = 10, scale = 2)
	private BigDecimal exRate;
	@Column(name = "creditdays", length = 10)
	private String creditDays;
	@Column(name = "duedate")
	private LocalDate dueDate;
	@Column(name = "orgid", length = 20)
	private Long orgId;
	@Column(name = "active")
	private boolean active;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "createdby", length = 25)
	private String createdBy;
	@Column(name = "modifiedby", length = 25)
	private String updatedBy;
	@Column(name = "branchcode", length = 20)
	private String branchCode;
	@Column(name = "screencode", length = 10)
	private String screenCode = "AA";
	@Column(name = "screenname", length = 25)
	private String screenName = "ARAP AdJUSTMENTS";
	@Column(name = "ipno", length = 15)
	private String ipNo;
	@Column(name = "latitude", length = 100)
	private String latitude;
	@Column(name = "transid", length = 50)
	private String transId;
	@Column(name = "chargeableamt", precision = 10, scale = 2)
	private BigDecimal chargeableAmt;
	@Column(name = "tdsamt", precision = 10, scale = 2)
	private BigDecimal tdsAmt;
	@Column(name = "gstflag")
	private boolean gstFlag;
	@Column(name = "subledgername", length = 50)
	private String subLedgerName;
	@Column(name = "amount")
	private BigDecimal amount;
	
	
	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
