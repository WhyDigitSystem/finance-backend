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
@Table(name = "chargerdebitnote")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChargerDebitNoteVO {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chargerdebitnotegen")
	@SequenceGenerator(name = "chargerdebitnotegen", sequenceName = "chargerdebitnoteseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "chargerdebitnoteid")
	private Long id;
	@Column(name = "gchargecode")
	private String gChargeCode;
	@Column(name = "gsac")
	private String gsac;
	@Column(name = "chargename")
	private String chargeName;
	@Column(name = "applyon")
	private String applyOn;
	@Column(name = "tax")
	private String tax;
	@Column(name = "currency")
	private String currency;
	@Column(name = "exrate")
	private BigDecimal exRate;
	@Column(name = "rate")
	private BigDecimal rate;
	@Column(name = "exempted")
	private String exempted;
	@Column(name = "fcamount")
	private BigDecimal fcAmount;
	@Column(name = "lcamount")
	private BigDecimal lcAmount;
	@Column(name = "taxablepercentage")
	private String taxablePercentage;
	@Column(name = "tlcAmount")
	private BigDecimal tlcAmount;
	@Column(name = "billamount")
	private BigDecimal billAmount;
	@Column(name = "gstpercentage")
	private BigDecimal gstPercentage;
	@Column(name = "gst")
	private String gst;
	@Column(name = "description")
	private String description;
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "debitnoteid")
	DebitNoteVO debitNoteVO;
}
