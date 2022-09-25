package com.ztcsoftware.myre;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.joda.time.DateTime;


public class SQLiteDBHelper extends SQLiteOpenHelper {

    private static final int   DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "database";

    public static final String TABLE_NAME = "data";
    public static final String COLUMN_ID = "_id";
    public static final String FIELD1 = "pN";
    public static final String FIELD2 = "uF";
    public static final String FIELD3 = "uFo";
    public static final String FIELD4 = "tZ";
    public static final String FIELD5 = "dY";
    public static final String FIELD6 = "tS";
    public static final String FIELD7 = "flag"; // default value 0, accepted values 1 or 0
    public static final String FIELD8 = "lastSubmission";
    public static final String FIELD9 = "lastSubmissionMillis";

    public static final String TABLE_NAME_P = "p_data";
    public static final String FIELD1_P ="sD";
    public static final String FIELD2_P ="eD";
    public static final String FIELD3_P ="msD";
    public static final String FIELD4_P ="ysD";
    public static final String FIELD5_P ="fD";
    public static final String FIELD6_P ="function"; //default value 0 for manual , for auto value 1


    public SQLiteDBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    private static SQLiteDBHelper mInstance = null;

    public static SQLiteDBHelper getmInstance(Context context){
        if (mInstance==null)
            mInstance = new SQLiteDBHelper(context.getApplicationContext());

        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIELD1 + " TEXT, " +
                FIELD2 + " TEXT, " +
                FIELD3 + " TEXT, " +
                FIELD4 + " TEXT, " +
                FIELD5 + " TEXT, " +
                FIELD6 + " TEXT, " +
                FIELD7 + " INTEGER DEFAULT 0, " +
                FIELD8 + " TEXT, " +
                FIELD9 + " TEXT" + ")");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME_P + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIELD1_P + " TEXT, " +
                FIELD2_P + " TEXT, " +
                FIELD3_P + " TEXT, " +
                FIELD4_P + " TEXT, " +
                FIELD5_P + " TEXT, " +
                FIELD6_P + " INTEGER DEFAULT 0" + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_P);
        onCreate(sqLiteDatabase);
    }

    public Cursor getList(){
        SQLiteDatabase database = this.getWritableDatabase();

        return database.query(true,TABLE_NAME,new String[] {COLUMN_ID,FIELD1,FIELD3,FIELD8,"MAX(lastSubmissionMillis)"},
                null,
                null,
                FIELD1,null,null,null);
    }

    public Cursor getListTZ(String med){
        SQLiteDatabase database = this.getWritableDatabase();
        String selection = FIELD1 + " = ?"; //WHERE  expr1=var1
        String[] selectionArgs = {med};  // {var1}
        return database.query(true,TABLE_NAME,new String[] {COLUMN_ID,FIELD1,FIELD4},
                selection,
                selectionArgs,
                FIELD4,null,null,null);
    }

    public Cursor getListDays(String med){
        SQLiteDatabase database = this.getWritableDatabase();
        String selection = FIELD1 + " = ?"; //WHERE  expr1=var1
        String[] selectionArgs = {med};  // {var1}
        return database.query(true,TABLE_NAME,new String[] {COLUMN_ID,FIELD1,FIELD5},
                selection,
                selectionArgs,
                FIELD5,null,null,null);
    }

    public Cursor getTimeEventCalendar(String msD, String ysD){

        SQLiteDatabase database = this.getWritableDatabase();
        String selection = FIELD3_P + " = ?" + " AND "+ FIELD4_P + " =? "; //WHERE  expr1=var1  AND expr2=var2 AND expr3=var3
        String[] selectionArgs = {msD,ysD};  // {var1,var2,var3}

        return database.query(TABLE_NAME_P,new String[] {COLUMN_ID,FIELD1_P,FIELD2_P,FIELD5_P,FIELD6_P},
                selection,
                selectionArgs,
                null,null,null,null);
    }

    public Cursor getSnList(String cTz, String cDy){

        SQLiteDatabase database = this.getReadableDatabase();
        String selection = FIELD4 + " = ?" +" AND " +FIELD5 + " =?" + " AND " + FIELD7 + " =?"; //WHERE  expr1=var1  AND expr2=var2 AND expr3=var3
        String[] selectionArgs = {cTz,cDy,String.valueOf(0)};  // {var1,var2,var3}
        return database.query(false,TABLE_NAME,new String[] {COLUMN_ID,FIELD1,FIELD3,FIELD4,FIELD5},
                selection,
                selectionArgs,
                null,
                null,
                null,
                null);
  //      Cursor cursor = database.rawQuery("SELECT _id,pN,uFo,tZ,dY FROM data WHERE tZ='Text_B' AND dY='cb8'",null);
    }

    public Cursor getWidgetList(String cTz, String cDy,Context mContext){

        SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(mContext);
        SQLiteDatabase database = sqLiteDBHelper.getReadableDatabase();

        String selection = FIELD4 + " = ?" +" AND " +FIELD5 + " =?" + " AND " + FIELD7 + " =?"; //WHERE  expr1=var1  AND expr2=var2 AND expr3=var3
        String[] selectionArgs = {cTz,cDy,String.valueOf(0)};  // {var1,var2,var3}

        return database.query(false,TABLE_NAME,new String[] {COLUMN_ID,FIELD1,FIELD3,FIELD4,FIELD5},
                selection,
                selectionArgs,
                null,
                null,
                null,
                null);
        //      Cursor cursor = database.rawQuery("SELECT _id,pN,uFo,tZ,dY FROM data WHERE tZ='Text_B' AND dY='cb8'",null);
    }

    public int updateList(String pN, String tZ, String dY, String timeStampSubm,String timeStampMillis){
        SQLiteDatabase database = this.getWritableDatabase();
        // New value for one column
        int newValue = 1;
        ContentValues values = new ContentValues();
        values.put(FIELD7,newValue);
        values.put(FIELD8,timeStampSubm);   //timestamp when user submit
        values.put(FIELD9,timeStampMillis); //timestamp in milliseconds when user submit

        String selection = FIELD1 + " = ?" +" AND " +FIELD4 + " = ?" +" AND " +FIELD5 + " =?"; //WHERE  expr1=var1  AND expr2=var2 AND expr3=var3
        String[] selectionArgs = {pN,tZ,dY};  // {var1,var2,var3}

        return database.update(TABLE_NAME,values,selection,selectionArgs);
    }

    public boolean initFlags(String tZ){
        SQLiteDatabase database = this.getWritableDatabase();
        // New value for one column
        int newValue = 0;
        ContentValues values = new ContentValues();
        values.put(FIELD7, newValue);

        String selection = FIELD4 + " != ?";
        String[] selectionArgs = {tZ};

        return database.update(TABLE_NAME, values, selection, selectionArgs)  >0;
    }
}
