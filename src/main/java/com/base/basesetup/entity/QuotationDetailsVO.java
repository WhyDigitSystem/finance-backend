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
@Table(name = "quotationDetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuotationDetailsVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "quotationDetailsgen")
	@SequenceGenerator(name = "quotationDetailsgen", sequenceName = "quotationDetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "quotationDetailsid")
	private Long id;
	@Column(name="description")
	private String description;
	@Column(name="quantity",precision = 10, scale = 2)
    private BigDecimal quantity;
	@Column(name="rate",precision = 10, scale = 2)
    private BigDecimal rate;
	@Column(name="amount",precision = 10, scale = 2)
    private BigDecimal amount;
	@Column(name="baseamount",precision = 10, scale = 2)
    private BigDecimal baseAmount;
	@Column(name="tax",precision = 10, scale = 2)
    private BigDecimal tax;
	@Column(name="taxamount",precision = 10, scale = 2)
    private BigDecimal taxAmount;
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "quotationid")
	private QuotationVO quotationVO;

}

