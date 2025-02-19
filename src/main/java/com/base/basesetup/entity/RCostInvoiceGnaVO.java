package com.base.basesetup.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="rcostinvoicegna")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RCostInvoiceGnaVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rcostinvoicegnagen")
	@SequenceGenerator(name = "rcostinvoicegnagen", sequenceName = "rcostinvoicegnaseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "rcostinvoicegnaid")
	private Long id;
	
	@Column(name = "docid", length = 30)
	private String docId;
	@Column(name = "docdate")
	private LocalDate docDate = LocalDate.now();
	@Column(name = "purvoucherno", length = 50)
	private String purVoucherNo;
	@Column(name = "purvoucherdate")
	private LocalDate purVoucherDate;
	@Column(name = "partytype", length = 10)
	private String partyType;
	@Column(name = "partycode", length = 15)
	private String partyCode;
	@Column(name = "supplierbillno", length = 50)
	private String supplierBillNo;
	@Column(name = "supplierbilldate")
	private LocalDate supplierBillDate;
	@Column(name = "partyname", length = 150)
	private String partyName;
	@Column(name = "creditdays", length = 10)
	private int creditDays;
	@Column(name = "duedate")
	private LocalDate dueDate;
	@Column(name = "suppliergstin", length = 150)
	private String supplierGstIn;
	@Column(name = "suppliergstincode", length = 20)
	private String supplierGstInCode;
	@Column(name = "currency", length = 10)
	private String currency;
	@Column(name = "exrate", precision = 10, scale = 2)
	private BigDecimal exRate;
	@Column(name = "place", length = 150)
	private String place;
	@Column(name = "address", length = 150)
	private String address;
	@Column(name = "remarks", length = 150)
	private String remarks;
	@Column(name = "gsttype", length = 15)
	private String gstType;
	
	//Default fields
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
	private String screenCode = "RCI";
	@Column(name = "screenname", length = 25)
	private String screenName = "REGISTER COSTINVOICE GNA";


//	SUMMARY
	@Column(name = "actbillamtbc", precision = 10, scale = 2)
	private BigDecimal actBillAmtBc;
	@Column(name = "actbillamtlc", precision = 10, scale = 2)
	private BigDecimal actBillAmtLc;
	@Column(name = "netamtbc", precision = 10, scale = 2)
	private BigDecimal netAmtBc;
	@Column(name = "netamtlc", precision = 10, scale = 2)
	private BigDecimal netAmtLc;
	@Column(name = "roundoff")
	private BigDecimal roundOff;
	@Column(name = "gstamtlc", precision = 10, scale = 2)
	private BigDecimal gstAmtLc;
	@Column(name = "amountinwords")
	private String amountInWords;
	

////	APPROVED
	@Column(name = "approvestatus", length = 20)
	private String approveStatus;
	@Column(name = "approveby", length = 20)
	private String approveBy;
	@DateTimeFormat(pattern = "dd-MM-yyyy hh:mm:ss a")
	@Column(name = "approveon")
	private String approveOn;
	

	 @OneToMany(mappedBy = "rCostInvoiceGnaVO", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	 @JsonManagedReference
	 private List<ChargeRCostInvoiceGnaVO> chargeRCostInvoiceGnaVO = new ArrayList<>();

	 @OneToMany(mappedBy = "rCostInvoiceGnaVO", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	 @JsonManagedReference
	 private List<TdsRCostInvoiceGnaVO> tdsRCostInvoiceGnaVO = new ArrayList<>();


	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}

	@JsonGetter("cancel")
	public String getCancel() {
		return cancel ? "T" : "F";
	}
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
	

}
