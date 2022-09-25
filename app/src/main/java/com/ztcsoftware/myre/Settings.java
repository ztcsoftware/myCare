package com.ztcsoftware.myre;

import static com.ztcsoftware.myre.RangePickerActivity.MyPREFERENCES;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shawnlin.numberpicker.NumberPicker;

public class Settings extends AppCompatActivity {

    public static final String interval = "interval"; //repetitions, store int value
    public static final String duration = "duration"; //calculations, store int value
    public static final String auto = "auto"; // store boolean value

    SharedPreferences sharedpreferences;
    NumberPicker   numberPickerDays,numberPickerMonths;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor editor = sharedpreferences.edit();
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch switchAM = findViewById(R.id.switchAutoManual);

        numberPickerDays =  findViewById(R.id.numberPickerDays);
        numberPickerMonths =  findViewById(R.id.numberPickerMonths);

//        Log.i("shared auto", String.valueOf(sharedpreferences.getBoolean(auto,false)));
//        Log.i("shared interval", String.valueOf(sharedpreferences.getInt(interval,0)));
//        Log.i("shared duration", String.valueOf(sharedpreferences.getInt(duration,0)));

        if(sharedpreferences.getBoolean(auto,false) ) {

            //second argument default value if sharedpref not exist
            //Initial state for pickers are INVISIBLE when switch set to AUTO pickers appears
            numberPickerDays.setVisibility(View.VISIBLE);
            numberPickerMonths.setVisibility(View.VISIBLE);
            switchAM.setChecked(true);
            switchAM.setText(getResources().getString(R.string.auto)); //update the text

            //update the pickers values with  the last values user choose
            // if the values are not set the default values 28 and 3 will loaded
            numberPickerDays.setValue(sharedpreferences.getInt(interval,28));
            numberPickerMonths.setValue(sharedpreferences.getInt(duration,3));

            // OnScrollListener
            numberPickerDays.setOnScrollListener((picker, scrollState) -> {

                if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                    Log.i("days", String.format("newVal: %d", picker.getValue()));

                //    Toast.makeText(this,picker.getValue(),Toast.LENGTH_SHORT).show();
                    editor.putInt(interval,picker.getValue());
                    editor.commit();
                }

            });

            // OnScrollListener
            numberPickerMonths.setOnScrollListener((picker, scrollState) -> {
                if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                    Log.i("Months", String.format("newVal: %d", picker.getValue()));
                    editor.putInt(duration,picker.getValue());
                    editor.commit();
                }
            });

        }else {
            //Initial state for pickers are INVISIBLE when switch set to AUTO pickers appears
            numberPickerDays.setVisibility(View.INVISIBLE);
            numberPickerMonths.setVisibility(View.INVISIBLE);
            switchAM.setChecked(false);
        }

        switchAM.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                switchAM.setText(getResources().getString(R.string.auto));
                numberPickerDays.setVisibility(View.VISIBLE);
                numberPickerMonths.setVisibility(View.VISIBLE);
                editor.putBoolean(auto, true);
                editor.commit();


                // OnScrollListener
                numberPickerDays.setOnScrollListener((picker, scrollState) -> {
                    Toast.makeText(this,picker.getValue(),Toast.LENGTH_SHORT).show();

                    if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                        Log.i("days", String.format("newVal: %d", picker.getValue()));
                        editor.putInt(interval,picker.getValue());
                        ///
                       editor.commit();
                    }
                });

                // OnScrollListener
                numberPickerMonths.setOnScrollListener((picker, scrollState) -> {
                    if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                        Log.i("Months", String.format("newVal: %d", picker.getValue()));
                        editor.putInt(duration,picker.getValue());
                        ///
                        editor.commit();
                    }
                });

            }
            else{
                switchAM.setText(getResources().getString(R.string.manual));
                numberPickerDays.setVisibility(View.INVISIBLE);
                numberPickerMonths.setVisibility(View.INVISIBLE);
                editor.putBoolean(auto, false);
                editor.commit();
            }

        });

    }
}
