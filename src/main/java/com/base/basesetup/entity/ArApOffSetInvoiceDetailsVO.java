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
@Table(name = "arapoffsetinvoicedetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArApOffSetInvoiceDetailsVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "arapoffsetinvoicedetailsgen")
	@SequenceGenerator(name = "arapoffsetinvoicedetailsgen", sequenceName = "arapoffsetinvoicedetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "arapoffsetinvoicedetailsid")
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

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "arapadjustmentoffsetid")
	ArApAdjustmentOffSetVO arApAdjustmentOffSetVO;
}
