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
@Table(name = "pyarap")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreviousYearARAPVO {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "pyarapgen")
    @SequenceGenerator(name = "pyarapgen", sequenceName = "pyarapseq", initialValue = 1000000001, allocationSize = 1)
    @Column(name = "pyarapid")
    private Long id;

    @Column(name = "year", length = 10)
    private String year;

    @Column(name = "month", length = 50)
    private String month;
    
    @Column(name = "orgid")
    private Long orgId;

    @Column(name = "client", length = 150)
    private String client;
    
    @Column(name = "clientcode", length = 50)
    private String clientCode;
    
    @Column(name = "createdby", length = 50)
    private String createdBy;

    @Column(name = "modifiedby", length = 50)
    private String updatedBy;

    @Column(name = "cancelremarks", length = 150)
    private String cancelRemarks;

    private boolean cancel = false;

    private boolean active = true;
    
    @Column(name = "quater",length = 5)
	private String quater;
	
	private int monthsequence;
	
	@Column(name = "type",length = 5)
	private String type;

    @Column(name = "supplier", length = 100)
    private String supplier;

    @Column(name = "paymentterms", length = 150)
    private String paymentTerms;

    @Column(name = "grosspurchase")
    private BigDecimal grossPurchase;

    @Column(name = "noofmonthpurchase")
    private Integer noOfMonthPurchase;

    @Column(name = "outstanding", precision = 15, scale = 2)
    private BigDecimal outStanding;
    
    @Column(name = "paymentperiod")
    private Integer paymentPeriod;
    
    @Column(name = "slab1", precision = 15, scale = 2)
    private BigDecimal slab1;
    
    @Column(name = "slab2", precision = 15, scale = 2)
    private BigDecimal slab2;
    
    @Column(name = "slab3", precision = 15, scale = 2)
    private BigDecimal slab3;
    
    @Column(name = "slab4", precision = 15, scale = 2)
    private BigDecimal slab4;
    
    @Column(name = "slab5", precision = 15, scale = 2)
    private BigDecimal slab5;
    
    @Column(name = "slab6", precision = 15, scale = 2)
    private BigDecimal slab6;

   
    
    @Embedded
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
