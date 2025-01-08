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
@Table(name = "costdebitnotetaxprtcul")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TdsCostDebitNoteVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "costdebitNotetaxprtculgen")
	@SequenceGenerator(name = "costdebitNotetaxprtculgen", sequenceName = "costdebitNotetaxprtculseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "costdebitnotetaxprtculid")
	private Long id;
	@Column(name = "tdswithholding", length = 10)
	private String tdsWithHolding;
	@Column(name = "tdswithholdingper", precision = 10, scale = 2)
	private BigDecimal tdsWithHoldingPer;
	@Column(name = "section", length = 10)
	private String section;
	@Column(name = "totaltds", precision = 10, scale = 2)
	private BigDecimal totTdsWhAmnt;

	@ManyToOne
	@JoinColumn(name = "costdebitnoteid")
	@JsonBackReference
	private CostDebitNoteVO costDebitNoteVO;

}
