package com.base.basesetup.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public interface EmailServiceAuto {

	List<Map<String, String>> getAvailableFiles();

	String getWatchDirectory();

	byte[] getFileContent(String filename) throws IOException;

	void sendSelectedEmails(List<String> employeeCodes, String bccAddress);

	List<Map<String, Object>> getEmployeeEmail(Long orgId, String employeeCodeOrEmail);

	void sendSelectedAutoEmails(List<String> singletonList, List<String> singletonList2);

}
