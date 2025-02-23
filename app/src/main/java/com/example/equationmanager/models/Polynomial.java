package com.example.equationmanager.models;

import java.util.ArrayList;
import java.util.List;

public class Polynomial {
    private int id;
    private int degree;
    private String coefficients; // שמור כמחרוזת "1.0,2.0,-3.5"

    public Polynomial(int id, int degree, String coefficients) {
        this.id = id;
        this.degree = degree;
        this.coefficients = coefficients;
    }

    public int getId() {
        return id;
    }

    public int getDegree() {
        return degree;
    }

    public String getCoefficientsAsString() {
        return coefficients;
    }

    // ✨ הוספת מתודה שמחזירה רשימה של מקדמים
    public List<String> getCoefficientsAsList() {
        List<String> coefficientList = new ArrayList<>();
        if (coefficients == null || coefficients.isEmpty()) {
            return coefficientList; // רשימה ריקה אם אין נתונים
        }
        String[] parts = coefficients.split(",");
        for (String part : parts) {
            try {
                double value = Double.parseDouble(part.trim());
                // אם מספר שלם, שמור אותו בלי ".0"
                if (value == (int) value) {
                    coefficientList.add(String.valueOf((int) value));
                } else {
                    coefficientList.add(String.valueOf(value));
                }
            } catch (NumberFormatException e) {
                coefficientList.add("0");
            }
        }
        return coefficientList;
    }

}
