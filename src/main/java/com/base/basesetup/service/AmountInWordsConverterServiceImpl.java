package com.base.basesetup.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class AmountInWordsConverterServiceImpl implements AmountInWordsConverterService{

	private static final String[] units = { "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
			"Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen",
			"Nineteen" };

	private static final String[] tens = { "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty",
			"Ninety" };

	private static String convertBelowThousand(int number) {
		if (number < 20) {
			return units[number];
		} else if (number < 100) {
			return tens[number / 10] + (number % 10 != 0 ? " " + units[number % 10] : "");
		} else {
			return units[number / 100] + " Hundred"
					+ (number % 100 != 0 ? " " + convertBelowThousand(number % 100) : "");
		}
	}

	public String convert(BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			return "Zero Only";
		}

		StringBuilder result = new StringBuilder();

		// Extract rupees part (integer part)
		long number = amount.longValue(); // integer part of the amount
		int crore = (int) (number / 1_00_00_000);
		number %= 1_00_00_000;

		int lakh = (int) (number / 1_00_000);
		number %= 1_00_000;

		int thousand = (int) (number / 1_000);
		number %= 1_000;

		int hundred = (int) (number);

		// Add the rupees part (crore, lakh, thousand, hundred)
		if (crore > 0) {
			result.append(convertBelowThousand(crore)).append(" Crore ");
		}
		if (lakh > 0) {
			result.append(convertBelowThousand(lakh)).append(" Lakh ");
		}
		if (thousand > 0) {
			result.append(convertBelowThousand(thousand)).append(" Thousand ");
		}
		if (hundred > 0) {
			result.append(convertBelowThousand(hundred));
		}

		String amountInWords = result.toString().trim();

		// Handling the paise (decimal part)
		BigDecimal paise = amount.remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(100)); // Extract paise (up to 2
																								// decimal places)

		if (paise.compareTo(BigDecimal.ZERO) > 0) {
			int paiseInt = paise.intValue();
			if (paiseInt > 0) {
				// Convert paise to words
				String paiseInWords = convertBelowThousand(paiseInt);
				amountInWords += " and " + paiseInWords + " Paise";
			}
		}

		return amountInWords + " Only";
	}
}
