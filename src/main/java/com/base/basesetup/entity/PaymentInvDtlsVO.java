package com.base.basesetup.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "paymentinvdtls")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentInvDtlsVO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "paymentinvdtlsgen")
	@SequenceGenerator(name = "paymentinvdtlsgen", sequenceName = "paymentinvdtlsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "paymentinvdtlsid")
	private Long id;

	@Column(name = "invno", length = 30)
	private String invNo;

	@Column(name = "invdate")
	private LocalDate invDate;

	@Column(name = "refno", length = 30)
	private String refNo;

	@Column(name = "refdate")
	private LocalDate refDate;

	@Column(name = "supplierrefno", length = 30)
	private String supplierRefNo;

	@Column(name = "supplierrefdate")
	private LocalDate supplierRefDate;

	@Column(name = "currency", length = 5)
	private String currency;

	@Column(name = "exrate", precision = 10, scale = 5)
	private BigDecimal exRate;

	@Column(name = "amount", precision = 10, scale = 2)
	private BigDecimal amount;

	@Column(name = "chargeamt", precision = 10, scale = 2)
	private BigDecimal chargeAmt;

	@Column(name = "outstanding", precision = 10, scale = 2)
	private BigDecimal outstanding;

	@Column(name = "settled", precision = 10, scale = 2)
	private BigDecimal settled;

	@Column(name = "payexrate", precision = 10, scale = 2)
	private BigDecimal payExRate;

	@Column(name = "txnsettled", precision = 10, scale = 2)
	private BigDecimal txnSettled;

	@Column(name = "gainorlossamt", precision = 10, scale = 2)
	private BigDecimal gainOrLossAmt;

	@Column(name = "remarks", length = 150)
	private String remarks;

	@Column(name = "fromdate")
	private LocalDate fromDate;

	@Column(name = "todate")
	private LocalDate toDate;

	@ManyToOne
	@JoinColumn(name = "paymentid")
	@JsonBackReference
	private PaymentVO paymentVO;

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

}
