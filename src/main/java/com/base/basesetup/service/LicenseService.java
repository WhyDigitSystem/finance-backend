package com.base.basesetup.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public interface LicenseService {



	String generateLicenseKey(Long companyId, LocalDate expiryDate) throws Exception;


	boolean validateLicenseKey(Long companyId, String licenseKey);

}
