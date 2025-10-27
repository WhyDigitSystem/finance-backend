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
@Table(name = "clientunit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientUnitVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "clientunitgen")
	@SequenceGenerator(name = "clientunitgen", sequenceName = "clientunitseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "clientunitid")
	private Long id;
	@Column(name="unit",length = 100)
	private String unit;
	@Column(name="location",length = 100)
	private String location;
	private boolean active;
	
	@ManyToOne
	@JoinColumn(name = "clientcompanyid")
	@JsonBackReference
	private ClientCompanyVO clientCompanyVO;

}
