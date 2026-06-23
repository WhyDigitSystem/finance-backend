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
@Table(name = "taxinvoicekitline")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxInvoiceKitLineVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "taxinvoicekitlinegen")
	@SequenceGenerator(name = "taxinvoicekitlinegen", sequenceName = "taxinvoicekitlineseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "taxinvoicekitlineid")
	private Long id;

	@Column(name = "annexuredate")
	private String annexureDate;
	@Column(name = "manifestno")
	private String manifestNo;
	@Column(name = "emitter")
	private String emitter;
	@Column(name = "location")
	private String location;
	@Column(name = "kitno")
	private String kitNo;
	@Column(name = "kitqty", precision = 10, scale = 2)
	private BigDecimal kitQty;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "invoicenewid")
	private InvoiceNewVO invoiceNewVO;

}
