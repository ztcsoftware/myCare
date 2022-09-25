package com.ztcsoftware.myre;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.chip.Chip;
import org.joda.time.DateTime;
import java.util.ArrayList;
import java.util.List;

public class Insert extends AppCompatActivity {

    EditText textA,textB,textC;
    List<String>  codes = new ArrayList<>();
    List<String>  hbts= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        codes.clear();
        hbts.clear();

        textA = findViewById(R.id.editText1); // pr
        textB = findViewById(R.id.editText2); // uFm
        textC = findViewById(R.id.editText3); // uFr

        CheckBox ch1 = findViewById(R.id.chBx1); //w
        CheckBox ch2 = findViewById(R.id.chBx2); //w
        CheckBox ch3 = findViewById(R.id.chBx3); //w
        CheckBox ch4 = findViewById(R.id.chBx4); //w
        CheckBox ch5 = findViewById(R.id.chBx5); //w
        CheckBox ch6 = findViewById(R.id.chBx6); //w
        CheckBox ch7 = findViewById(R.id.chBx7); //w
        CheckBox ch8 = findViewById(R.id.chBx8); //ed

        Chip chip1 =findViewById(R.id.chip1); // p
        Chip chip2 =findViewById(R.id.chip2); // m
        Chip chip3 =findViewById(R.id.chip3); // br
        Chip chip4 =findViewById(R.id.chip4); // abr

        Button cancel = findViewById(R.id.buttonA); //a
        Button clear  = findViewById(R.id.buttonB); //k
        Button submit = findViewById(R.id.buttonC); //e

