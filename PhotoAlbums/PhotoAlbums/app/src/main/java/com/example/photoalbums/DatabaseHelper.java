package com.example.photoalbums;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    /*
     * This time we will not be using the hardcoded string values
     * Instead here we are defining all the Strings that is required for our database
     * for example databasename, table name and column names.
     * */
    private static final String DATABASE_NAME = "PhotosDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "albums";
    private static final String PHOTOS_TABLE_NAME = "photos";
    private static final String TAG_TABLE = "tag";
    private static final String ALBUM_ID = "albumid";
    private static final String ALBUM_NAME = "albumname";
    private static final String IMAGE_NAME = "imgname";
    private static final String IMAGE_PATH = "imgpath";
    private static final String IMAGE_CAPTION = "caption";
    private static final String IMAGE_LOADDATE = "imageloaddate";
    private static final String PHOTO_ID = "id";
    private static final String TAG_NAME = "tagname";
    private static final String TAG_VALUE = "tagvalue";


    /*
     * We need to call the super i.e. parent class constructur
     * And we need to pass 4 parameters
     * 1. Context context -> It is the context object we will get it from the activity while creating the instance
     * 2. String databasename -> It is the name of the database and here we are passing the constant that we already defined
     * 3. CursorFactory cursorFactory -> If we want a cursor to be initialized on the creation we can use cursor factory, it is optionall and that is why we passed null here
     * 4. int version -> It is an int defining our database version
     * */
    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /*
         * The query to create our table
         * It is same as we had in the previous post
         * The only difference here is we have changed the
         * hardcoded string values with String Variables
         * */

        String sql = "CREATE TABLE " + TABLE_NAME + " (\n" +
                "    " + ALBUM_ID + " INTEGER NOT NULL CONSTRAINT employees_pk PRIMARY KEY AUTOINCREMENT,\n" +
                "    " + ALBUM_NAME + " varchar(200) NOT NULL\n" +
                ");";

        String sqlphoto = "CREATE TABLE " + PHOTOS_TABLE_NAME + " (\n" +
                "    " + PHOTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    " + IMAGE_NAME + " varchar(200) NOT NULL,\n" +
                "    " + IMAGE_PATH + " varchar(500) NOT NULL,\n" +
                "    " + IMAGE_CAPTION + " varchar(200) NOT NULL,\n" +
                "    " + IMAGE_LOADDATE + " datetime  NOT NULL,\n" +
                "    " + ALBUM_ID + " integer references " + TABLE_NAME + " ("+ALBUM_ID+")"+
                ");";

        String sqltag = "CREATE TABLE " + TAG_TABLE + " (\n" +
                "    " + TAG_NAME + " varchar(200) NOT NULL,\n" +
                "    " + TAG_VALUE + " varchar(200) NOT NULL,\n" +
                "    " + PHOTO_ID + " integer references " + PHOTOS_TABLE_NAME + " ("+PHOTO_ID+")"+
                ");";

        /*
         * Executing the string to create the table
         * */
        sqLiteDatabase.execSQL(sql);
       sqLiteDatabase.execSQL(sqlphoto);
       sqLiteDatabase.execSQL(sqltag);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        /*
         * We are doing nothing here
         * Just dropping and creating the table
         * */
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
       String sql_photos = "DROP TABLE IF EXISTS " + PHOTOS_TABLE_NAME + ";";
       String sql_tag = "DROP TABLE IF EXISTS " + TAG_TABLE + ";";
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(sql_photos);
        sqLiteDatabase.execSQL(sql_tag);
        onCreate(sqLiteDatabase);
    }

    /*
     * CREATE OPERATION
     * ====================
     * This is the first operation of the CRUD.
     * This method will create a new employee in the table
     * Method is taking all the parameters required
     *
     * Operation is very simple, we just need a content value objects
     * Inside this object we will put everything that we want to insert.
     * So each value will take the column name and the value that is to inserted
     * for the column name we are using the String variables that we defined already
     * And that is why we converted the hardcoded string to variables
     *
     * Once we have the contentValues object with all the values required
     * We will call the method getWritableDatabase() and it will return us the SQLiteDatabase object and we can write on the database using it.
     *
     * With this object we will call the insert method it takes 3 parameters
     * 1. String -> The table name where the value is to be inserted
     * 2. String -> The default values of null columns, it is null here as we don't have any default values
     * 3. ContentValues -> The values that is to be inserted
     *
     * insert() will return the inserted row id, if there is some error inserting the row
     * it will return -1
     *
     * So here we are returning != -1, it will be true of record is inserted and false if not inserted
     * */

    boolean addEmployee(String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ALBUM_NAME, name);

        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABLE_NAME, null, contentValues) != -1;
    }

    boolean addTag(String tagname,String tagvalue) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TAG_NAME, tagname);
        contentValues.put(TAG_VALUE, tagvalue);

        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TAG_TABLE, null, contentValues) != -1;
    }


    /*
     * READ OPERATION
     * =================
     * Here we are reading values from the database
     * First we called the getReadableDatabase() method it will return us the SQLiteDatabase instance
     * but using it we can only perform the read operations.
     *
     * We are running rawQuery() method by passing the select query.
     * rawQuery takes two parameters
     * 1. The query
     * 2. String[] -> Arguments that is to be binded -> We use it when we have a where clause in our query to bind the where value
     *
     * rawQuery returns a Cursor object having all the data fetched from database
     * */
    Cursor getAllEmployees() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    Cursor getAllPhotos() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + PHOTOS_TABLE_NAME, null);
    }

    Cursor getAllTags() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TAG_TABLE, null);
    }

    /*
     * UPDATE OPERATION
     * ==================
     * Here we are performing the update operation. The proecess is same as the Create operation.
     * We are first getting a database instance using getWritableDatabase() method as the operation we need to perform is a write operation
     * Then we have the contentvalues object with the new values
     *
     * to update the row we use update() method. It takes 4 parameters
     * 1. String -> It is the table name
     * 2. ContentValues -> The new values
     * 3. String -> Here we pass the column name = ?, the column we want to use for putting the where clause
     * 4. String[] -> The values that is to be binded with the where clause
     * */
    boolean updateEmployee(int id, String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ALBUM_NAME, name);

        return db.update(TABLE_NAME, contentValues, ALBUM_ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }


    /*
     * DELETE OPERATION
     * ======================
     *
     * This is the last delete operation.  To delete again we need a writable database using getWritableDatabase()
     * Then we will call the delete method. It takes 3 parameters
     * 1. String -> Table name
     * 2. String -> The where clause passed as columnname = ?
     * 3. String[] -> The values to be binded on the where clause
     * */
    boolean deleteEmployee(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME, ALBUM_ID + "=?", new String[]{String.valueOf(id)}) == 1;
    }

    public boolean addPhotos(String ename, String eIname, String urii, String sysdate) {

      //  System.out.println("Image name: "+ename+"desc: "+urii+"date: "+sysdate+"path: "+eIname);
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGE_NAME, eIname);
        contentValues.put(IMAGE_PATH,urii);
        contentValues.put(IMAGE_CAPTION, ename);
        contentValues.put(IMAGE_LOADDATE, sysdate);

        SQLiteDatabase db = getWritableDatabase();
        return db.insert(PHOTOS_TABLE_NAME, null, contentValues) != -1;
    }
}
