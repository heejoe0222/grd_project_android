package org.grd_p.grd_project.PoschairDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseAdapter {
    // Database fields

    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public DataBaseAdapter(Context context) {
        this.context = context;
    }

    public DataBaseAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }
    public void dbclose() {
        database.close();
    }
    public void dbopen() {
        database= dbHelper.getWritableDatabase();
    }

    public Cursor fetchSomeTable3data(String date) {
        return database.query(DayChart.TABLE_NAME,
                new String[] { DayChart.DATE,DayChart.TOTAL_SITTING,DayChart.CORRECT_SITTING,
                        DayChart.KEYWORD0,DayChart.KEYWORD1,DayChart.KEYWORD2,DayChart.KEYWORD3,
                        DayChart.KEYWORD4, DayChart.KEYWORD5,DayChart.KEYWORD6,
                        DayChart.CORRECT_PELVIS,DayChart.LEFT_PELVIS},
                DayChart.DATE + "=?",
                new String[]{date}, null, null, null,null);
    }

    public Cursor fetchAllTable4data(String week) {
        return database.query(WeekChart.TABLE_NAME,
                new String[] { WeekChart.WEEK,WeekChart.CORRECT_SITTING,
                        WeekChart.KEYWORD0,WeekChart.KEYWORD1,WeekChart.KEYWORD2,WeekChart.KEYWORD3,
                        WeekChart.KEYWORD4, WeekChart.KEYWORD5,WeekChart.KEYWORD6,
                        WeekChart.CORRECT_PELVIS,WeekChart.LEFT_PELVIS},
                WeekChart.WEEK + "=?",
                new String[]{String.valueOf(week)}, null, null, null,null);
    }

    public Cursor fetchAllTable5data(String month) {
        return database.query(MonthChart.TABLE_NAME,
                new String[] { MonthChart.MONTH,MonthChart.CORRECT_SITTING,
                        MonthChart.KEYWORD0,MonthChart.KEYWORD1,MonthChart.KEYWORD2,MonthChart.KEYWORD3,
                        MonthChart.KEYWORD4, MonthChart.KEYWORD5,MonthChart.KEYWORD6,
                        MonthChart.CORRECT_PELVIS,MonthChart.LEFT_PELVIS},
                MonthChart.MONTH + "=?",
                new String[]{String.valueOf(month)}, null, null, null,null);
    }

    public void deleteTable(String tablename){
        database.execSQL("delete from "+tablename+';');
    }
    public void createIndividualTable(String query){
        database.execSQL(query);
    }

    public void InsertTable1Data(Object[] params) {
        String sql = "insert into video(videoID, title, viewNum, postedTime, videoLike) values (?,?,?,?,?)";
        database.execSQL(sql,params);
    }

    public void InsertTable2Data(Object[] params) {
        String sql = "insert into likeVideo(videoID, title, viewNum, postedTime, videoLike) values (?,?,?,?,?)";
        database.execSQL(sql,params);
    }


    public void InsertTable3Data(DayChart day) {
        ContentValues values = new ContentValues();
        values.put(day.DATE, day.getDate());
        values.put(day.TOTAL_SITTING, day.getTotal_sitting_time());
        values.put(day.CORRECT_SITTING, day.getCorrect_sitting_time());
        values.put(day.KEYWORD0, day.getK0());
        values.put(day.KEYWORD1, day.getK1());
        values.put(day.KEYWORD2, day.getK2());
        values.put(day.KEYWORD3, day.getK3());
        values.put(day.KEYWORD4, day.getK4());
        values.put(day.KEYWORD5, day.getK5());
        values.put(day.KEYWORD6, day.getK6());
        values.put(day.CORRECT_PELVIS, day.getCorrect_pelvis());
        values.put(day.LEFT_PELVIS, day.getLeft_pelvis());

        database.insert(DayChart.TABLE_NAME, null, values);

    }

    public void InsertTable4Data(WeekChart week) {
        ContentValues values = new ContentValues();
        values.put(week.WEEK, week.getWeek());
        values.put(week.CORRECT_SITTING, week.getCorrect_sitting_time());
        values.put(week.KEYWORD0, week.getK0());
        values.put(week.KEYWORD1, week.getK1());
        values.put(week.KEYWORD2, week.getK2());
        values.put(week.KEYWORD3, week.getK3());
        values.put(week.KEYWORD4, week.getK4());
        values.put(week.KEYWORD5, week.getK5());
        values.put(week.KEYWORD6, week.getK6());
        values.put(week.CORRECT_PELVIS, week.getCorrect_pelvis());
        values.put(week.LEFT_PELVIS, week.getLeft_pelvis());

        database.insert(WeekChart.TABLE_NAME, null, values);

    }

    public void InsertTable5Data(MonthChart month) {
        ContentValues values = new ContentValues();
        values.put(month.MONTH, month.getMonth());
        values.put(month.CORRECT_SITTING, month.getCorrect_sitting_time());
        values.put(month.KEYWORD0, month.getK0());
        values.put(month.KEYWORD1, month.getK1());
        values.put(month.KEYWORD2, month.getK2());
        values.put(month.KEYWORD3, month.getK3());
        values.put(month.KEYWORD4, month.getK4());
        values.put(month.KEYWORD5, month.getK5());
        values.put(month.KEYWORD6, month.getK6());
        values.put(month.CORRECT_PELVIS, month.getCorrect_pelvis());
        values.put(month.LEFT_PELVIS, month.getLeft_pelvis());

        database.insert(MonthChart.TABLE_NAME, null, values);
    }

}
