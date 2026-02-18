package com.base.basesetup.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class AmountUtil {

    private static final Locale INDIA = new Locale("en", "IN");


    public static String formatInteger(BigDecimal amount) {
        if (amount == null) return "0";
        NumberFormat formatter = NumberFormat.getInstance(INDIA);
        formatter.setMaximumFractionDigits(0); // ❌ no decimals
        formatter.setMinimumFractionDigits(0);
        return formatter.format(amount);
    }
}