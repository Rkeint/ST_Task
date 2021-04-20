package com.example.st_task;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "Userdata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Tasks(id TEXT primary key, name TEXT, details TEXT, status TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("drop Table if exists Tasks");
    }

    public boolean saveTask(String id, String name, String details, String status){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("details", details);
        contentValues.put("status", status);
        long result=DB.insert("Tasks", null, contentValues);
        if (result==-1){
            return false;
        }else{
            return true;
        }
    }

    public boolean deleteTask(String name) {
        SQLiteDatabase DB = this.getWritableDatabase();

        Cursor cursor = DB.rawQuery("Select * from Tasks where id = "+ name,null);

        if (cursor.getCount() > 0) {

            long result = DB.delete("Tasks", "id="+ name,null);
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else{
            return false;
        }
    }

    public Cursor getAllTasks() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Tasks", null);
        return cursor;
    }

    public Cursor getTask(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "Select * from Tasks where id='"+name+"'", null );
        return res;
    }

    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM Tasks";
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public boolean updateTask(String id, String name, String details, String status) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("details", details);
        contentValues.put("status", status);
        Cursor cursor = DB.rawQuery("Select * from Tasks where id='"+id+"'", null);

        if (cursor.getCount() > 0) {

            long result = DB.update("Tasks", contentValues, "id="+ id,null);
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else{
            return false;
        }
    }

}
