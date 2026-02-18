package com.base.basesetup.service;

import org.springframework.stereotype.Service;

@Service
public interface QuaterMonthService {
	
	
	int getQuaterMonthDetails(String yearType,String month);

	int getMonthNumber(String yearType, String month);

}
