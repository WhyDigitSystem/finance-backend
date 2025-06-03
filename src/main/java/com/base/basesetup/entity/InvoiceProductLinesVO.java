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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="invoicedetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProductLinesVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "invoicedetailsgen")
	@SequenceGenerator(name = "invoicedetailsgen", sequenceName = "invoicedetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "invoicedetailsid")
	private Long id;
	@Column(name="description")
	private String description;
	@Column(name="quantity",precision = 10, scale = 2)
    private BigDecimal quantity;
	@Column(name="rate",precision = 10, scale = 2)
    private BigDecimal rate;
	@Column(name="amount",precision = 10, scale = 2)
    private BigDecimal amount;
	@Column(name="tax",precision = 10, scale = 2)
    private BigDecimal tax;
	@Column(name="igst",precision = 10, scale = 2)
    private BigDecimal igst;
	@Column(name="cgst",precision = 10, scale = 2)
    private BigDecimal cgst;
	@Column(name="sgst",precision = 10, scale = 2)	
    private BigDecimal sgst;
	@Column(name="igstamount",precision = 10, scale = 2)
    private BigDecimal igstAmount;
	@Column(name="cgstamount",precision = 10, scale = 2)
    private BigDecimal cgstAmount;
	@Column(name="sgstamount",precision = 10, scale = 2)
    private BigDecimal sgstAmount;
	@Column(name="baseamount",precision = 10, scale = 2)
    private BigDecimal baseAmount;
	@Column(name="taxvalue",precision = 10, scale = 2)
    private BigDecimal taxValue;
	
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "invoiceid")
	private InvoiceVO invoiceVO;

}
