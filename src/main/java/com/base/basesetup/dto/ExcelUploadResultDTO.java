package com.base.basesetup.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelUploadResultDTO {

	private int totalExcelRows;
	private int successfulUploads;
	private int unsuccessfulUploads;
	private List<String> failureReasons=new ArrayList<>();
	
	public void addFailureReason(String reason) {
        this.failureReasons.add(reason);
    }

	

}
