package com.base.basesetup.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="einvoice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EInvoiceVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "einvoicegen")
	@SequenceGenerator(name = "einvoicegen", sequenceName = "einvoiceseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "einvoiceid")
	private Long id;
	
	private boolean cancel;
	
	private boolean active;
	
	@Column(name = "createdby")
	private String createdBy;
	
	@Column(name = "modifiedby")
	private String modifiedBy;
	
	@Column(name = "cancelremarks")
	private String cancelRemarks;
	
	private String taxsch;
	
	private String revcharge;
	
	private String igstonintra;
	
	private String sellergstin;
	
	private String sellerlegalname;
	
	private String sellertradename;
	
	
	private String selleradd1;
	
	private String selleradd2;
	
	private String sellerlocation;
	
	private String sellerpincode;
	
	private String sellerstcd;
	
	private String shipgstin;
	
	private String shiplegalname;
	
	private String shiptradename;
		
	private String shipadd1;
	
	private String shipadd2;
	
	private String shiplocation;
	
	private String unit;
	
	private String shippincode;
	
	private String shipstcd;
	
	private String transid;
	
	private String transname;
	
	private String transmode;
	
	private int distance;
	
	private String transdocno;
	
	private LocalDate transdocdate;
	
	private String vehicleno;
	
	private String vehicletype;
	
	
	private String docid;
	
	private LocalDate docdate;
	
	private String suptype;
	
	private String supplytype;
	
	private String documenttype;
	
	private String buyergstin;
	
	private String buyerlegalname;
	
	private String buyertradename;
	
	private String buyerpos;
	
	private String buyeradd1;
	
	private String add2;
	
	private String buyerlocation;
	
	private String buyerpincode;
	
	private String buyerstcd;
	
	private double tottaxablevalue;
	
	private double vigstamt;
	
	private double vcgstamt;
	
	private double vsgstamt;
	
	private double vothercharges;
	
	private double totinvvalue;
	
	private int slno;
	
	private String productiondesc;
	
	private String isservice;
	
	private String hsncode;
	
	private int quantity;
	
	private double unitprice;
	
	private double grossamount;
	
	private double taxablevalue;
	
	private double gstrate;
	
	private double igstamt;
	
	private double sgstamt;
	
	private double cgstamt;
	
	private double itemtotal;
	
	private String ackno;
	
	private String ackdate;
	
	private String irn;
	
	private boolean geneinvoice;
	
	private boolean genewaybill;
	
	private boolean apicall;
	
	private boolean eapicall;
	
	private boolean irnstatus;
	
	private boolean ewaystatus;
	
	private String ewbno;

	private String ewbdate;

	private String ewbvalidtill;
		
	@Lob
	@Column(columnDefinition = "BLOB")
	private String signedqrcode;
	
	
	
	@Embedded
	private CreatedUpdatedDate createdUpdatedDate = new CreatedUpdatedDate();

}
