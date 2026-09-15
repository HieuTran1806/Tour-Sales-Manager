package org.example.gui.helper;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
/**
 *
 * @author Nhat
 */
public class DateHelper {
    private static final String DEFAULT_FORMAT = "dd/MM/yyyy";

    public static String dateToString(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
        return sdf.format(date);
    }


    public static Date stringToDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
            sdf.setLenient(false);
            return sdf.parse(dateString);
        } catch (ParseException e) {
            System.out.println("Lỗi ép kiểu ngày: " + dateString);
            return null;
        }
    }
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String toString(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(FORMATTER);
    }

    public static LocalDate toLocalDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateString, FORMATTER);
        } catch (DateTimeParseException e) {
            System.out.println("Lỗi sai định dạng ngày: " + dateString);
            return null;
        }
    }

    public static java.util.Date toUtilDate(LocalDate localDate) {
        if (localDate == null) return null;
        return java.sql.Date.valueOf(localDate);
    }

    public static LocalDate toLocalDateFromUtil(java.util.Date utilDate) {
        if (utilDate == null) return null;
        return new java.sql.Date(utilDate.getTime()).toLocalDate();
    }
}
