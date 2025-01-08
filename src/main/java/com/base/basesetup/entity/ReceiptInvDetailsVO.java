package com.base.basesetup.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

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
@Table(name = "receiptinvdetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptInvDetailsVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "receiptinvdetailsgen")
	@SequenceGenerator(name = "receiptinvdetailsgen", sequenceName = "receiptinvdetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "receiptinvdetailsid")
	private Long id;

	@Column(name = "invno",length =50)
	private String invNo;

	@Column(name = "invdate")
	private LocalDate invDate;

	@Column(name = "refno",length =50)
	private String refNo;

	@Column(name = "refdate")
	private LocalDate refDate;

	@Column(name = "masterref",length =50)
	private String masterRef;

	@Column(name = "houseref",length =50)
	private String houseRef;

	@Column(name = "currency",length =10)
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

	@Column(name = "recexrate", precision = 10, scale = 2)
	private BigDecimal recExRate;

	@Column(name = "txnsettled", precision = 10, scale = 2)
	private BigDecimal txnSettled;

	@Column(name = "gainamt", precision = 10, scale = 2)
	private BigDecimal gainAmt;


	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "receiptid")
	ReceiptVO receiptVO;

}
