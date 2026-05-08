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
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "urcostinvoicegna")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrCostInvoiceGnaVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "urcostinvoicegnagen")
	@SequenceGenerator(name = "urcostinvoicegnagen", sequenceName = "urcostinvoicegnaseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "urcostinvoicegnaid")
	private Long id;
	@Column(name = "docid", length = 30)
	private String docId;
	@Column(name = "docdate")
	private LocalDate docDate;
	@Column(name = "purvoucherno", length = 50)
	private String purVoucherNo;
	@Column(name = "purvoucherdate")
	private LocalDate purVoucherDate;
	@Column(name = "supplietype", length = 10)
	private String supplierType;
	@Column(name = "suppliercode", length = 15)
	private String supplierCode;
	@Column(name = "supplierbillno", length = 50)
	private String supplierBillNo;
	@Column(name = "supplierbilldate")
	private LocalDate supplierBillDate;
	@Column(name = "suppliername", length = 150)
	private String supplierName;
	@Column(name = "supplierplace", length = 15)
	private String supplierPlace;
	@Column(name = "creditdays", length = 10)
	private int creditDays;
	@Column(name = "duedate")
	private LocalDate dueDate;
	@Column(name = "currency", length = 10)
	private String currency;
	@Column(name = "exrate", precision = 10, scale = 2)
	private BigDecimal exRate;
	@Column(name = "suppliergstin", length = 150)
	private String supplierGstIn;
	@Column(name = "suppliergstincode")
	private String supplierGstInCode;
	@Column(name = "state")
	private String state;
	@Column(name = "remarks")
	private String remarks;
	@Column(name = "address")
	private String address;
	@Column(name = "otherinfo", length = 150)
	private String otherInfo;
	@Column(name = "shipperrefno", length = 25)
	private String shipperRefNo;
	@Column(name = "gsttype", length = 15)
	private String gstType;
	@Column(name = "vid", length = 50)
	private String vId;
	@Column(name = "vdate")
	private LocalDate vDate;
	@Column(name = "addresstype")
	private String addressType;

	// default fields
	@Column(name = "orgid", length = 15)
	private Long orgId;
	@Column(name = "active")
	private boolean active;
	@Column(name = "modifiedby", length = 25)
	private String updatedBy;
	@Column(name = "createdby", length = 25)
	private String createdBy;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "cancelremarks", length = 25)
	private String cancelRemarks;
	@Column(name = "branch", length = 25)
	private String branch;
	@Column(name = "branchcode", length = 10)
	private String branchCode;
	@Column(name = "finyear", length = 10)
	private String finYear;
	@Column(name = "status")
	private String status;
	@Column(name = "mode", length = 10)
	private String mode;
	@Column(name = "screencode", length = 10)
	private String screenCode = "URCI";
	@Column(name = "screenname", length = 25)
	private String screenName = "UR COSTINVOICE GNA";

//	SUMMARY
	@Column(name = "totchargeamtlct", precision = 10, scale = 2)
	private BigDecimal totChargeAmtLc;
	@Column(name = "netamountbillcurr", precision = 10, scale = 2)
	private BigDecimal netamountBillCurr;
	@Column(name = "actbillamtlc", precision = 10, scale = 2)
	private BigDecimal actBillAmtLc;
	@Column(name = "roundoff", precision = 10, scale = 2)
	private BigDecimal roundOff;
	@Column(name = "input", precision = 10, scale = 2)
	private BigDecimal input;
	@Column(name = "output", precision = 10, scale = 2)
	private BigDecimal output;
	@Column(name = "totalgstamount", precision = 10, scale = 2)
	private BigDecimal totalGstAmount;
	

//	APPROVED
	@Column(name = "approvestatus", length = 20)
	private String approveStatus;
	@Column(name = "approveby", length = 20)
	private String approveBy;
	@DateTimeFormat(pattern = "dd-MM-yyyy hh:mm:ss a")
	@Column(name = "approveon")
	private String approveOn;

	@OneToMany(mappedBy = "urCostInvoiceGnaVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	List<ChargesUrCostInvoiceGnaVO> chargesUrCostInvoiceGnaVO;
	
	
	@Transient
	List<ChargesUrCostInvoiceGnaVO> gstLines;

	@Transient
	List<ChargesUrCostInvoiceGnaVO> normalCharges;

	@OneToMany(mappedBy = "urCostInvoiceGnaVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	List<TdsUrCostInvoiceGnaVO> tdsUrCostInvoiceGnaVO;

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
