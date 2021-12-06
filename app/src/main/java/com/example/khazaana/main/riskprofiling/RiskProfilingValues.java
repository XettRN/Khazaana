package com.example.khazaana.main.riskprofiling;

import java.util.HashMap;

public class RiskProfilingValues {

    public static String risk_profiling_value1 = "";
    public static String risk_profiling_value2 = "";
    public static String risk_profiling_value3 = "";
    public static String risk_profiling_value4 = "";
    public static String risk_profiling_value5 = "";
    public static String risk_profiling_value6 = "";
    public static String risk_profiling_value7 = "";
    public static String risk_profiling_value8 = "";
    public static String risk_profiling_value9 = "";
    public static String risk_profiling_value10 = "";
    public static String risk_profiling_value11 = "";
    public static String risk_profiling_value12 = "";
    public static String risk_profiling_valueC1 = "";
    public static String risk_profiling_valueC2 = "";
    public static String risk_profiling_valueC3 = "";
    
    public static String getRisk_profiling_value1() {
        return risk_profiling_value1;
    }

    public static String getRisk_profiling_value2() {
        return risk_profiling_value2;
    }

    public static String getRisk_profiling_value3() {
        return risk_profiling_value3;
    }

    public static String getRisk_profiling_value4() {
        return risk_profiling_value4;
    }

    public static String getRisk_profiling_value5() {
        return risk_profiling_value5;
    }

    public static String getRisk_profiling_value6() {
        return risk_profiling_value6;
    }

    public static String getRisk_profiling_value7() {
        return risk_profiling_value7;
    }

    public static String getRisk_profiling_value8() {
        return risk_profiling_value8;
    }

    public static String getRisk_profiling_value9() {
        return risk_profiling_value9;
    }

    public static String getRisk_profiling_value10() {
        return risk_profiling_value10;
    }

    public static String getRisk_profiling_value11() {
        return risk_profiling_value11;
    }

    public static String getRisk_profiling_value12() {
        return risk_profiling_value12;
    }

    public static String getRisk_profiling_valueC1() {
        return risk_profiling_valueC1;
    }

    public static String getRisk_profiling_valueC2() {
        return risk_profiling_valueC2;
    }

    public static String getRisk_profiling_valueC3() {
        return risk_profiling_valueC3;
    }

    public static void setRisk_profiling_value1(String risk_profiling_value1) {
        RiskProfilingValues.risk_profiling_value1 = risk_profiling_value1;
    }

    public static void setRisk_profiling_value2(String risk_profiling_value2) {
        RiskProfilingValues.risk_profiling_value2 = risk_profiling_value2;
    }

    public static void setRisk_profiling_value3(String risk_profiling_value3) {
        RiskProfilingValues.risk_profiling_value3 = risk_profiling_value3;
    }

    public static void setRisk_profiling_value4(String risk_profiling_value4) {
        RiskProfilingValues.risk_profiling_value4 = risk_profiling_value4;
    }

    public static void setRisk_profiling_value5(String risk_profiling_value5) {
        RiskProfilingValues.risk_profiling_value5 = risk_profiling_value5;
    }

    public static void setRisk_profiling_value6(String risk_profiling_value6) {
        RiskProfilingValues.risk_profiling_value6 = risk_profiling_value6;
    }

    public static void setRisk_profiling_value7(String risk_profiling_value7) {
        RiskProfilingValues.risk_profiling_value7 = risk_profiling_value7;
    }

    public static void setRisk_profiling_value8(String risk_profiling_value8) {
        RiskProfilingValues.risk_profiling_value8 = risk_profiling_value8;
    }

    public static void setRisk_profiling_value9(String risk_profiling_value9) {
        RiskProfilingValues.risk_profiling_value9 = risk_profiling_value9;
    }

    public static void setRisk_profiling_value10(String risk_profiling_value10) {
        RiskProfilingValues.risk_profiling_value10 = risk_profiling_value10;
    }

