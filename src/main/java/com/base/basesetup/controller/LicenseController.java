package com.base.basesetup.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.base.basesetup.service.LicenseService;

@RestController
@RequestMapping("/api/license")
public class LicenseController {

	@Autowired
   LicenseService licenseService;

    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    // Generate license key
    @GetMapping("/generate")
    public String generateKey(@RequestParam Long companyId,
                              @RequestParam String expiryDate) throws Exception {
        LocalDate expiry = LocalDate.parse(expiryDate);
        return licenseService.generateLicenseKey(companyId, expiry);
    }

    // Validate license key
    @PostMapping("/validate")
    public boolean validateKey(@RequestParam Long companyId,@RequestBody String licenseKey) {
        return licenseService.validateLicenseKey(companyId,licenseKey);
    }
}