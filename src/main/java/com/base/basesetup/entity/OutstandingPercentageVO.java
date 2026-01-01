package com.base.basesetup.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="outstandingemail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutstandingPercentageVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "outstandingemailgen")
	@SequenceGenerator(name = "outstandingemailgen", sequenceName = "outstandingemailseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name="outstandingemailid")
	private Long id;
	
	private int percentage;
	
	@Column(name="createdby")
	private String createdBy;
	
	@Column(name="modifiedby")
	private String modifiedBy;
	
	private Long orgId;
	

}
