package com.base.basesetup.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
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

import org.springframework.format.annotation.DateTimeFormat;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "costestimation")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CostEstimationVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "costestimationgen")
	@SequenceGenerator(name = "costestimationgen", sequenceName = "costestimationseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "costestimationid")
	private Long id;
	
	@Column(name = "docid", length = 50)
	private String docId;

	@Column(name = "docdate")
//	@Builder.Default
	private LocalDate docDate;
	
	@Column(name = "employeename")
	private String employeeName;
	
	@Column(name = "employeecode")
	private String employeeCode;
	
	@Column(name = "branch", length = 25)
	private String branch;

	@Column(name = "branchcode", length = 20)
	private String branchCode;


	@Column(name = "createdby", length = 25)
	private String createdBy;

	@Column(name = "modifyby", length = 25)
	private String updatedBy;

	@Column(name = "active")
	private boolean active = true;

	@Column(name = "cancel")
	private boolean cancel  =false;

	@Column(name = "cancelremarks", length = 25)
	private String cancelRemarks;

	@Column(name = "finyear", length = 5)
	private String finYear;
	
	@Column(name = "department")
	private String department;

	@Column(name = "screencode", length = 5)
	@Builder.Default
	private String screenCode = "CE";

	@Column(name = "screenname", length = 25)
	@Builder.Default
	private String screenName = "COST ESTIMATION";	

	@Column(name = "orgid")
	private Long orgId;
	
	@Column(name = "totalamount", precision = 10, scale = 2)
	private BigDecimal totalAmount;
	
	@Column(name = "status", length = 20)
	private String status;
	
	@Column(name = "fromdate")
	private LocalDate fromDate;
	@Column(name = "todate")
	private LocalDate toDate;
	
	@Column(name = "approvalremarks")
	private String approvalRemarks;
	
	
//	APPROVED
	@Column(name = "approvestatus", length = 20)
	private String approveStatus;
	@Column(name = "approveby", length = 20)
	private String approveBy;
	@DateTimeFormat(pattern = "dd-MM-yyyy hh:mm:ss a")
	@Column(name = "approveon")
	private String approveOn;
	@Column(name="amountinwords")
	private String amountInWords;
	@Column(name = "purvoucherno", length = 50)
	private String purVoucherNo;
	@Column(name = "purvoucherdate")
	private LocalDate purVoucherDate;
	

	@OneToMany(mappedBy = "costEstimationVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	List<CostEstimationDetailsVO> costEstimationDetailsVO;


	@JsonGetter("active")
	public String getActive() {
		return active ? "Active" : "In-Active";
	}

	// Optionally, if you want to control serialization for 'cancel' field similarly
	@JsonGetter("cancel")
	public String getCancel() {
		return cancel ? "T" : "F";
	}

	
	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
