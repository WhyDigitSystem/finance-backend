package com.base.basesetup.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllotmentDetailsResponseDTO {

	private String projectCode;   
    private Long part;
    private String inventory;
    private String schedule;       
    private Long month;
    private Long day;
    private Long boxesReq;
    private Long shortage;
    private Long shorted;
    private String adherence;
    private Long allot;
    private String kitDesc;   
    private String kitNo;
    private String partNo;
}
