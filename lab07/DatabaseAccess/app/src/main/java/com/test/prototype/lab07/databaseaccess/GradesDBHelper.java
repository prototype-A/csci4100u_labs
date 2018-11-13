package com.test.prototype.lab07.databaseaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class GradesDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "grades.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "Grades";
    private static final String CREATE_TABLE_SQL =
            "CREATE TABLE " + TABLE_NAME + " (" +
            "   _id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "   studentId INTEGER NOT NULL," +
            "   courseComponent VARCHAR(100) NOT NULL," +
            "   mark TEXT NOT NULL);";
    private static final String DELETE_TABLE_SQL =
            "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public GradesDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table on first time execute
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Check database versions
        if (oldVersion < newVersion) {
            // upgrade

        } else {
            // downgrade

        }

        // Delete old database tables
        db.execSQL(DELETE_TABLE_SQL);

        // Create new database
        onCreate(db);
    }

    public Grade addGrade(int sid, String course, float mark) {

        ContentValues values = new ContentValues();
        values.put("studentId", sid);
        values.put("courseComponent", course);
        values.put("mark", String.valueOf(mark));

        getWritableDatabase().insert(TABLE_NAME, null, values);

        return new Grade(sid, course, mark);
    }

    public void deleteGrade(Grade grade) {
        // Delete database entry of grade
        String[] whereArgs = { String.valueOf(grade.getStudentId()), grade.getCourseComponent(), String.valueOf(grade.getMark()) };
        int rowsDeleted = getWritableDatabase().delete(TABLE_NAME, "studentId=? AND courseComponent=? AND mark=?", whereArgs);
        System.out.println("Rows deleted: " + rowsDeleted + " for Grade: " + grade.getStudentId() + "'s " + grade.getCourseComponent() + ": " + String.valueOf(grade.getMark()));
    }

    public List<Grade> getAllGrades() {

        SQLiteDatabase db = getReadableDatabase();
        String[] cols = { "studentId", "courseComponent", "mark" };
        String[] args = {};

        // Query all rows
        Cursor cursor = db.query(TABLE_NAME, cols,
                "", args,
                "", "",
                cols[1]);

        List<Grade> grades = new ArrayList<>();

        // Iterate over all returned tuples
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int sid = cursor.getInt(0);
            String course = cursor.getString(1);
            float mark = cursor.getFloat(2);

            Grade grade = new Grade(sid, course, mark);
            grades.add(grade);

            cursor.moveToNext();
        }

        return grades;
    }

}