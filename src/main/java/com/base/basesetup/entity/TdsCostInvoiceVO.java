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
@Table(name = "tdscostinvoice")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class TdsCostInvoiceVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tdscostinvoicegen")
	@SequenceGenerator(name = "tdscostinvoicegen", sequenceName = "tdscostinvoiceseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "tdscostinvoiceid")
	private Long id;
	@Column(name = "tdswithholding",length =50)
	private String tdsWithHolding;
	@Column(name = "tdswithholdingper",precision =10,scale = 2)
	private BigDecimal tdsWithHoldingPer;
	@Column(name = "section",length =255)
	private String section;
	@Column(name = "totaltds",precision =10,scale = 2)
	private BigDecimal totTdsWhAmnt;
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "costinvoiceid")
	CostInvoiceVO costInvoiceVO;
}
