package com.base.basesetup.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "taxinvoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxInvoiceVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taxinvoicegen")
	@SequenceGenerator(name = "taxinvoicegen", sequenceName = "taxinvoiceseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "taxinvoiceid")
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
	@Column(name = "biztype",length = 10)
	private String bizType;
	@Column(name = "bizmode",length = 30)
	private String bizMode;
	@Column(name = "partyname",length = 150)
	private String partyName;
	@Column(name = "partyid")
	private Long partyId;
	@Column(name = "partycode",length = 10)
	private String partyCode;
	@Column(name = "partytype",length = 30)
	private String partyType;
	@Column(name = "stateno",length = 30)
	private String stateNo;
	@Column(name = "statecode",length = 10)
	private String stateCode;
	@Column(name = "recipientgstin",length = 30)
	private String recipientGSTIN;
	@Column(name = "placeofsupply",length = 30)
	private String placeOfSupply;
	@Column(name = "addresstype",length = 30)
	private String addressType;
	@Column(name = "address",length = 150)
	private String address;
	@Column(name = "pincode",length = 10)
	private String pinCode;
	@Column(name = "status",length = 30)
	private String status;
	@Column(name = "gsttype",length = 30)
	private String gstType;
	@Column(name = "docid",length = 30)
	private String docId;
	@Column(name = "screencode",length = 30)
	private String screenCode="TI";
	@Column(name = "screenname",length = 30)
	private String screenName="TAX INVOICE";

	@Column(name = "docdate")
	private LocalDate docDate= LocalDate.now();
	@Column(name = "supplierbillno",length = 30)
	private String supplierBillNo;
	@Column(name = "supplierbilldate")
	private LocalDate supplierBillDate;
	@Column(name = "billcurr",length = 30)
	private String billCurr;
	@Column(name = "billcurrrate", precision = 10, scale = 2)
	private BigDecimal billCurrRate;
	@Column(name = "examount", precision = 10, scale = 2)
	private BigDecimal exAmount;
	@Column(name = "creditdays",length = 5)
	private int creditDays;
	@Column(name = "contactperson",length = 30)
	private String contactPerson;
	@Column(name = "shipperinvoiceno",length = 30)
	private String shipperInvoiceNo;
	@Column(name = "billofentry",length = 30)
	private String billOfEntry;
	@Column(name = "billmonth",length = 30)
	private String billMonth;
	
	@Column(name="approvestatus",length = 20)
	private String approveStatus;
	@Column(name="approveby",length = 20)
	private String approveBy;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy hh:mm:ss a")
	@Column(name="approveon")
	private String approveOn;
	
	
	
	@Column(name = "invoiceno",length = 30)
	private String invoiceNo;

	@Column(name = "invoicedate")
	private LocalDate invoiceDate;
	@Column(name = "salestype",length = 30)
	private String salesType;

	@Column(name = "totalchargeamountlc", precision = 10, scale = 2)
	private BigDecimal totalChargeAmountLc;
	@Column(name = "totaltaxamountlc", precision = 10, scale = 2)
	private BigDecimal totalTaxAmountLc;
	@Column(name = "totalinvamountlc", precision = 10, scale = 2)
	private BigDecimal totalInvAmountLc;
	@Column(name = "roundoffamountlc", precision = 10, scale = 2)
	private BigDecimal roundOffAmountLc;
	@Column(name = "totalchargeamountbc", precision = 10, scale = 2)
	private BigDecimal totalChargeAmountBc;
	@Column(name = "totaltaxamountbc", precision = 10, scale = 2)
	private BigDecimal totalTaxAmountBc;
	@Column(name = "totalinvamountbc", precision = 10, scale = 2)
	private BigDecimal totalInvAmountBc;
	@Column(name = "totaltaxableamountlc", precision = 10, scale = 2)
	private BigDecimal totalTaxableAmountLc;
	@Column(name = "amountinwords",length = 150)
	private String amountInWords;
	@Column(name = "billingremarks",length = 30)
	private String billingRemarks;

	


	@OneToMany(mappedBy = "taxInvoiceVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	List<TaxInvoiceDetailsVO> taxInvoiceDetailsVO;

	
	@OneToMany(mappedBy = "taxInvoiceVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	List<TaxInvoiceGstVO> taxInvoiceGstVO;

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
	
	
	// Formatted getters for BigDecimal fields
    @JsonProperty("totalChargeAmountLc")
    public String getFormattedTotalChargeAmountLc() {
        return totalChargeAmountLc != null 
            ? totalChargeAmountLc.setScale(2, RoundingMode.HALF_UP).toString() 
            : "0.00";
    }

    @JsonProperty("totalTaxAmountLc")
    public String getFormattedTotalTaxAmountLc() {
        return totalTaxAmountLc != null 
            ? totalTaxAmountLc.setScale(2, RoundingMode.HALF_UP).toString() 
            : "0.00";
    }

    @JsonProperty("totalInvAmountLc")
    public String getFormattedTotalInvAmountLc() {
        return totalInvAmountLc != null 
            ? totalInvAmountLc.setScale(2, RoundingMode.HALF_UP).toString() 
            : "0.00";
    }

    @JsonProperty("roundOffAmountLc")
    public String getFormattedRoundOffAmountLc() {
        return roundOffAmountLc != null 
            ? roundOffAmountLc.setScale(2, RoundingMode.HALF_UP).toString() 
            : "0.00";
    }

    @JsonProperty("totalChargeAmountBc")
    public String getFormattedTotalChargeAmountBc() {
        return totalChargeAmountBc != null 
            ? totalChargeAmountBc.setScale(2, RoundingMode.HALF_UP).toString() 
            : "0.00";
    }

    @JsonProperty("totalTaxAmountBc")
    public String getFormattedTotalTaxAmountBc() {
        return totalTaxAmountBc != null 
            ? totalTaxAmountBc.setScale(2, RoundingMode.HALF_UP).toString() 
            : "0.00";
    }

    @JsonProperty("totalInvAmountBc")
    public String getFormattedTotalInvAmountBc() {
        return totalInvAmountBc != null 
            ? totalInvAmountBc.setScale(2, RoundingMode.HALF_UP).toString() 
            : "0.00";
    }

    @JsonProperty("totalTaxableAmountLc")
    public String getFormattedTotalTaxableAmountLc() {
        return totalTaxableAmountLc != null 
            ? totalTaxableAmountLc.setScale(2, RoundingMode.HALF_UP).toString() 
            : "0.00";
    }

   
}