package com.base.basesetup.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "customeroutstanding")
@Data
public class CustomerOutstandingVO {
	
	@Id
    private String subledgercode;
    private String subledgername;
    private BigDecimal creditlimit;
    private BigDecimal totaldue;
    private Integer outpercentage;

}
