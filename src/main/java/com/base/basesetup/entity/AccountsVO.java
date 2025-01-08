package com.base.basesetup.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountsVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accountsgen")
	@SequenceGenerator(name = "accountsgen", sequenceName = "accountsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "accountsid")
	private Long id;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "branch",length = 30)
	private String branch;
	@Column(name = "branchcode",length = 10)
	private String branchCode;
	@Column(name = "finyear",length =10)
	private String finYear;
	@Column(name = "createdby",length = 30)
	private String createdBy;
	@Column(name = "modifiedby",length = 30)
	private String modifiedBy;
	@Column(name = "active")
	private boolean active=true;
	@Column(name = "cancel")
	private boolean cancel=false;
	@Column(name = "cancelremarks",length = 150)
	private String cancelRemarks;
	@Column(name = "sourceid")
	private Long sourceId;
	@Column(name = "sourcescreen",length = 30)
	private String sourceScreen;
	@Column(name = "sourcescreencode",length = 10)
	private String sourceScreenCode;
	@Column(name = "docid",length = 30)
	private String docId;
	@Column(name = "docdate")
	private LocalDate docDate= LocalDate.now();
	@Column(name = "refno",length = 30)
	private String refNo;
	@Column(name = "refdate")
	private LocalDate refDate;
	@Column(name = "currency",length = 30)
	private String currency;
	@Column(name = "exrate", precision = 10, scale = 2)
	private BigDecimal exRate;
	@Column(name = "remarks",length = 150)
	private String remarks;
	@Column(name = "totaldebitamount", precision = 10, scale = 2)
	private BigDecimal totalDebitAmount;
	@Column(name = "totalcreditamount", precision = 10, scale = 2)
	private BigDecimal totalCreditAmount;
	@Column(name = "sttaxamount", precision = 10, scale = 2)
	private BigDecimal stTaxAmount;
	@Column(name = "chargeableamount", precision = 10, scale = 2)
	private BigDecimal chargeableAmount;
	@Column(name = "creditdays",length = 5)
	private int creditDays;
	@Column(name = "duedate")
	private LocalDate dueDate;
	@Column(name = "chequeno",length = 30)
	private String chequeNo;
	@Column(name = "chequedate")
	private LocalDate chequeDate;
	@Column(name = "chequebank",length = 150)
	private String chequeBank;
	@Column(name = "product",length = 10)
	private String product;
	@Column(name = "amountinwords",length = 150)
	private String amountInWords;
	@Column(name = "supplierrefno",length = 30)
	private String supplierRefNo;
	@Column(name = "supplierrefdate")
	private LocalDate supplierRefDate;
	@Column(name = "billmonth",length = 30)
	private String billMonth;
	@Column(name = "acknowledgementno")
	private Long acknowledgementNo;
	@Column(name = "acknowledgementdate")
	private LocalDateTime acknowledgementDate;
	@Column(name = "irnid",length = 150)
	private String irnId;
	@Column(name = "rc",length = 30)
	private String rc;
	@Column(name = "salestype",length = 30)
	private String salesType;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy hh:mm:ss a")
	@Column(name="createdon",length = 25)
	private String createdon;

	@DateTimeFormat(pattern = "dd-MM-yyyy hh:mm:ss a")
	@Column(name="modifiedon",length = 25)
	private String modifiedon;
		


	@OneToMany(mappedBy = "accountsVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	List<AccountsDetailsVO> accountsDetailsVO ;

}