    public static void setRisk_profiling_value11(String risk_profiling_value11) {
        RiskProfilingValues.risk_profiling_value11 = risk_profiling_value11;
    }

    public static void setRisk_profiling_value12(String risk_profiling_value12) {
        RiskProfilingValues.risk_profiling_value12 = risk_profiling_value12;
    }

    public static void setRisk_profiling_valueC1(String risk_profiling_valueC1) {
        RiskProfilingValues.risk_profiling_valueC1 = risk_profiling_valueC1;
    }

    public static void setRisk_profiling_valueC2(String risk_profiling_valueC2) {
        RiskProfilingValues.risk_profiling_valueC2 = risk_profiling_valueC2;
    }

    public static void setRisk_profiling_valueC3(String risk_profiling_valueC3) {
        RiskProfilingValues.risk_profiling_valueC3 = risk_profiling_valueC3;
    }

    public static int getRisk_profiling_score1() {
        String rp1 = getRisk_profiling_value1();
        HashMap <String, Integer> rp1_dictionary = new HashMap<>();
        rp1_dictionary.put("Below 18", 0);
        rp1_dictionary.put("18 - 30", 2);
        rp1_dictionary.put("31 - 40", 4);
        rp1_dictionary.put("41 - 50", 6);
        rp1_dictionary.put("51 - 60", 8);
        rp1_dictionary.put("Above 60", 10);
        int score = rp1_dictionary.get(rp1);
        return score;
    }

    public static int getRisk_profiling_score2() {
        String rp2 = getRisk_profiling_value2();
        HashMap <String, Integer> rp2_dictionary = new HashMap<>();
        rp2_dictionary.put("Single Individual", 2);
        rp2_dictionary.put("Young Couple", 4);
        rp2_dictionary.put("Young Family with Children", 6);
        rp2_dictionary.put("Mature Family", 8);
        rp2_dictionary.put("Preparing for Retirement", 10);
        rp2_dictionary.put("Retired", 12);
        int score = rp2_dictionary.get(rp2);
        return score;
    }

    public static int getRisk_profiling_score3() {
        String rp3 = getRisk_profiling_value3();
        HashMap <String, Integer> rp3_dictionary = new HashMap<>();
        rp3_dictionary.put("0", 0);
        rp3_dictionary.put("1-2", 8);
        rp3_dictionary.put("3-4", 15);
        rp3_dictionary.put("Planning to have in 1 year", 6);
        rp3_dictionary.put("Planning to have in 3 years", 4);
        rp3_dictionary.put("Planning to have in 5 years", 2);
        int score = rp3_dictionary.get(rp3);
        return score;
    }

    public static int getRisk_profiling_score4() {
        String rp4 = getRisk_profiling_value4();
        HashMap <String, Integer> rp4_dictionary = new HashMap<>();
        rp4_dictionary.put("Less than $50,000", 10);
        rp4_dictionary.put("$50,000 - $100,000", 8);
        rp4_dictionary.put("$100,000 - $200,000", 6);
        rp4_dictionary.put("$200,000 - $500,000", 4);
        rp4_dictionary.put("$500,000 - $1,000,000", 2);
        rp4_dictionary.put("Greater than $1,000,000", 1);
        int score = rp4_dictionary.get(rp4);
        return score;
    }

    public static int getRisk_profiling_score5() {
        String rp5 = getRisk_profiling_value5();
        HashMap <String, Integer> rp5_dictionary = new HashMap<>();
        rp5_dictionary.put("Salaried Personnel", 5);
        rp5_dictionary.put("Entrepreneur", 15);
        rp5_dictionary.put("Business Owner", 10);
        rp5_dictionary.put("Freelancer", 20);
        rp5_dictionary.put("Government Employee", 0);
        rp5_dictionary.put("Not employed", 25);
        int score = rp5_dictionary.get(rp5);
        return score;
    }

