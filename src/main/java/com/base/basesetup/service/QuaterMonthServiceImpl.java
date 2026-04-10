package com.base.basesetup.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class QuaterMonthServiceImpl implements QuaterMonthService {

	
	
	private static final Map<String, Integer> fyQuarterMap = new HashMap<>();
    private static final Map<String, Integer> cyQuarterMap = new HashMap<>();

    static {
        // Financial Year (FY: April - March)
        fyQuarterMap.put("April", 1);
        fyQuarterMap.put("May", 1);
        fyQuarterMap.put("June", 1);
        fyQuarterMap.put("July", 2);
        fyQuarterMap.put("August", 2);
        fyQuarterMap.put("September", 2);
        fyQuarterMap.put("October", 3);
        fyQuarterMap.put("November", 3);
        fyQuarterMap.put("December", 3);
        fyQuarterMap.put("January", 4);
        fyQuarterMap.put("February", 4);
        fyQuarterMap.put("March", 4);

        // Calendar Year (CY: January - December)
        cyQuarterMap.put("January", 1);
        cyQuarterMap.put("February", 1);
        cyQuarterMap.put("March", 1);
        cyQuarterMap.put("April", 2);
        cyQuarterMap.put("May", 2);
        cyQuarterMap.put("June", 2);
        cyQuarterMap.put("July", 3);
        cyQuarterMap.put("August", 3);
        cyQuarterMap.put("September", 3);
        cyQuarterMap.put("October", 4);
        cyQuarterMap.put("November", 4);
        cyQuarterMap.put("December", 4);
    }

    @Override
    public int getQuaterMonthDetails(String yearType, String month) {
        if (month == null || yearType == null) {
            throw new IllegalArgumentException("Month and yearType cannot be null");
        }

        if (yearType.equalsIgnoreCase("FY")) {
            return fyQuarterMap.getOrDefault(month, -1);
        } else if (yearType.equalsIgnoreCase("CY")) {
            return cyQuarterMap.getOrDefault(month, -1);
        } else {
            throw new IllegalArgumentException("Invalid year type (Use 'FY' for Financial Year or 'CY' for Calendar Year)");
        }
    }
    
    @Override
    public  int getMonthNumber(String yearType, String month) {
        // Convert the month to Sentence case
        month = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();
        
        int monthNumber = 0;
        
        // Mapping for Financial Year (FY)
        if ("FY".equalsIgnoreCase(yearType)) {
            switch (month) {
                case "April":
                    monthNumber = 1;
                    break;
                case "May":
                    monthNumber = 2;
                    break;
                case "June":
                    monthNumber = 3;
                    break;
                case "July":
                    monthNumber = 4;
                    break;
                case "August":
                    monthNumber = 5;
                    break;
                case "September":
                    monthNumber = 6;
                    break;
                case "October":
                    monthNumber = 7;
                    break;
                case "November":
                    monthNumber = 8;
                    break;
                case "December":
                    monthNumber = 9;
                    break;
                case "January":
                    monthNumber = 10;
                    break;
                case "February":
                    monthNumber = 11;
                    break;
                case "March":
                    monthNumber = 12;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid month: " + month);
            }
        }

        // Mapping for Calendar Year (CY)
        else if ("CY".equalsIgnoreCase(yearType)) {
            switch (month) {
                case "January":
                    monthNumber = 1;
                    break;
                case "February":
                    monthNumber = 2;
                    break;
                case "March":
                    monthNumber = 3;
                    break;
                case "April":
                    monthNumber = 4;
                    break;
                case "May":
                    monthNumber = 5;
                    break;
                case "June":
                    monthNumber = 6;
                    break;
                case "July":
                    monthNumber = 7;
                    break;
                case "August":
                    monthNumber = 8;
                    break;
                case "September":
                    monthNumber = 9;
                    break;
                case "October":
                    monthNumber = 10;
                    break;
                case "November":
                    monthNumber = 11;
                    break;
                case "December":
                    monthNumber = 12;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid month: " + month);
            }
        } else {
            throw new IllegalArgumentException("Invalid yearType: " + yearType);
        }

        return monthNumber;
    }

}
