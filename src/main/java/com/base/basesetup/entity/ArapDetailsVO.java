package com.base.basesetup.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "arapdetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArapDetailsVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "arapdetailsgen")
	@SequenceGenerator(name = "arapdetailsgen", sequenceName = "arapdetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "arapdetailsid")
	private Long id;
	@Column(name = "branch",length =25)
	private String branch;
	@Column(name = "finyear",length =10)
	private String finYear;
	@Column(name = "sourcetransid",length =50)
	private String sourceTransid;
	@Column(name = "docid",length =25)
	private String docId;
	@Column(name = "refno",length =50)
	private String refNo;
	@Column(name = "accname",length =50)
	private String accName;
	@Column(name = "currency",length =15)
	private String currency;
	@Column(name = "acccurrency",length =15)
	private String accCurrency;
	@Column(name = "exrate",precision =10,scale = 2)
	private float exRate;
	@Column(name = "amount",precision =10,scale = 2)
	private float amount;
	@Column(name = "baseamt",precision =10,scale = 2)
	private float baseAmt;
	@Column(name = "nativeamt",precision =10,scale = 2)
	private float nativeAmt;
	@Column(name = "chargableamt",precision =10,scale = 2)
	private float chargableAmt;
	@Column(name = "gstflag")
	private boolean gstFlag;
	@Column(name = "doctypecode",length =50)
	private String docTypeCode;
	@Column(name = "subtypecode",length =50)
	private String subTypeCode;
	@Column(name = "subledgerdivision",length =50)
	private String subLedgerDivision;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name = "docdate")
	private LocalDate docDate=LocalDate.now();
	@Column(name = "supprefno",length =50)
	private String suppRefNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name = "refdate")
	private LocalDateTime refDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name = "suprefdate")
	private LocalDateTime supRefDate;
	@Column(name = "subledgercode",length =50)
	private String subLedgerCode;
	@Column(name = "creditdays",length =10)
	private String creditDays;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name = "duedate")
	private LocalDateTime dueDate;
	@Column(name = "tdsamt",precision =10,scale = 2)
	private float TDSAmt;
	@Column(name = "hno",length =50)
	private String hno;
	@Column(name = "orgid",length =15)
	private Long orgId;
	@Column(name = "active")
	private boolean active;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "cancelremarks",length =50)
	private String canelRemarks;
	@Column(name = "createdby",length =25)
	private String createdBy;
	@Column(name = "modifiedby",length =25)
	private String updatedBy;
	@Column(name = "Screencode",length =10)
	private String ScreenCode="AD";
	@Column(name = "Screenname",length =25)
	private String ScreenName="ARAP Details";
	@Column(name = "ipno",length =15)
	private String ipNo;
	@Column(name = "latitude",length =100)
	private String latitude;
	@Column(name = "subledgername",length =50)
	private String subLedgerName;
	@Column(name = "branchcode",length =20)
	private String branchCode;
	


	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
