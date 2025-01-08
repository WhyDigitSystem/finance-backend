package com.base.basesetup.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jobcard")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobCardVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jobcardgen")
	@SequenceGenerator(name = "jobcardgen", sequenceName = "jobcardseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "jobcardid")
	private Long id;
	@Column(name = "jobno", length = 30)
	private String jobNo;
	@Column(name = "customer", length = 50)
	private String customer;
	@Column(name = "operationclosed")
	private boolean operationClosed;
	@Column(name = "docdate")
	private LocalDate date=LocalDate.now();
	@Column(name = "salescategory", length = 50)
	private String salesCategory;
	@Column(name = "financeclosed")
	private boolean financeClosed;
	@Column(name = "salesperson", length = 50)
	private String salesPerson;
	@Column(name = "closed")
	private boolean closed;
	@Column(name = "closedon")
	private LocalDateTime closedOn;
	@Column(name = "income", precision = 10, scale = 2)
	private BigDecimal income;
	@Column(name = "expense", precision = 10, scale = 2)
	private BigDecimal expense;
	@Column(name = "profit", precision = 10, scale = 2)
	private BigDecimal profit;
	@Column(name = "remarks", length = 150)
	private String remarks;


	// default fields
	@Builder.Default
	@Column(name = "screencode", length = 5)
	private String screenCode = "JC";
	@Builder.Default
	@Column(name = "screenname", length = 20)
	private String screenName = "JOB CARD";
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "branch", length = 25)
	private String branch;
	@Column(name = "branchcode", length = 20)
	private String branchCode;
	@Column(name = "createdby", length = 25)
	private String createdBy;
	@Column(name = "modifiedby", length = 25)
	private String updatedBy;
	@Column(name = "active")
	private boolean active;
	@Column(name = "cancel")
	private boolean cancel;
	@Column(name = "cancelremarks", length = 50)
	private String cancelRemarks;
	@Column(name = "finyear", length = 5)
	private String finYear;

	@OneToMany(mappedBy = "jobCardVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	List<CostCenterJobCardVO> costCenterJobCardVO;

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
