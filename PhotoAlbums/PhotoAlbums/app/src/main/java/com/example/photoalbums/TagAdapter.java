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

public class TagAdapter extends ArrayAdapter<Tag_model> {

    Context mCtx;
    int layoutRes;
    List<Tag_model> tagList;

    //the databasemanager object
    DatabaseHelper mDatabase;

    //modified the constructor and we are taking the DatabaseManager instance here
    public TagAdapter(Context mCtx, int layoutRes, List<Tag_model> employeeList, DatabaseHelper mDatabase) {
        super(mCtx, layoutRes, employeeList);

        this.mCtx = mCtx;
        this.layoutRes = layoutRes;
        this.tagList = employeeList;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View view = inflater.inflate(layoutRes, null);

        TextView texttagName = view.findViewById(R.id.tagname_dialog);
        TextView texttagValue = view.findViewById(R.id.tagvalue_dialog);


        final Tag_model employee = tagList.get(position);

        texttagName.setText("Tag Name: "+employee.getTagname());
        texttagValue.setText("Tag Value: "+employee.getTagvalue());





        return view;
    }




}

