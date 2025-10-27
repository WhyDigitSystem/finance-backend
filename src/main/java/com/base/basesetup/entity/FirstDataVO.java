package com.base.basesetup.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "firstdata")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FirstDataVO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "firstdatagen")
	@SequenceGenerator(name = "firstdatagen", sequenceName = "firstdataseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "firstdataid")
	private Long id;

	@Column(name ="docdate")
	private LocalDate docDate;
	@Column(name ="accountnumber")
	private Long accountNumber;
	@Column(name ="accountname")
	private String accountName;
	private String description; 
	private String Category;
	private BigDecimal debit;
	private BigDecimal credit;
	private String currency;
	private BigDecimal balance;
	@Column(name ="transactiontype")	
	private String transactionType;
	@Column(name ="createdby")
	private String createdBy;
	@Column(name ="modifiedby")
	private String updatedBy;
	
	
	
	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
	
}
