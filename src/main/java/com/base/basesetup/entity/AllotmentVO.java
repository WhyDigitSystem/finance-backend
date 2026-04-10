package com.base.basesetup.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "allotment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllotmentVO {

	 @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "allotmentgen")
	    @SequenceGenerator(name = "allotmentgen", sequenceName = "allotmentseq", initialValue = 1000000001, allocationSize = 1)
	    @Column(name = "allotmentid")
	    private Long id;

	    @Column(name = "supplier")
	    private String supplier;

	    @Column(name = "customer")
	    private String customer;

	    @Column(name = "mode")
	    private String mode;

	    @Column(name = "startdate")
	    private LocalDate startDate;

	    @Column(name = "enddate")
	    private LocalDate endDate;

	    @Column(name = "active")
	    private boolean active = true;

	    @Column(name = "createdby")
	    private String createdBy;

	    @Column(name = "modifiedby")
	    private String updatedBy;
	    
	    

	    @Column(name = "orgid")
	    private Long orgId;

	    @Column(name = "cancel")
	    private boolean cancel;

	    @Column(name = "branchcode")
	    private String branchCode;

	    @Column(name = "branchname")
	    private String branchName;

	    @OneToMany(mappedBy = "allotmentVO", cascade = CascadeType.ALL)
	    @JsonManagedReference
	    private List<AllotmentDetailsVO> allotmentDetailsVO;

	    @Embedded
	    private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
}
