package com.base.basesetup.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.base.basesetup.dto.CreatedUpdatedDate;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "paymentvoucher")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentVoucherVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "paymentvouchergen")
	@SequenceGenerator(name = "paymentvouchergen", sequenceName = "paymentvoucherseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "paymentvoucherid")
	private Long id;

	@Column(name = "vehiclesubtype", length = 30)
	private String vehicleSubType;
	@Column(name = "referenceno", length = 10)
	private String referenceNo;

	@Column(name = "docid", length = 50)
	private String docId;
	@Column(name = "referencedate")
	private LocalDate referenceDate;
	@Column(name = "exrate", precision = 10, scale = 2)
	private BigDecimal exRate;
	@Column(name = "docdate")
	private LocalDate docDate ;
	@Column(name = "chequeno", length = 25)
	private String chequeNo;
	@Column(name = "chequedate")
	private LocalDate chequeDate;
	@Column(name = "chequebank", length = 50)
	private String chequeBank;
	@Column(name = "remarks", length = 50)
	private String remarks;
	@Column(name = "finyear")
	private String finYear;
	// default fields
	@Builder.Default
	@Column(name = "screencode", length = 5)
	private String screenCode = "PV";
	@Builder.Default
	@Column(name = "screenname", length = 20)
	private String screenName = "PAYMENT VOUCHER";

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

	@Column(name = "ipno", length = 15)
	private String ipNo;
	@Column(name = "latitude", length = 100)
	private String latitude;
	@Column(name = "totaldebitamount", precision = 10, scale = 2)
	private BigDecimal totalDebitAmount;
	@Column(name = "totalcreditamount", precision = 10, scale = 2)
	private BigDecimal totalCreditAmount;

	@Column(name = "currency")
	private String currency;

	@OneToMany(mappedBy = "paymentVoucherVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	List<ParticularsPaymentVoucherVO> particularsPaymentVoucherVO;

	@OneToMany(mappedBy = "paymentVoucherVO", cascade = CascadeType.ALL)
	@JsonManagedReference
	List<PaymentSummaryVO> paymentSummaryVO;

	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();

	@PrePersist
	private void setDefaultFinyr() {
		// Execute the logic to set the default value for finyr
		String fyFull = calculateFinyr();
		this.finYear = fyFull;
	}

	private String calculateFinyr() {
		// Logic to calculate finyr based on the provided SQL query
		String currentMonthDay = LocalDate.now().format(DateTimeFormatter.ofPattern("MMdd"));
		String fyFull = (currentMonthDay.compareTo("0331") > 0)
				? LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"))
				: LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yyyy"));
		return fyFull;
	}
}
