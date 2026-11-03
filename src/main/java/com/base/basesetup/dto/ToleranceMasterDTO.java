package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToleranceMasterDTO {
	
    private Long toleranceid;

    private BigDecimal lowerTolerance;

    private BigDecimal upperTolerance;

    private BigDecimal tolerance;

    private boolean active;

    private String createdBy;

    private String updatedBy;

    private boolean cancel;

    private String screenCode;

    private String screenName;

}
