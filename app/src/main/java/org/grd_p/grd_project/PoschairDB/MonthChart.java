package org.grd_p.grd_project.PoschairDB;

public class MonthChart {
    public static final String TABLE_NAME = "monthChart";

    public static final String MONTH = "month";
    public static final String KEYWORD0 = "K0"; public static final String KEYWORD1 = "K1"; public static final String KEYWORD2 = "K2";
    public static final String KEYWORD3 = "K3"; public static final String KEYWORD4 = "K4"; public static final String KEYWORD5 = "K5";
    public static final String KEYWORD6 = "K6";
    public static final String CORRECT_SITTING = "correct_sitting_time";
    public static final String CORRECT_PELVIS = "correct_pelvis";
    public static final String LEFT_PELVIS = "left_pelvis";

    private String month;
    private int k0,k1,k2,k3,k4,k5,k6;
    private int correct_sitting_time,correct_pelvis,left_pelvis;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + MONTH + " TEXT PRIMARY KEY,"
                    + CORRECT_SITTING + " INTEGER,"
                    + KEYWORD0 + " INTEGER," + KEYWORD1 + " INTEGER," + KEYWORD2 + " INTEGER,"
                    + KEYWORD3 + " INTEGER," + KEYWORD4 + " INTEGER," + KEYWORD5 + " INTEGER,"
                    + KEYWORD6 + " INTEGER,"
                    + CORRECT_PELVIS + " INTEGER,"
                    + LEFT_PELVIS + " INTEGER"
                    + ")";

    public MonthChart(String month, int k0, int k1, int k2, int k3, int k4, int k5, int k6, int correct_sitting_time, int correct_pelvis, int left_pelvis) {
        this.month = month;
        this.k0 = k0;this.k1 = k1;this.k2 = k2;this.k3 = k3;
        this.k4 = k4;this.k5 = k5;this.k6 = k6;
        this.correct_sitting_time = correct_sitting_time;
        this.correct_pelvis = correct_pelvis;
        this.left_pelvis = left_pelvis;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getK0() {
        return k0;
    }

    public void setK0(int k0) {
        this.k0 = k0;
    }

    public int getK1() {
        return k1;
    }

    public void setK1(int k1) {
        this.k1 = k1;
    }

    public int getK2() {
        return k2;
    }

    public void setK2(int k2) {
        this.k2 = k2;
    }

    public int getK3() {
        return k3;
    }

    public void setK3(int k3) {
        this.k3 = k3;
    }

    public int getK4() {
        return k4;
    }

    public void setK4(int k4) {
        this.k4 = k4;
    }

    public int getK5() {
        return k5;
    }

    public void setK5(int k5) {
        this.k5 = k5;
    }

    public int getK6() {
        return k6;
    }

    public void setK6(int k6) {
        this.k6 = k6;
    }

    public int getCorrect_sitting_time() {
        return correct_sitting_time;
    }

    public void setCorrect_sitting_time(int correct_sitting_time) {
        this.correct_sitting_time = correct_sitting_time;
    }

    public int getCorrect_pelvis() {
        return correct_pelvis;
    }

    public void setCorrect_pelvis(int correct_pelvis) {
        this.correct_pelvis = correct_pelvis;
    }

    public int getLeft_pelvis() {
        return left_pelvis;
    }

    public void setLeft_pelvis(int left_pelvis) {
        this.left_pelvis = left_pelvis;
    }
}
