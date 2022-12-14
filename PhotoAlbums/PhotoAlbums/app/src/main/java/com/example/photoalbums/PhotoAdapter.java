package com.example.photoalbums;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends ArrayAdapter<Photos_model> {

    Context mCtx;
    int layoutRes;
    List<Photos_model> picList;
    List<Tag_model> tag;
    Uri uri;
    TagAdapter adapter;
    String tagname_str = "";
    String tagval_str ="";

    //the databasemanager object
    DatabaseHelper mDatabase;

    //modified the constructor and we are taking the DatabaseManager instance here
    public PhotoAdapter(Context mCtx, int layoutRes, List<Photos_model> picList, DatabaseHelper mDatabase) {
        super(mCtx, layoutRes, picList);

        this.mCtx = mCtx;
        this.layoutRes = layoutRes;
        this.picList = picList;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View view = inflater.inflate(layoutRes, null);

        TextView textViewName = view.findViewById(R.id.pic_name);
        TextView textViewdesc = view.findViewById(R.id.pic_desc);
        TextView textViewdate = view.findViewById(R.id.pic_date);
        Button tag_btn = view.findViewById(R.id.tag_btn);
        ImageView pic_img = view.findViewById(R.id.pic_imageview);




        final Photos_model employee = picList.get(position);
        System.out.println("path: "+employee.getImg_contentpath());
        System.out.println("name: "+employee.getImg_path());
        uri = Uri.parse(employee.getImg_contentpath());
        pic_img.setImageURI(uri);
        pic_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View vi=LayoutInflater.from(getContext()).inflate(R.layout.image_dialog,null);

                ImageView imageView = (ImageView) vi.findViewById(R.id.fullImg);
                imageView.setImageURI(uri);

                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getContext(),R.style.my_dialog);
                mDialogBuilder.setView(vi);
                mDialogBuilder.setCancelable(true);



                final AlertDialog mAlertDialog = mDialogBuilder.create();
                mAlertDialog.show();
            }
        });
        textViewName.setText("Image Name: "+employee.getImg_path());
        textViewdesc.setText("Caption: "+employee.getCaption());
        textViewdate.setText("Date: "+employee.getDate());

        tag_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set the custom dialog components - text, image and button
                View view=LayoutInflater.from(getContext()).inflate(R.layout.tag_dialog,null);

                EditText tag_name = (EditText) view.findViewById(R.id.tagname);
                EditText tag_value = (EditText) view.findViewById(R.id.tagvalue);

                Button tagadd= (Button)view.findViewById(R.id.addTag_btn);
                ListView listView = (ListView) view.findViewById(R.id.tahglist) ;


                mDatabase = new DatabaseHelper(getContext());
                tag = new ArrayList<>();

                Cursor cursor = mDatabase.getAllTags();

                if (cursor.moveToFirst()) {
                    do {
                        tag.add(new Tag_model(
                                cursor.getString(0),
                                cursor.getString(1)

                        ));
                    } while (cursor.moveToNext());

                    System.out.println(tag);

                    //passing the databasemanager instance this time to the adapter
                    adapter = new TagAdapter(getContext(), R.layout.list_layout_tag, tag, mDatabase);
                    listView.setAdapter(adapter);
                }


                tagadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tagname_str = tag_name.getText().toString();
                        tagval_str = tag_value.getText().toString();

                        if (!tagname_str.equals("") || !tagval_str.equals("") ) {

                            System.out.println("TAGTop: " + tagname_str + tagval_str);

                            //line 115


                            System.out.println("TAG: " + tagname_str + tagval_str);
                            mDatabase.addTag(tagname_str, tagval_str);
                        }else {
                            Toast.makeText(v.getContext(), "Please Fill Fields", Toast.LENGTH_SHORT).show();
                        }


                    }
                });



                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getContext());
                mDialogBuilder.setView(view);
                mDialogBuilder.setCancelable(true);
                mDialogBuilder.setTitle("Add Tags");


                final AlertDialog mAlertDialog = mDialogBuilder.create();
                mAlertDialog.show();


            }
        });






//        view.findViewById(R.id.buttonDeleteEmployee).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                deleteEmployee(employee);
//            }
//        });
//
//        view.findViewById(R.id.buttonUpdateEmployee).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                updateEmployee(employee);
//            }
//        });

        return view;
    }

    private void loadTagFromDatabase() {

    }

//    private void updateEmployee(final Album employee) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
//        LayoutInflater inflater = LayoutInflater.from(mCtx);
//        View view = inflater.inflate(R.layout.dialog_update_employee, null);
//        builder.setView(view);
//
//        final AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//
//        final EditText editTextName = view.findViewById(R.id.editTextName);
//
//
//        editTextName.setText(employee.getName());
//
//
//        view.findViewById(R.id.buttonUpdateEmployee).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String name = editTextName.getText().toString().trim();
//
//
//                if (name.isEmpty()) {
//                    editTextName.setError("Name can't be empty");
//                    editTextName.requestFocus();
//                    return;
//                }
//
//
//
//                //calling the update method from database manager instance
//                if (mDatabase.updateEmployee(employee.getId(), name)) {
//                    Toast.makeText(mCtx, "Album Updated", Toast.LENGTH_SHORT).show();
//                    loadEmployeesFromDatabaseAgain();
//                }
//                alertDialog.dismiss();
//            }
//        });
//    }
//
//    private void deleteEmployee(final Album employee) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
//        builder.setTitle("Are you sure?");
//
//        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                //calling the delete method from the database manager instance
//                if (mDatabase.deleteEmployee(employee.getId()))
//                    loadEmployeesFromDatabaseAgain();
//            }
//        });
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }
//
//    private void loadEmployeesFromDatabaseAgain() {
//        //calling the read method from database instance
//        Cursor cursor = mDatabase.getAllEmployees();
//
//        employeeList.clear();
//        if (cursor.moveToFirst()) {
//            do {
//                employeeList.add(new Album(
//                        cursor.getInt(0),
//                        cursor.getString(1)
//                ));
//            } while (cursor.moveToNext());
//        }
//        notifyDataSetChanged();
//    }
}

