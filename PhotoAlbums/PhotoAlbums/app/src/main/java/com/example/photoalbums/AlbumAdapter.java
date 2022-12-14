package com.example.photoalbums;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class AlbumAdapter extends ArrayAdapter<Album> {

    Context mCtx;
    int layoutRes;
    List<Album> employeeList;

    //the databasemanager object
    DatabaseHelper mDatabase;

    //modified the constructor and we are taking the DatabaseManager instance here
    public AlbumAdapter(Context mCtx, int layoutRes, List<Album> employeeList, DatabaseHelper mDatabase) {
        super(mCtx, layoutRes, employeeList);

        this.mCtx = mCtx;
        this.layoutRes = layoutRes;
        this.employeeList = employeeList;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View view = inflater.inflate(layoutRes, null);

        TextView textViewName = view.findViewById(R.id.textViewName);


        final Album employee = employeeList.get(position);

        textViewName.setText(employee.getName());



        view.findViewById(R.id.view_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("employeeList.get(position).getId:"+employeeList.get(position).getId());
                Intent intent = new Intent(getContext(), Photos_class.class);
                intent.putExtra("key_id", employeeList.get(position).getId());
                getContext().startActivity(intent);
            }
        });

        view.findViewById(R.id.buttonDeleteEmployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEmployee(employee);
            }
        });

        view.findViewById(R.id.buttonUpdateEmployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEmployee(employee);
            }
        });

        return view;
    }

    private void updateEmployee(final Album employee) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.dialog_update_employee, null);
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final EditText editTextName = view.findViewById(R.id.editTextName);


        editTextName.setText(employee.getName());


        view.findViewById(R.id.buttonUpdateEmployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = editTextName.getText().toString().trim();


                if (name.isEmpty()) {
                    editTextName.setError("Name can't be empty");
                    editTextName.requestFocus();
                    return;
                }



                //calling the update method from database manager instance
                if (mDatabase.updateEmployee(employee.getId(), name)) {
                    Toast.makeText(mCtx, "Album Updated", Toast.LENGTH_SHORT).show();
                    loadEmployeesFromDatabaseAgain();
                }
                alertDialog.dismiss();
            }
        });
    }

    private void deleteEmployee(final Album employee) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
        builder.setTitle("Are you sure?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //calling the delete method from the database manager instance
                if (mDatabase.deleteEmployee(employee.getId()))
                    loadEmployeesFromDatabaseAgain();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void loadEmployeesFromDatabaseAgain() {
        //calling the read method from database instance
        Cursor cursor = mDatabase.getAllEmployees();

        employeeList.clear();
        if (cursor.moveToFirst()) {
            do {
                employeeList.add(new Album(
                        cursor.getInt(0),
                        cursor.getString(1)
                ));
            } while (cursor.moveToNext());
        }
        notifyDataSetChanged();
    }
}
