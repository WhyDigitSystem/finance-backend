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
@Table(name = "chargesurcostinvoicegna")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChargesUrCostInvoiceGnaVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chargesurcostinvoicegnagen")
	@SequenceGenerator(name = "chargesurcostinvoicegnagen", sequenceName = "chargesurcostinvoicegnaseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "chargesurcostinvoicegnaid")
	private Long id;
	@Column(name = "chargeledger", length = 150)
	private String chargeLedger;
	@Column(name = "chargeaccount", length = 150)
	private String chargeAccount;
	@Column(name = "currency", length = 15)
	private String currency;
	@Column(name = "exrate", precision = 10, scale = 2)
	private BigDecimal exRate;
	@Column(name = "rate", precision = 10, scale = 2)
	private BigDecimal rate;
	@Column(name = "gstpercent")
	private float GSTPercent;
	@Column(name = "fcamount", precision = 10, scale = 2)
	private BigDecimal fcAmount;
	@Column(name = "lcamount", precision = 10, scale = 2)
	private BigDecimal lcAmount;
	@Column(name = "billamount", precision = 10, scale = 2)
	private BigDecimal billAmount;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "urcostinvoicegnaid")
	UrCostInvoiceGnaVO urCostInvoiceGnaVO;
}