        ch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if(ch1.getText().toString().equals("M") || ch1.getText().toString().equals("Δ") )
                    codes.add("1");
                ch8.setChecked(false);
            }
            else
                codes.remove("1");
        });
        ch2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                if(ch2.getText().toString().equals("T") || ch2.getText().toString().equals("Τρ"))
                    codes.add("2");
                ch8.setChecked(false);
            }
            else
                codes.remove("2");
        });
        ch3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                if(ch3.getText().toString().equals("W") || ch3.getText().toString().equals("Τε"))
                    codes.add("3");
                ch8.setChecked(false);
            }
            else
                codes.remove("3");
        });
        ch4.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                if( ch4.getText().toString().equals("Th") || ch4.getText().toString().equals("Πε")   )
                    codes.add("4");
                ch8.setChecked(false);
            }
            else
                codes.remove("4");
        });
        ch5.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                if(ch5.getText().toString().equals("F") || ch5.getText().toString().equals("Πα"))
                    codes.add("5");
                ch8.setChecked(false);
            }
            else
                codes.remove("5");
        });
        ch6.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if(ch6.getText().toString().equals("Sa") || ch6.getText().toString().equals("Σ"))
                    codes.add("6");
                ch8.setChecked(false);
            }
            else
                codes.remove("6");
        });
        ch7.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                if(ch7.getText().toString().equals("Su") || ch7.getText().toString().equals("Κ"))
                    codes.add("7");
                ch8.setChecked(false);
            }
            else
                codes.remove("7");
        });
        ch8.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        //codes.add(ch8.getText().toString());

                        for (int i=1 ;i<=7;i++) codes.add(String.valueOf(i));

                        ch1.setChecked(false);ch2.setChecked(false);ch3.setChecked(false);
                        ch4.setChecked(false);ch5.setChecked(false);ch6.setChecked(false);
                        ch7.setChecked(false);
                    }else {
                        for (int i = 1; i <= 7; i++) codes.remove(String.valueOf(i));
                    }
        });

        /////////////////////////////// Chips
        chip1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
               // hbts.add((String) chip1.getText());
                hbts.add("Text_A");
            else
                //hbts.remove(chip1.getText());
                hbts.remove("Text_A");
        });

        chip2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
                hbts.add("Text_B");
            else
                hbts.remove("Text_B");
        });

        chip3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
                hbts.add("Text_C");
            else
                hbts.remove("Text_C");
        });

        chip4.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
                hbts.add("Text_D");
            else
                hbts.remove("Text_D");
        });
        ///////////////////////// Buttons
        cancel.setOnClickListener(v -> {
            Intent intent = new Intent(Insert.this,MainActivity.class);
            startActivity(intent);
        });

        clear.setOnClickListener(v -> {
            String cds="";
            for(int i=0;i<codes.size();i++){
                cds = cds + codes.get(i);
            }
            String s="";
            for(int i=0;i<hbts.size();i++){
                s = s + hbts.get(i);
            }
            Log.i("info codes ",cds);

            Log.i("info hbts",s);
            textA.setText(null);textB.setText(null);textC.setText(null);
            ch1.setChecked(false);ch2.setChecked(false);ch3.setChecked(false);ch4.setChecked(false);ch5.setChecked(false);
            ch6.setChecked(false);ch7.setChecked(false);ch8.setChecked(false);
            chip1.setChecked(false);chip2.setChecked(false);chip3.setChecked(false);chip4.setChecked(false);
            codes.clear();
            hbts.clear();
        });

        submit.setOnClickListener(v -> {
            Log.i("info codes size : ", String.valueOf(codes.size()));
            Log.i("info hbts size : ", String.valueOf(hbts.size()));

            if (validityCheckText())
                return ;
            else
                if( validityCheckCodes() )
                    return;
                else
                     if(   validityCheckHbts())
                return;
            else {
                for(int i=0;i<codes.size();i++)
                    for (int j=0;j<hbts.size();j++){
                        saveToDB(codes.get(i),hbts.get(j));
                    }
                         if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                             // only for R (API 30) and newer versions
                             LayoutInflater inflater = getLayoutInflater();
                             View layout = inflater.inflate(R.layout.custom_toast,
                                     (ViewGroup) findViewById(R.id.custom_toast_container));

                             TextView text = (TextView) layout.findViewById(R.id.text);
                             text.setText(getResources().getText(R.string.save_entry));
                             text.setTextColor(Color.BLACK);

                             Toast toast = new Toast(getApplicationContext());
                             toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.BOTTOM, 0, 120);
                             toast.setDuration(Toast.LENGTH_LONG);
                             toast.setView(layout);
                             toast.show();
                         }else
                            Toast.makeText(this, getResources().getText(R.string.save_entry), Toast.LENGTH_LONG).show();

            }

        });
    }

    //database data manipulation
    private void saveToDB(String codes,String hbts) {
        DateTime dateTime = new DateTime();
        SQLiteDatabase database = new SQLiteDBHelper(Insert.this).getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SQLiteDBHelper.FIELD1, textA.getText().toString());
        values.put(SQLiteDBHelper.FIELD2, textB.getText().toString());
        values.put(SQLiteDBHelper.FIELD3, textC.getText().toString());
        values.put(SQLiteDBHelper.FIELD4, hbts);                       // hbts from user
        values.put(SQLiteDBHelper.FIELD5, codes);                      // codes from user
        values.put(SQLiteDBHelper.FIELD6, dateTime.toDate().toString());// timestamp
        //returns long type value for inserted rows
        database.insert(SQLiteDBHelper.TABLE_NAME, null, values);
    }

    public Cursor fetch() {
        SQLiteDatabase database = new SQLiteDBHelper(Insert.this).getWritableDatabase();
        String[] columns = new String[] { SQLiteDBHelper.COLUMN_ID, SQLiteDBHelper.FIELD1};
        Cursor cursor = database.query(SQLiteDBHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String name, String desc) {
        SQLiteDatabase database = new SQLiteDBHelper(Insert.this).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
//        contentValues.put(DatabaseHelper.SUBJECT, name);
//        contentValues.put(DatabaseHelper.DESC, desc);

        int i = database.update(SQLiteDBHelper.TABLE_NAME, contentValues, SQLiteDBHelper.COLUMN_ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        SQLiteDatabase database = new SQLiteDBHelper(Insert.this).getWritableDatabase();
        database.delete(SQLiteDBHelper.TABLE_NAME, SQLiteDBHelper.COLUMN_ID + "=" + _id, null);
    }

    // validation methods
    public boolean validityCheckText(){
        if (textA.getText().toString().matches("")) {
            Toast.makeText(this, getResources().getText(R.string.submit_something), Toast.LENGTH_LONG).show();
            return true;
        }else
            return false;
    }
    public boolean validityCheckCodes(){
        if (codes.size()==0){
            Toast.makeText(this, getResources().getText(R.string.submit_codes), Toast.LENGTH_LONG).show();
            return true;
        }else
            return false;
    }
    public boolean validityCheckHbts(){
        if (hbts.size()==0) {
            Toast.makeText(this, getResources().getText(R.string.submit_hbts), Toast.LENGTH_LONG).show();
            return true;
        }else
            return false;
    }
}
