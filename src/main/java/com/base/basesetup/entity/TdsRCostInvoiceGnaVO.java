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
@Table(name = "tdsrcostinvoicegna")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TdsRCostInvoiceGnaVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tdsrcostinvoicegnagen")
	@SequenceGenerator(name = "tdsrcostinvoicegnagen", sequenceName = "tdsrcostinvoicegnaseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "tdsrcostinvoicegnaid")
	private Long id;
	@Column(name = "tds", length = 30)
	private String tds;
	@Column(name = "tdsper", precision = 10, scale = 2)
	private BigDecimal tdsPer;
	@Column(name = "section", length = 255)
	private String section;
	@Column(name = "tdsperamt", precision = 10, scale = 2)
	private BigDecimal TdsPerAmt;
	@Column(name = "totaltdsamt", precision = 10, scale = 2)
	private BigDecimal totalTdsAmt;

	@ManyToOne
	@JoinColumn(name = "rcostinvoicegnaid")
//	@JsonBackReference("tdsrCostInvoiceGnaVO")
	@JsonIgnore
	private RCostInvoiceGnaVO rCostInvoiceGnaVO;

}
