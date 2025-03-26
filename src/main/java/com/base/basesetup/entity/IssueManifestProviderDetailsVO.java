package com.base.basesetup.entity;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mimdetails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueManifestProviderDetailsVO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "mimdetailsgen")
	@SequenceGenerator(name = "mimdetailsgen", sequenceName = "mimdetailsseq", initialValue = 1000000001, allocationSize = 1)
	@Column(name = "mimdetailsid")
	private Long id;
	@Column(name = "kitid")
	private String kitId;
	@Column(name = "kitname")
	private String kitName;
	@Column(name = "kitqty")
	private Long kitQty;
	@Column(name = "hsncode")
	private Long hsnCode;
	@Column(name = "assetcode")
	private String assetCode;
	@Column(name = "asset")
	private String asset;
	@Column(name = "assetqty")
	private Long assetQty;

	@ManyToOne
	@JsonBackReference
	@JoinColumn(name = "mimid")
	private IssueManifestProviderVO issueManifestProviderVO;
}