    public static int getRisk_profiling_score6() {
        String rp6 = getRisk_profiling_value6();
        HashMap <String, Integer> rp6_dictionary = new HashMap<>();
        rp6_dictionary.put("Less than $100,000", 10);
        rp6_dictionary.put("$100,000 - $300,000", 8);
        rp6_dictionary.put("$300,000 - $600,000", 6);
        rp6_dictionary.put("$600,000 - $1,000,000", 4);
        rp6_dictionary.put("$1,000,000 - $2,000,000", 2);
        rp6_dictionary.put("Greater than $2,000,000", 1);
        int score = rp6_dictionary.get(rp6);
        return score;
    }

    public static int getRisk_profiling_score7() {
        String rp7 = getRisk_profiling_value7();
        HashMap <String, Integer> rp7_dictionary = new HashMap<>();
        rp7_dictionary.put("Less than 5%", 1);
        rp7_dictionary.put("5% - 10%", 3);
        rp7_dictionary.put("11% - 15%", 6);
        rp7_dictionary.put("16% - 20%", 9);
        rp7_dictionary.put("21% - 25%", 12);
        rp7_dictionary.put("Greater than 25%", 15);
        int score = rp7_dictionary.get(rp7);
        return score;
    }

    public static int getRisk_profiling_score8() {
        String rp8 = getRisk_profiling_value8();
        HashMap <String, Integer> rp8_dictionary = new HashMap<>();
        rp8_dictionary.put("Grow my wealth", 2);
        rp8_dictionary.put("Passive source of income", 4);
        rp8_dictionary.put("Attain financial independence", 6);
        rp8_dictionary.put("Steady income for retirement", 8);
        rp8_dictionary.put("Education fund for children", 10);
        rp8_dictionary.put("Specific goal in future", 12);
        int score = rp8_dictionary.get(rp8);
        return score;
    }

    public static int getRisk_profiling_score9() {
        String rp9 = getRisk_profiling_value9();
        HashMap <String, Integer> rp9_dictionary = new HashMap<>();
        rp9_dictionary.put("None", 10);
        rp9_dictionary.put("Have a savings account", 12);
        rp9_dictionary.put("Have a portfolio from 1 year", 8);
        rp9_dictionary.put("Have a portfolio from 3 years", 5);
        rp9_dictionary.put("Have a portfolio from 5 years", 2);
        int score = rp9_dictionary.get(rp9);
        return score;
    }

    public static int getRisk_profiling_score10() {
        String rp10 = getRisk_profiling_value10();
        HashMap <String, Integer> rp10_dictionary = new HashMap<>();
        rp10_dictionary.put("Stocks and Bonds", 0);
        rp10_dictionary.put("Real Estate, Artwork, etc.", 5);
        rp10_dictionary.put("Venture Capital", 10);
        rp10_dictionary.put("Cryptocurrency", 15);
        rp10_dictionary.put("Options 1 and 2", 3);
        rp10_dictionary.put("No idea", 2);
        int score = rp10_dictionary.get(rp10);
        return score;
    }

    public static int getRisk_profiling_score11() {
        String rp11 = getRisk_profiling_value11();
        HashMap <String, Integer> rp11_dictionary = new HashMap<>();
        rp11_dictionary.put("Very Risk Averse", 0);
        rp11_dictionary.put("Risk Averse", 3);
        rp11_dictionary.put("Indifferent", 6);
        rp11_dictionary.put("Risk Taking", 9);
        rp11_dictionary.put("Risk Loving", 15);
        rp11_dictionary.put("No idea", 8);
        int score = rp11_dictionary.get(rp11);
        return score;
    }

    public static int getRisk_profiling_score12() {
        String rp12 = getRisk_profiling_value12();
        HashMap <String, Integer> rp12_dictionary = new HashMap<>();
        rp12_dictionary.put("In 1 year", 12);
        rp12_dictionary.put("In 3 years", 10);
        rp12_dictionary.put("In 5 years", 8);
        rp12_dictionary.put("In 10 years", 6);
        rp12_dictionary.put("After retirement", 3);
        rp12_dictionary.put("No idea", 15);
        int score = rp12_dictionary.get(rp12);
        return score;
    }

