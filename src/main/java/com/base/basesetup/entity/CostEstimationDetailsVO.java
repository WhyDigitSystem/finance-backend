package com.base.basesetup.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "costestimationdetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostEstimationDetailsVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "costestimationdetailsgen")
	@SequenceGenerator(name = "costestimationdetailsgen", sequenceName = "costestimationdetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "costestimationdetailsid")
	private Long id;

	@Column(name = "category")
	private String category;

	@Column(name = "particulars")
	private String particulars;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "amount", precision = 10, scale = 2)
	private BigDecimal amount;
	
	@Lob
	@Column(name = "image", columnDefinition = "LONGBLOB") // Ensure the column is LONGBLOB
	private byte[] image;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "costestimationid")
	CostEstimationVO costEstimationVO;

}