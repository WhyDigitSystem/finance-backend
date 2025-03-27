package com.base.basesetup.entity;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "taxinvoiceannexure")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxInvoiceAnnexureVO {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taxinvoiceannexuregen")
	@SequenceGenerator(name = "taxinvoiceannexuregen", sequenceName = "taxinvoiceannexureseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "accountsid")
	private Long id;
	@Column(name = "transdate")
	private LocalDate transDate;
	@Column(name = "transno")
	private String transNo;
	@Column(name = "kitid")
	private String kitId;

	private String dsec;
	@Column(name = "skutype")
	private String skuType;

	private int qty;

	private double rate;

	private double amount;
	
//	private double subTotal;
	
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "taxinvoiceid")
	TaxInvoiceVO taxInvoiceVO;
	
}
