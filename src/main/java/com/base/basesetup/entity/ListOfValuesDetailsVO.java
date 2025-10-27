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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="listofvaluesdetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListOfValuesDetailsVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "listofvaluesdetailsgen")
	@SequenceGenerator(name = "listofvaluesdetailsgen", sequenceName = "listofvaluesdetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "listofvaluesdetailsid")
	private Long id;
	
	
	@Column(name = "valuesdescription", length = 150)
	private String valuesDescription;
	
	private boolean active;
	
	
	@ManyToOne
	@JoinColumn(name = "listofvaluesid")
	@JsonBackReference
	private ListOfValuesVO listOfValuesVO;

}
