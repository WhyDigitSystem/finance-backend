package com.base.basesetup.entity;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoice")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "invoicegen")
	@SequenceGenerator(name = "invoicegen", sequenceName = "invoiceseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "invoiceid")
	private Long id;

	@Column(name = "podate", columnDefinition = "TEXT")
	private String poDate;

	@Column(name = "pono", columnDefinition = "TEXT")
	private String poNumber;

	@Column(name = "companyaddress", columnDefinition = "TEXT")
	private String companyAddress;

	@Column(name = "vendoraddress", columnDefinition = "TEXT")
	private String vendorAddress;

	@Column(name = "deliveryaddress", columnDefinition = "TEXT")
	private String deliveryAddress;

	@Column(name = "termsandconditions", length = 1000, columnDefinition = "TEXT")
	private String termsAndConditions;

	@Column(name = "subtotal")
	private Long subtotal;

	@Column(name = "sgst")
	private Integer sgst;

	@Column(name = "cgst")
	private Integer cgst;

	@Column(name = "total")
	private Long total;

	@Column(name = "gsttype", length = 1000, columnDefinition = "TEXT")
	private String gstType;

	@Column(name = "igst")
	private Integer igst;

	@Column(name = "orgid")
	private Long orgId;
	
	@Column(name = "finyear")
	private String finYear;
	
	@Column(name = "createdby", columnDefinition = "TEXT")
	private String createdBy;
	
	@Column(name = "modifiedby", columnDefinition = "TEXT")
	private String modifiedBy;
	
	@Column(name = "cancel")
	private boolean cancel = false;
	


	@OneToMany(mappedBy = "invoiceVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<InvoiceProductLinesVO> productLines;

	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
