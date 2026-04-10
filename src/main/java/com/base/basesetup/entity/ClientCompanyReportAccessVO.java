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
@Table(name="clientreportaccess")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientCompanyReportAccessVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clientreportaccessgen")
	@SequenceGenerator(name = "clientreportaccessgen", sequenceName = "clientreportaccessseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "clientreportaccessid")
	private Long id;
	
	@Column(name = "elcode", length = 30)
	private String elCode;
	@Column(name = "description", length = 100)
	private String description;
	@Column(name = "access" )
	private boolean access;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "clientcompanyid")
	private ClientCompanyVO clientCompanyVO;

}
