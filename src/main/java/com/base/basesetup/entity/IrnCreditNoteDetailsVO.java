package com.base.basesetup.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "irncreditnotedetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IrnCreditNoteDetailsVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "irncreditnotedetailsgen")
	@SequenceGenerator(name = "irncreditnotedetailsgen", sequenceName = "irncreditnotedetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "irncreditnotedetailsid")
	private Long id;
	
	@Column(name = "chargetype",length = 30)
	private String chargeType;
	@Column(name = "chargecode",length = 30)
	private String chargeCode;
	@Column(name = "govchargecode",length = 10)
	private String govChargeCode;
	@Column(name = "ledger",length = 150)
	private String ledger;
	@Column(name = "chargename",length = 150)
	private String chargeName;
	@Column(name = "taxable",length = 10)
	private String taxable;
	@Column(name = "qty")
	private int qty;
	@Column(name = "description")
	private String description;
	
	
	@Column(name = "rate", precision = 10, scale = 2)
	private BigDecimal rate;
	@Column(name = "currency",length = 10)
	private String currency;
	@Column(name = "exrate", precision = 10, scale = 2)
	private BigDecimal exRate;
	
	
	@Column(name = "exempted", precision = 10, scale = 2)
	private String exempted;
	
	@Column(name = "fcamount", precision = 10, scale = 2)
	private BigDecimal fcAmount;
	@Column(name = "lcamount", precision = 10, scale = 2)
	private BigDecimal lcAmount;
	
	@Column(name = "tlcamount", precision = 10, scale = 2)
	private BigDecimal tlcAmount;
	
	@Column(name = "billamount", precision = 10, scale = 2)
	private BigDecimal billAmount;
	@Column(name = "sac",length = 30)
	private String sac;
	@Column(name = "gstpercent")
	private int GSTPercent;
	@Column(name = "gstamount", precision = 10, scale = 2)
	private BigDecimal gstAmount;

	
	@JsonProperty("rate")
    public String getFormattedRate() {
        return rate != null ? rate.setScale(2, RoundingMode.HALF_UP).toString() : "0.00";
    }

    @JsonProperty("exRate")
    public String getFormattedExRate() {
        return exRate != null ? exRate.setScale(2, RoundingMode.HALF_UP).toString() : "0.00";
    }

    @JsonProperty("fcAmount")
    public String getFormattedFcAmount() {
        return fcAmount != null ? fcAmount.setScale(2, RoundingMode.HALF_UP).toString() : "0.00";
    }

    @JsonProperty("lcAmount")
    public String getFormattedLcAmount() {
        return lcAmount != null ? lcAmount.setScale(2, RoundingMode.HALF_UP).toString() : "0.00";
    }

    @JsonProperty("tlcAmount")
    public String getFormattedTlcAmount() {
        return tlcAmount != null ? tlcAmount.setScale(2, RoundingMode.HALF_UP).toString() : "0.00";
    }

    @JsonProperty("billAmount")
    public String getFormattedBillAmount() {
        return billAmount != null ? billAmount.setScale(2, RoundingMode.HALF_UP).toString() : "0.00";
    }

    @JsonProperty("gstAmount")
    public String getFormattedGstAmount() {
        return gstAmount != null ? gstAmount.setScale(2, RoundingMode.HALF_UP).toString() : "0.00";
    }

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "irncreditnoteid")
	IrnCreditNoteVO irnCreditNoteVO;
}
