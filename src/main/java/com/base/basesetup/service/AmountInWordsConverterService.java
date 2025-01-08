package com.base.basesetup.service;

import org.springframework.stereotype.Service;

@Service
public interface AmountInWordsConverterService {

	String convert(Long amount);
	
	

}
