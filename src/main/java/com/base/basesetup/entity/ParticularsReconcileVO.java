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
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "particularsreconcile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticularsReconcileVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "particularsreconcilegen")
	@SequenceGenerator(name = "particularsreconcilegen", sequenceName = "particularsreconcileseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "particularsreconcileid")
	private Long id;
	@Column(name="voucherno",length = 30)
	private String voucherNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name="voucherdate")
	private LocalDate voucherDate;
	@Column(name="chequeno",length = 30)
	private String chequeNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name="chequedate")
	private LocalDate chequeDate;
	@Column(name="deposit",precision = 10,scale = 2)
	private BigDecimal deposit;
	@Column(name="withdrawal",precision = 10,scale = 2)
	private BigDecimal withdrawal;
	@Column(name="bankref",length = 30)
	private String bankRef;
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="reconcilebankid")
	ReconcileBankVO reconcileBankVO;
	
	

}
