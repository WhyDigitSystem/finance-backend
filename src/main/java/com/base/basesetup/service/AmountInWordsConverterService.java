package com.base.basesetup.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public interface AmountInWordsConverterService {

	String convert(BigDecimal amount);
	

	
	

}