    public static int getRisk_profiling_scoreC1() {
        String rpc1 = getRisk_profiling_valueC1();
        if (rpc1.equals("")) {
            return 0;
        }
        HashMap <String, Integer> rpc1_dictionary = new HashMap<>();
        rpc1_dictionary.put("Investment losing value", 5);
        rpc1_dictionary.put("Money invested", 10);
        rpc1_dictionary.put("Investment gaining value", 15);

        int score = rpc1_dictionary.get(rpc1);
        return score;
    }

    public static int getRisk_profiling_scoreC2() {
        String rpc2 = getRisk_profiling_valueC2();
        if (rpc2.equals("")) {
            return 0;
        }
        HashMap <String, Integer> rpc2_dictionary = new HashMap<>();
        rpc2_dictionary.put("Strongly Agree", 15);
        rpc2_dictionary.put("Agree", 12);
        rpc2_dictionary.put("Neutral", 9);
        rpc2_dictionary.put("Disagree", 6);
        rpc2_dictionary.put("Strongly Disagree", 3);
        int score = rpc2_dictionary.get(rpc2);
        return score;
    }

    public static int getRisk_profiling_scoreC3() {
        String rpc3 = getRisk_profiling_valueC3();
        if (rpc3.equals("")) {
            return 0;
        }
        HashMap <String, Integer> rpc3_dictionary = new HashMap<>();
        rpc3_dictionary.put("Strongly Agree", 3);
        rpc3_dictionary.put("Agree", 6);
        rpc3_dictionary.put("Neutral", 9);
        rpc3_dictionary.put("Disagree", 12);
        rpc3_dictionary.put("Strongly Disagree", 15);
        int score = rpc3_dictionary.get(rpc3);
        return score;
    }

    public static String returnProfiling() {
        int s1 = getRisk_profiling_score1();
        int s2 = getRisk_profiling_score2();
        int s3 = getRisk_profiling_score3();
        int s4 = getRisk_profiling_score4();
        int s5 = getRisk_profiling_score5();
        int s6 = getRisk_profiling_score6();
        int s7 = getRisk_profiling_score7();
        int s8 = getRisk_profiling_score8();
        int s9 = getRisk_profiling_score9();
        int s10 = getRisk_profiling_score10();
        int s11 = getRisk_profiling_score11();
        int s12 = getRisk_profiling_score12();
        int c1 = getRisk_profiling_scoreC1();
        int c2 = getRisk_profiling_scoreC2();
        int c3 = getRisk_profiling_scoreC3();

        int sum = s1 + s2 + s3 + s4 + s5 + s6 + s7 + s8 + s9 + s10 + s11 + s12 + c1 + c3 + c2;

        if (sum <= 40) {
            return "Conservative";
        } else if (sum <= 80) {
            return "Moderate";
        } else {
            return "Aggressive";
        }
    }

    public static String[] returnProfilingArray() {
        String s1 = getRisk_profiling_value1();
        String s2 = getRisk_profiling_value1();
        String s3 = getRisk_profiling_value1();
        String s4 = getRisk_profiling_value1();
        String s5 = getRisk_profiling_value1();
        String s6 = getRisk_profiling_value1();
        String s7 = getRisk_profiling_value1();
        String s8 = getRisk_profiling_value1();
        String s9 = getRisk_profiling_value1();
        String s10 = getRisk_profiling_value1();
        String s11 = getRisk_profiling_value1();
        String s12 = getRisk_profiling_value1();
        String C1 = getRisk_profiling_valueC1();
        String C2 = getRisk_profiling_valueC1();
        String C3 = getRisk_profiling_valueC1();

        String[] rp_array = {s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, C1, C2, C3};

        return rp_array;
    }
}
