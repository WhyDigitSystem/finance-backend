package com.base.basesetup.entity;

import java.math.BigDecimal;

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
@Table(name = "chargertaxinvoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChargerTaxInvoiceVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chargertaxinvoicegen")
	@SequenceGenerator(name = "chargertaxinvoicegen", sequenceName = "chargertaxinvoiceVO", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "chargertaxinvoiceid")
	private Long id;
	@Column(name = "type")
	private String type;
	@Column(name = "chargeCode")
	private String chargeCode;
	@Column(name = "gchargecode")
	private String gChargeCode;
	@Column(name = "chargename")
	private String chargeName;
	@Column(name = "taxable")
	private String taxable;
	@Column(name = "qty")
	private int qty;
	@Column(name = "rate")
	private BigDecimal rate;
	@Column(name = "currency")
	private String currency;
	@Column(name = "exRate")
	private BigDecimal exRate;
	@Column(name = "fcAmount")
	private BigDecimal fcAmount;
	@Column(name = "lcAmount")
	private BigDecimal lcAmount;
	@Column(name = "billAmount")
	private BigDecimal billAmount;
	@Column(name = "sac")
	private String sac;
	@Column(name = "gstpercent")
	private BigDecimal GSTPercent;
	@Column(name = "gstamount")
	private BigDecimal GST;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "taxinvoiceid")
	TaxInvoiceVO taxInvoiceVO;
}