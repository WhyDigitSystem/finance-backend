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
@Table(name="automationdetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutomationDetailsVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "automationdetailsgen")
	@SequenceGenerator(name = "automationdetailsgen", sequenceName = "automationdetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "automationdetailsgenid")
	private Long id;
	
	@Column(name="maingroup")
	private String mainGroup;
	
	@Column(name = "subgroup", length = 150)
	private String subGroup;
	
	@Column(name = "accountcode", length = 150)
	private String accountCode;
	
	@Column(name = "accountname", length = 150)
	private String accountName;
	
	
	@ManyToOne
	@JoinColumn(name = "automationid")
	@JsonBackReference
	private AutomationVO automationVO;
		
}
