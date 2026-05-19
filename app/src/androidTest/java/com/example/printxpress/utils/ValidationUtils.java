package com.example.print.utils;

import android.util.Patterns;

public class ValidationUtils {

    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        // At least 6 characters
        return password != null && password.length() >= 6;
    }

    public static boolean isValidPhone(String phone) {
        // Simple phone validation (10 digits)
        return phone != null && phone.matches("\\d{10}");
    }

    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    public static boolean isValidQuantity(String qty) {
        if (isEmpty(qty)) return false;
        try {
            int q = Integer.parseInt(qty);
            return q > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
