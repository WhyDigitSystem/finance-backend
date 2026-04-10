package com.base.basesetup.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetDTO {

	private Long id;

	private Long orgId;

	private String category;

	private String categoryCode;

	private String assetCodeId;

	private String assetName;

	private String belongsTo;

	private String materialIdentification;

	private String manufacturePartCode;

	private String design;

	private float length;

	private float breath;

	private float height;

	private float weight;

	private int quantity;

	// private String dimUnit;

	private String manufacturer;

	private String chargableWeight;

	// private String brand;

	private String eanUpc;

	private String assetType;

	private String expectedLife;

	private String maintanencePeriod;

	private String expectedTrips;

	private String hsnCode;

	private String taxRate;

	private long skuFrom;

	private long skuTo;

	private String costPrice;

	private String sellPrice;

	private String scrapValue;

	// private boolean cancel;

	private String createdBy;

	private String cancelremarks;

	private String poNo;

	private LocalDate poDate = LocalDate.now();

	private boolean active;

	// private List<AssetItemDTO> assetItemDTO;

}
