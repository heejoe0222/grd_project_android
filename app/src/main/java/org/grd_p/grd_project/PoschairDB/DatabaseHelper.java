package org.grd_p.grd_project.PoschairDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "poschairDB";

    private static final int DATABASE_VERSION =1;

    // Database creation sql statement
    public static final String Table1= "create table if not exists video (videoID text PRIMARY KEY, title text, viewNum integer, postedTime text, videoLike integer)";
    public static final String Table2 = "create table if not exists likeVideo (videoID text PRIMARY KEY, title text, viewNum integer, postedTime text, videoLike integer)";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //schema: (videoID text PRIMARY KEY, title text, viewNum text, postedTime text, videoLike integer)
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Table1); //create video table
        db.execSQL(Table2); //create likeVideo table

        db.execSQL(DayChart.CREATE_TABLE); //create dayChart table
        db.execSQL(WeekChart.CREATE_TABLE); //create weekChart table
        db.execSQL(MonthChart.CREATE_TABLE); //create monthChart table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old_v, int new_v) {
        if(new_v>1){
            db.execSQL("drop table if exists Table1");
            db.execSQL("drop table if exists Table2");
            db.execSQL("drop table if exists Table3");
            db.execSQL("drop table if exists Table4");
            db.execSQL("drop table if exists Table5");

            onCreate(db);

        }
    }
    public boolean deleteDatabase(Context context) {
        return context.deleteDatabase(DATABASE_NAME);
    }
}
