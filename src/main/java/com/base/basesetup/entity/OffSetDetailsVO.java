package com.base.basesetup.entity;

import java.math.BigDecimal;
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
@Table(name="GST_ARAPOFFSETDTLID")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OffSetDetailsVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "offsetgen")
	@SequenceGenerator(name = "offsetgen", sequenceName = "offsetseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "offsetid")
	private Long id;
	
	@Column(name = "invoiceno",length = 30)
	private String invoiceNo;
	
	@Column(name = "invoicedate")
	private LocalDate invoiceDate;
	
	@Column(name = "refno",length = 30)
	private String refNo;
	
	@Column(name = "invcurrency",length = 30)
	private String invCurrency;
	
	@Column(name = "invexrate",precision =10,scale = 2)
	private BigDecimal invExRate;
	
	@Column(name = "arapamount",precision =10,scale = 2)
	private BigDecimal arApAmount;
	@Column(name = "arapoutstanding",precision =10,scale = 2)
	private BigDecimal arApOutStanding;
	@Column(name = "arapsettled",precision =10,scale = 2)
	private BigDecimal arApSettled;
	
	@Column(name = "settledexrate",precision =10,scale = 2)
	private BigDecimal settledExRate;
	
	@Column(name = "gainorloss",precision =10,scale = 2)
	private BigDecimal gainOrLoss;
	
	@Column(name = "remarks",length = 150)
	private BigDecimal remarks;
	
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "offsetid")
	OffSetVO offSetVO;

}
