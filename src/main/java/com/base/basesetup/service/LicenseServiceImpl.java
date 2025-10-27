package com.base.basesetup.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import com.base.basesetup.util.InternetTimeUtil;

@Service
public class LicenseServiceImpl implements LicenseService {
	
	private final String appKey = "LAz2aeV0irbbTrjtl3uKAAXeVJig91kjbracM3DWfO8=";
	
	// Generate license key
	@Override
	public String generateLicenseKey(Long companyId, LocalDate expiryDate) throws Exception {

	    // Data = companyId|expiryDate
	    String data = companyId + "|" + expiryDate;
	    
	    // Encrypt with AES
	    String encryptedPayload = encryptWithAES(data, appKey);

	    // Generate checksum (first 6 chars of SHA-256 hash)
	    String checksum = DigestUtils.sha256Hex(data).substring(0, 6).toUpperCase();

	    // Combine encrypted part + checksum
	    String rawKey = encryptedPayload + checksum;

	    // Format with hyphens every 5 chars like XXXXX-XXXXX
	    return rawKey.replaceAll("(.{5})(?=.)", "$1-");
	}
	
	private static String encryptWithAES(String data, String base64Key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
	
	

	@Override
	public boolean validateLicenseKey(Long companyId, String licenseKey) {
	    try {
	        String cleanKey = licenseKey.replace("-", "");

	        if (cleanKey.length() < 6) return false;

	        // Extract encrypted + checksum
	        String encryptedPart = cleanKey.substring(0, cleanKey.length() - 6);
	        String checksumPart = cleanKey.substring(cleanKey.length() - 6);

	     // 🔑 Decrypt instead of plain Base64 decode
	        String decodedData = decryptWithAES(encryptedPart, appKey);
	        // Decode back original data
//	        String decodedData = new String(
//	                Base64.getUrlDecoder().decode(encryptedPart),
//	                StandardCharsets.UTF_8
//	        );

	        // Recalculate checksum
	        String expectedChecksum = DigestUtils.sha256Hex(decodedData)
	                .substring(0, 6)
	                .toUpperCase();

	        if (!expectedChecksum.equals(checksumPart)) {
	            return false; // tampered or wrong checksum
	        }

	        // Extract companyId + expiryDate
	        String[] parts = decodedData.split("\\|");
	        if (parts.length != 2) return false;

	        Long embeddedCompanyId = Long.parseLong(parts[0]);
	        LocalDate expiryDate = LocalDate.parse(parts[1]);

	        LocalDate currentDate = InternetTimeUtil.getInternetDate();
	        // ✅ Verify both companyId and expiry date
	        return embeddedCompanyId.equals(companyId) && !expiryDate.isBefore(currentDate);
	    } catch (Exception e) {
	        return false; // invalid key format
	    }
	}
	
	private static String decryptWithAES(String base64EncryptedData, String base64Key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(base64EncryptedData));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }


}
