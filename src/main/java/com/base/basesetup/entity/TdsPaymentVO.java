package com.base.basesetup.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tdspayment")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class TdsPaymentVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tdspaymentgen")
	@SequenceGenerator(name = "tdspaymentgen", sequenceName = "tdspaymenteq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "tdsPaymentid")
	private Long id;
	@Column(name = "tdswithholding",length =10)
	private String tdsWithHolding;
	@Column(name = "tdswithholdingper",precision =10,scale = 2)
	private BigDecimal tdsWithHoldingPer;
	@Column(name = "section",length =255)
	private String section;
	@Column(name = "totaltds",precision =10,scale = 2)
	private BigDecimal totTdsWhAmnt;
	
//	@ManyToOne
//	@JsonBackReference
//	@JoinColumn(name = "paymentid")
//	private PaymentVO paymentVO;
}
