package com.base.basesetup.entity;

import java.math.BigDecimal;

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
@Table(name="previousyearob")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreviousYearActualOBVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "previousyearobgen")
	@SequenceGenerator(name = "previousyearobgen", sequenceName = "previousyearobseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "previousyearobid")
	private Long id;
	
	@Column(name = "orgid")
	private Long orgId;
	
	@Column(name = "client",length = 100 )
	private String client;
	@Column(name = "clientcode",length = 20 )
	private String clientCode;
	@Column(name = "year",length = 10 )
	private String year;
	@Column(name = "month",length = 30 )
	private String month;
	@Column(name = "type",length = 100)
	private String type; 
	@Column(name = "amount",length = 100)
	private BigDecimal amount;
	@Column(name = "segment",length = 25)
	private String segment;
	@Column(name = "customername",length = 100)
	private String customerName;
	@Column(name = "ordervalue",length = 100)
	private BigDecimal orderValue;
	@Column(name = "existingorderplan",length = 100)
	private BigDecimal existingOrderPlan;
	@Column(name = "balanceordervalue",length = 100)
	private BigDecimal balanceOrderValue;
	@Column(name = "paymentreceived",length = 100)
	private BigDecimal paymentReceived;
	@Column(name = "yettoreceive",length = 100)
	private BigDecimal yetToReceive;
	@Column(name = "classification",length = 100)
	private String classification;
	@Column(name = "active")
	private boolean active;
	@Column(name = "createdby",length = 50)
	private String createdBy;
	@Column(name = "modifiedby",length = 50)
	private String updatedBy;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "quater",length = 5)
	private String quater;	
	private int monthsequence;
	
	@Embedded
	private CreatedUpdatedDate createdUpdatedDate= new CreatedUpdatedDate();

}
