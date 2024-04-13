package com.assigment.eventbooking.utils;

import com.assigment.eventbooking.exceptions.InvalidDateException;
import com.assigment.eventbooking.exceptions.InvalidDateFormatException;
import com.assigment.eventbooking.exceptions.PasswordTooWeakException;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class Verification {
    public static void dateChecker(int year, int month, int date) throws InvalidDateFormatException, InvalidDateException {
        String regexYear = "^20[0-2][0-9]$";
        String regexMonth = "^(([1-9])|(1[0-2]))$";
        String regexDay = "^(3[01]|[12][0-9]|0?[1-9])$";
        if (!Pattern.matches(regexYear, String.valueOf(year)) || !Pattern.matches(regexMonth, String.valueOf(month)) || !Pattern.matches(regexDay, String.valueOf(date))) {
            throw new InvalidDateFormatException("Invalid date format");
        }
        if (LocalDate.of(year, month, date).isBefore(LocalDate.now())) {
            throw new InvalidDateException("Invalid date");

        }
    }
    public static void passwordChecker(String password) throws PasswordTooWeakException {
        String regex = "^(?=.*[0-9])" + "(?=.*[a-z])" + "(?=.*[A-Z])" + "(?=.*[@#$%^&-+=()])" + "(?=\\S+$).{8,20}$";
        if (!Pattern.matches(regex, password))
            throw new PasswordTooWeakException("Password too weak");
    }


}
