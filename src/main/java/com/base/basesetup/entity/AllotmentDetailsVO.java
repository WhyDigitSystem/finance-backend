package com.base.basesetup.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "allotmentdetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllotmentDetailsVO {

	 @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tdriverdocsgen")
	    @SequenceGenerator(name = "tdriverdocsgen", sequenceName = "tdriverdocsseq", initialValue = 1000000001, allocationSize = 1)
	    @Column(name = "tdriverdocumentsid")
	    private Long id;

	    @Column(name = "projectcode")
	    private String projectCode;   

	    @Column(name = "part")
	    private Long part;

	    @Column(name = "kitdesc")
	    private String kitDesc;   

	    @Column(name = "kitno")
	    private String kitNo;
	    @Column(name = "inventory")
	    private String inventory;

	    @Column(name = "schedule")
	    private String schedule;       

	    @Column(name = "month")
	    private Long month;

	    @Column(name = "day")
	    private Long day;
	    
	    @Column(name = "boxesreq")
	    private Long boxesReq;

	    @Column(name = "shortage")
	    private Long shortage;

	    @Column(name = "short")
	    private Long shorted;
	    
	    @Column(name = "adherence")
	    private String adherence;
	    
	    @Column(name = "allot")
	    private Long allot;

	    
		@ManyToOne
	    @JoinColumn(name = "allotmentid", nullable = false)
		@JsonBackReference
	    private AllotmentVO allotmentVO;
}
