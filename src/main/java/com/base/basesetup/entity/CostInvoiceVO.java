
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
@Table(name = "costinvoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostInvoiceVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "costinvoicegen")
	@SequenceGenerator(name = "costinvoicegen", sequenceName = "costinvoiceseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "costinvoiceid")
	private Long id;
	@Column(name = "mode", length = 10)
	private String mode;
	@Column(name = "product", length = 50)
	private String product;
	@Column(name = "purvoucherno", length = 50)
	private String purVoucherNo;
	@Column(name = "purvoucherdate")
	private LocalDate purVoucherDate;
	@Column(name = "supplierbillno", length = 50)
	private String supplierBillNo;
	@Column(name = "supplietype", length = 10)
	private String supplierType;
	@Column(name = "suppliercode", length = 15)
	private String supplierCode;
	@Column(name = "creditdays", length = 10)
	private int creditDays;
	@Column(name = "duedate")
	private LocalDate dueDate;
	@Column(name = "suppliername", length = 150)
	private String supplierName;
	@Column(name = "supplierplace", length = 15)
	private String supplierPlace;
	@Column(name = "currency", length = 10)
	private String currency;
	@Column(name = "exrate", precision = 10, scale = 2)
	private BigDecimal exRate;
	@Column(name = "suppliergstin", length = 20)
	private String supplierGstIn;
	@Column(name = "suppliergstincode", length = 15)
	private String supplierGstInCode;
	@Column(name = "remarks", length = 150)
	private String remarks;
	@Column(name = "address", length = 150)
	private String address;
	@Column(name = "otherinfo", length = 150)
	private String otherInfo;
	@Column(name = "shipperrefno", length = 25)
	private String shipperRefNo;
	@Column(name = "gsttype", length = 15)
	private String gstType;
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
	@Column(name = "customer", length = 100)
	private String customer;
	@Column(name = "client", length = 30)
	private String client;
	@Column(name = "finyear", length = 10)
	private String finYear;
	@Column(name = "screencode", length = 10)
	private String screenCode = "CI";
	@Column(name = "screenname", length = 25)
	private String screenName = "COST INVOICE";
	@Column(name = "docid", length = 30)
	private String docId;
	@Column(name = "docdate")
	private LocalDate docDate = LocalDate.now();
	@Column(name = "payment", length = 20)
	private String payment;
	@Column(name = "accuralid", length = 20)
	private String accuralid;
	@Column(name = "utrref", length = 10)
	private String utrRef;
	@Column(name = "costtype", length = 10)
	private String costType;

//	SUMMARY
	@Column(name = "totchargesbillcurramt", precision = 10, scale = 2)
	private BigDecimal totChargesBillCurrAmt;
	@Column(name = "totchargeslcamt", precision = 10, scale = 2)
	private BigDecimal totChargesLcAmt;
	@Column(name = "actbillcurramt", precision = 10, scale = 2)
	private BigDecimal actBillCurrAmt;
	@Column(name = "actbilllcamt", precision = 10, scale = 2)
	private BigDecimal actBillLcAmt;
	@Column(name = "netbillcurramt", precision = 10, scale = 2)
	private BigDecimal netBillCurrAmt;
	@Column(name = "netbilllcamt", precision = 10, scale = 2)
	private BigDecimal netBillLcAmt;
	@Column(name = "roundoff")
	private Long roundOff;
	@Column(name = "sumLcAmt", precision = 10, scale = 2)
	private BigDecimal sumLcAmt;
	@Column(name = "gstinputlcamt", precision = 10, scale = 2)
	private BigDecimal gstInputLcAmt;

//	APPROVED
	@Column(name = "approvestatus", length = 20)
	private String approveStatus;
	@Column(name = "approveby", length = 20)
	private String approveBy;
	@DateTimeFormat(pattern = "dd-MM-yyyy hh:mm:ss a")
	@Column(name = "approveon")
	private String approveOn;

	@OneToMany(mappedBy = "costInvoiceVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	List<ChargerCostInvoiceVO> chargerCostInvoiceVO;

	@Transient
	List<ChargerCostInvoiceVO> gstLines;

	@Transient
	List<ChargerCostInvoiceVO> normalCharges;

	@OneToMany(mappedBy = "costInvoiceVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	List<TdsCostInvoiceVO> tdsCostInvoiceVO;

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
