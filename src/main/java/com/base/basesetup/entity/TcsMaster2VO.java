package com.base.basesetup.entity;

import java.time.LocalDate;

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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tcsmaster2")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TcsMaster2VO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "tcsmaster2gen")
	@SequenceGenerator(name = "tcsmaster2gen", sequenceName = "tcsmaster2seq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "tcsmaster2id")
	private Long tcsMaster2Id;
	@Column(name = "Serialno",length =50)
	private long SerialNo;
	@Column(name = "fromdate")
	private LocalDate fromDate;
	@Column(name = "todate")
	private LocalDate toDate;
	@Column(name = "tcspercentage",precision =2,scale = 3)
	private float tcsPercentage;
	
	@Embedded
	@Builder.Default
	private CreatedUpdatedDate commonDate = new CreatedUpdatedDate();
	
}
