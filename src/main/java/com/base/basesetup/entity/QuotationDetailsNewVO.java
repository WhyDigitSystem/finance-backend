package com.base.basesetup.entity;

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
@Table(name = "quotationdetailsnew")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuotationDetailsNewVO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "quotationdetailsnewgen")
	@SequenceGenerator(name = "quotationdetailsnewgen", sequenceName = "quotationdetailsnewseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "quotationdetailsnewid")
	private Long id;

	
	private String description;

	private Long unit;

	private Long pricre;

	private Long total;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "quotationnewid")
	private QuotationNewVO quotationNewVO;

}
