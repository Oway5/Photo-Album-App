package com.example.photoalbums;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Photos_class extends AppCompatActivity {

    Button create;
    DatabaseHelper mDatabaseHelper;
    PhotoAdapter adapter;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    String urii="";
    String sysdate="";
    List<Photos_model> picList;
    ListView listView;
    //PhotoAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_layot);

        Intent intent = getIntent();
        int intValue = intent.getIntExtra("key_id", 0);
        System.out.println("KEY: "+intValue);

        mDatabaseHelper = new DatabaseHelper(this);
        picList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listViewPhotos);

        loadPhotosFromDatabase();

        create = (Button) findViewById(R.id.add_photos);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sysdate = sdf.format(new Date());
               // textView.setText(currentDateandTime);

                add_photos();



            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Photos_class.this,MainActivity.class));
    }

    private void loadPhotosFromDatabase() {
        Cursor cursor = mDatabaseHelper.getAllPhotos();

        if (cursor.moveToFirst()) {
            do {
                picList.add(new Photos_model(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getInt(5)

                ));
            } while (cursor.moveToNext());

            //passing the databasemanager instance this time to the adapter
            adapter = new PhotoAdapter(this, R.layout.list_layout_photos, picList, mDatabaseHelper);
            listView.setAdapter(adapter);
        }
    }

    private void getimagedetails() {
        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this);
        builder.setTitle("Photo Details");

        // set the custom layout
        final View customLayout
                = getLayoutInflater()
                .inflate(
                        R.layout.custom_photos_layout,
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

                        System.out.println("URII: "+urii);
                        EditText picname=customLayout.findViewById(R.id.image_path);


                        TextView sysdates=customLayout.findViewById(R.id.getdate);
                        sysdates.setText("sysdate");

                        EditText editText
                                = customLayout
                                .findViewById(
                                        R.id.editTextdesc);
                        String ename = editText.getText().toString();
                        String eIname = picname.getText().toString();
                        if (editText.getText().toString().isEmpty()){
                            editText.setError("Name can't be empty");
                            editText.requestFocus();
                        }else if(picname.getText().toString().isEmpty()){}else {
                            sendDialogDataToActivity(
                                    ename, eIname, urii, sysdate);}
                    }
                });

        // create and show
        // the alert dialog
        AlertDialog dialog
                = builder.create();
        dialog.show();
    }

    private void sendDialogDataToActivity(String ename, String eIname, String urii, String sysdate ) {
        System.out.println("Image name: "+eIname+"desc: "+ename+"date: "+sysdate+"path: "+urii);
        if(mDatabaseHelper.addPhotos(ename,eIname,urii,sysdate));
        System.out.println("IU: "+urii);
        startActivity(new Intent(Photos_class.this,Photos_class.class));
    }


    private void add_photos() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);

}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            urii=imageUri.toString();

            if (urii.equals("")){
                System.out.println("err");
            }else {
                getimagedetails();
            }

        }
    }
}
