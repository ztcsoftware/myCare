package com.ztcsoftware.myre;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.ztcsoftware.myre.adapters.Adapter;

public class ShowTheList extends AppCompatActivity {

    private SQLiteDBHelper sqLiteDBHelper;
    Adapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    public  ImageView emptyListIcon;
    public  TextView noEntriesText;
    public  ListView mListView;
    public  Cursor cursor;

    @Override
    protected void onCreate(Bundle  savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_showthelist);

        emptyListIcon = findViewById(R.id.emptyList);
        noEntriesText = findViewById(R.id.noEntriesText);
        mListView = findViewById(R.id.listView);
        swipeRefreshLayout = findViewById(R.id.swipe);

        sqLiteDBHelper  = SQLiteDBHelper.getmInstance(getApplicationContext());

        //load the list when activity starts
        loadEntries();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadEntries();
            swipeRefreshLayout.setRefreshing(false);
        });

        mListView.setOnItemClickListener((parent, view, position, id) -> {

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(ShowTheList.this);

            View dialogView = getLayoutInflater().inflate(R.layout.remove_entry_dialog,null);

            Button deleteBtn = dialogView.findViewById(R.id.deleteBtn);
            Button cancel = dialogView.findViewById(R.id.cancel);

            mBuilder.setView(dialogView);

            final AlertDialog dialog = mBuilder.create();
            dialog.setCanceledOnTouchOutside(false);

            deleteBtn.setOnClickListener(v -> {

                if(cursor!=null) {

                    Log.i("Cursor position before entries deleted  -->",String.valueOf( cursor.getPosition()) );
                    Log.i("Cursor elements  -->",String.valueOf( cursor.getCount()) );

                    @SuppressLint("Range") int i=delete(cursor.getString(cursor.getColumnIndex("pN")));
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("pN"));
                    Log.i("Deleted entries -->",i+" "+name );

                    loadEntries();
                    cursor.moveToNext();
                    // Cursor position after entries deleted must be set to  0
                    Log.i("Cursor position after entries deleted  -->",String.valueOf( cursor.getPosition()) );
                }

                Toast.makeText(ShowTheList.this,getResources().getText(R.string.entry_deleted),Toast.LENGTH_LONG ).show();
                dialog.cancel();

            });
            cancel.setOnClickListener(v -> dialog.cancel());
            dialog.show();

        });
    }
    @Override
    protected void onDestroy() {
        sqLiteDBHelper.close();
        super.onDestroy();
    }
    public int delete(String text) {
        SQLiteDatabase database = new SQLiteDBHelper(ShowTheList.this).getWritableDatabase();
        return database.delete(SQLiteDBHelper.TABLE_NAME, SQLiteDBHelper.FIELD1 + "=?" , new String[] {text});
    }
    @SuppressLint("Range")
    private void loadEntries(){

        cursor = sqLiteDBHelper.getList();  //Read the data from the table "data"

        if(cursor!=null){
                adapter = new Adapter(getApplicationContext(), cursor, 0);  //load cursor to the adapter

                //handle the adapter if it is empty or not
                if (adapter.isEmpty()) {
                    emptyListIcon.setVisibility(View.VISIBLE);
                    noEntriesText.setVisibility(View.VISIBLE);
                }
                else {
                    emptyListIcon.setVisibility(View.INVISIBLE);
                    noEntriesText.setVisibility(View.INVISIBLE);
                }

                mListView.setAdapter(adapter);
             }
    }

    //Reload MainActivity, and update the curList when user delete a med
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
