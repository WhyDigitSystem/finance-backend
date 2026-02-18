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
@Table(name = "userloginclientaccess")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserLoginClientAccessVO {

	@Id
	@SequenceGenerator(name = "userloginclientaccessgen", sequenceName = "userloginclientaccessseq", initialValue = 1000000001, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userloginclientaccessgen")
	@Column(name = "userloginclientaccessid")
	private Long id;
	@Column(name = "clientname")
	private String clientName;
	@Column(name = "clientcode")
	private String clientCode;

	@ManyToOne
	@JoinColumn(name = "usersid")
	@JsonBackReference
	private UserVO userVO;

}
