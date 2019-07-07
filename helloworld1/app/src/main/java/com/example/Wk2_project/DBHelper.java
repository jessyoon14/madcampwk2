package com.example.Wk2_project;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.Wk2_project.Fg3_Memo.MemoInfo;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 MONEYBOOK이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */

        db.execSQL(" CREATE TABLE IF NOT EXISTS MONEYBOOK (_id INTEGER PRIMARY KEY AUTOINCREMENT, item TEXT, create_at TEXT);");
}

    public String select(String id){
        SQLiteDatabase db = getWritableDatabase();
        String result = "";
        // DB에 입력한 값으로 행 추가
        Cursor cursor = db.rawQuery("SELECT ITEM FROM MONEYBOOK WHERE _id="+id+";", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0);
        }
        db.close();
        return result;
    }
    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String create_at, String item) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO MONEYBOOK VALUES(null, '" + item + "', '" + create_at + "');");
        db.close();
    }

    public void update(String item, String id) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 가격 정보 수정
        db.execSQL("UPDATE MONEYBOOK SET item='" + item + "' WHERE _id= "+ id + ";");
        db.close();
    }

    public void delete(String id) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM MONEYBOOK WHERE _id=" + id + ";");
        db.close();
    }

    public String getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM MONEYBOOK", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0)
                    + " : "
                    + cursor.getString(1)
                    + " | "
                    + cursor.getString(2)
                    + "\n";
        }

        return result;
    }
    public ArrayList getResultof(String item){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<MemoInfo> MemoInfoArrayList = new ArrayList<>();
        String result = "";

        Cursor cursor = db.rawQuery("SELECT * FROM MONEYBOOK", null);
        while (cursor.moveToNext()) {
            if( item.compareTo(cursor.getString(2)) == 0) {
                MemoInfoArrayList.add(new MemoInfo(cursor.getInt(0), cursor.getString(2),cursor.getString(1)));
                result += cursor.getString(0)
                        + " : "
                        + cursor.getString(1)
                        + " | "
                        + cursor.getString(2)
                        + "\n";

            }
        }
        return MemoInfoArrayList;
    }




}



