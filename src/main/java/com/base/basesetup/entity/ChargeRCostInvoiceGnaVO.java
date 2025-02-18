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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chargercostinvoicegna")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargeRCostInvoiceGnaVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chargercostinvoicegnagen")
	@SequenceGenerator(name = "chargercostinvoicegnagen", sequenceName = "chargercostinvoicegnaseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "chargercostinvoicegnaid")
	private Long id;

	@Column(name = "chargename", length = 150)
	private String chargeName;
	@Column(name = "tdsapplicable")
	private boolean tdsApplicable;
	@Column(name = "currency", length = 15)
	private String currency;
	@Column(name = "exrate", precision = 10, scale = 2)
	private BigDecimal exRate;
	@Column(name = "rate", precision = 10, scale = 2)
	private BigDecimal rate;
	@Column(name = "gstper")
	private float gstPer;
	@Column(name = "fcamt", precision = 10, scale = 2)
	private BigDecimal fcAmt;
	@Column(name = "lcamt", precision = 10, scale = 2)
	private BigDecimal lcAmt;
	@Column(name = "billamt", precision = 10, scale = 2)
	private BigDecimal billAmt;
	@Column(name = "gtaamount", precision = 10, scale = 2)
	private BigDecimal gtaamount;

//	@ManyToOne
//	@JoinColumn(name = "rcostinvoicegnaid")
//	@JsonBackReference
//	private RCostInvoiceGnaVO rCostInvoiceGnaVO;

	@ManyToOne
	@JoinColumn(name = "rcostinvoicegnaid")
//	@JsonBackReference("chargerCostInvoiceGnaVO")
	@JsonIgnore
	private RCostInvoiceGnaVO rCostInvoiceGnaVO;
}
