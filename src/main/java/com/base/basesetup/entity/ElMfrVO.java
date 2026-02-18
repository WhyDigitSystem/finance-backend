package com.base.basesetup.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "elmfr")	
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElMfrVO {

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "elmfrgen")
	@SequenceGenerator(name = "elmfrgen", sequenceName = "elmfrseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "elmfrid")
	private Long id;
	
	private String description;
	@Column(name ="elcode")
	private String elCode;
	@Column(name ="orgid")
	private Long orgId;
	@Column(name ="createdby")
	private String createdBy;
	@Column(name ="modifiedby")
	private String updatedBy;
	private String remarks;
	
	
	@Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
