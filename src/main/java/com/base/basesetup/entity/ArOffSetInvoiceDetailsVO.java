package com.base.basesetup.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aroffsetinvoicedetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArOffSetInvoiceDetailsVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aroffsetinvoicedetailsgen")
	@SequenceGenerator(name = "aroffsetinvoicedetailsgen", sequenceName = "aroffsetinvoicedetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "aroffsetinvoicedetailsid")
	private Long id;
	@Column(name = "invoiceno",length = 50)
	private String invoiceNo;
	@Column(name = "invoicedate")
	private LocalDate invoiceDate;
	@Column(name = "refno",length = 50)
	private String refNo;
	@Column(name = "refdate")
	private LocalDate refDate;
	@Column(name = "curr",length = 10)
	private String curr;
	@Column(name = "exrate",precision = 10, scale = 6)
	private BigDecimal exRate;
	@Column(name = "invamount",precision = 10, scale = 2)
	private BigDecimal invAmount;
	@Column(name = "outstanding",precision = 10, scale = 2)
	private BigDecimal outStanding;
	@Column(name = "settled",precision = 10, scale = 2)
	private BigDecimal settled;
	@Column(name = "setexrate",precision = 10, scale = 6)
	private BigDecimal setExRate;
	@Column(name = "tnxsettled",precision = 10, scale = 2)
	private BigDecimal tnxSettled;
	@Column(name = "gainorloss",precision = 10, scale = 2)
	private BigDecimal gainOrLoss;
	@Column(name = "remarks",length=150)
	private String remarks;
	@Column(name = "gstamt", precision = 10, scale = 2)
	private BigDecimal gstAmt;
	@Column(name = "chargeamt", precision = 10, scale = 2)
	private BigDecimal chargeAmt;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "aradjustmentoffsetid")
	ArAdjustmentOffSetVO arAdjustmentOffSetVO;
}
