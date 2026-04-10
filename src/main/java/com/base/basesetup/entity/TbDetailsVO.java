package com.base.basesetup.entity;

import java.math.BigDecimal;

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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbdetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TbDetailsVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "tbdetailsgen")
	@SequenceGenerator(name = "tbdetailsgen", sequenceName = "tbdetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "tbdetailsid")
	private Long id;
	@Column(name = "orgid")
	private Long orgId;
	@Column(name = "coa",length = 150)
	private String coa;
	@Column(name = "coacode",length = 50)
	private String coaCode;
	@Column(name = "clientaccountname",length = 150)
	private String clientAccountName;
	@Column(name = "clientaccountcode",length = 20)
	private String clientAccountCode;
	@Column(name = "clientcode",length = 10)
	private String clientCode;
	@Column(name = "client",length =100 )
	private String client;
	@Column(name = "tbmonth",length =30 )
	private String tbMonth;
	@Column(name = "opbalancedb",precision = 20,scale = 2)
	private BigDecimal openingBalanceDb;
	@Column(name = "opbalancecr",precision = 20,scale = 2)
	private BigDecimal openingBalanceCr;
	@Column(name = "transcredit",precision = 20,scale = 2)
	private BigDecimal transCredit;
	@Column(name = "transdebit",precision = 20,scale = 2)
	private BigDecimal transDebit;
	@Column(name = "clbalancedb",precision = 20,scale = 2)
	private BigDecimal closingBalanceDb;
	@Column(name = "clbalancecr",precision = 20,scale = 2)
	private BigDecimal closingBalanceCr;
	@Column(name = "finyear",length =10)
	private String finYear;
	@Column(name = "remarks",length =100)
	private String remarks;
	

	@ManyToOne
	@JoinColumn(name = "tbheaderid")
	@JsonBackReference
	private TbHeaderVO tbHeaderVO;

}
