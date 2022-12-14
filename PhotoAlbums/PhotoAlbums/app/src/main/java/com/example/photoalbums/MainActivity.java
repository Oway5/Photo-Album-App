package com.example.photoalbums;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button create;
    DatabaseHelper mDatabaseHelper;
    List<Album> employeeList;
    ListView listView;
    AlbumAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseHelper = new DatabaseHelper(this);
        employeeList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listViewEmployees);

        loadEmployeesFromDatabase();

        create = (Button) findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_album();
            }
        });
    }

    private void loadEmployeesFromDatabase() {
        //we are here using the DatabaseManager instance to get all employees
        Cursor cursor = mDatabaseHelper.getAllEmployees();

        if (cursor.moveToFirst()) {
            do {
                employeeList.add(new Album(
                        cursor.getInt(0),
                        cursor.getString(1)

                ));
            } while (cursor.moveToNext());

            //passing the databasemanager instance this time to the adapter
            adapter = new AlbumAdapter(this, R.layout.list_layout_employees, employeeList, mDatabaseHelper);
            listView.setAdapter(adapter);
        }
    }

    private void create_album() {
        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this);
        builder.setTitle("Album Name");

        // set the custom layout
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.custom_layout,
                        null);
        builder.setView(customLayout);

        // add a button
        builder.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which)
                            {

                                // send data from the
                                // AlertDialog to the Activity
                                EditText editText
                                        = customLayout
                                        .findViewById(
                                                R.id.editText);
                                if (editText.getText().toString().isEmpty()){
                                    editText.setError("Name can't be empty");
                                    editText.requestFocus();
                                }else {
                                sendDialogDataToActivity(
                                        editText
                                                .getText()
                                                .toString());}
                            }
                        });

        // create and show
        // the alert dialog
        AlertDialog dialog
                = builder.create();
        dialog.show();
    }

    private void sendDialogDataToActivity(String data)
    {
        if (mDatabaseHelper.addEmployee(data))
        startActivity(new Intent(MainActivity.this,MainActivity.class));
    }
}