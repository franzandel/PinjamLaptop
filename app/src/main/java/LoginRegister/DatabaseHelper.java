package LoginRegister;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by OK on 4/9/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "PINJAMLAPTOP.db";

    // Account table name
    private static final String TABLE_ACCOUNT = "ACCOUNT";
    private static final String TABLE_M_LAPTOP = "M_LAPTOP";
    private static final String TABLE_P_LAPTOP = "P_LAPTOP";
    private static final String TABLE_ANGGOTA = "ANGGOTA";
    private static final String TABLE_INVENTARIS = "INVENTARIS";
    private static final String TABLE_M_PINJAM = "M_PINJAM";
    private static final String TABLE_P_PINJAM = "P_PINJAM";
    private static final String TABLE_KEMBALI = "KEMBALI";

    // Account Table Columns names
    private static final String COLUMN_EMAIL = "EMAIL";
    private static final String COLUMN_NAMA = "NAMA";
    private static final String COLUMN_PASSWORD = "PASSWORD";

    private static final String COLUMN_NO_LAPTOP = "NO_LAPTOP";
    private static final String COLUMN_QTY_TOTAL = "QTY_TOTAL";
    private static final String COLUMN_SATUAN = "SATUAN";
    private static final String COLUMN_QTY_BAIK = "QTY_BAIK";
    private static final String COLUMN_QTY_RUSAK = "QTY_RUSAK";
    private static final String COLUMN_QTY_HILANG = "QTY_HILANG";

    private static final String COLUMN_KD_MEREK = "KD_MEREK";
    private static final String COLUMN_KD_PROCESSOR = "KD_PROCESSOR";
    private static final String COLUMN_KD_VGA = "KD_VGA";
    private static final String COLUMN_MODEL = "MODEL";
    private static final String COLUMN_RAM = "RAM";
    private static final String COLUMN_HDD = "HDD";

    private static final String COLUMN_NO_ANGGOTA = "NO_ANGGOTA";
    private static final String COLUMN_NM_ANGGOTA = "NM_ANGGOTA";
    private static final String COLUMN_GENDER = "GENDER";
    private static final String COLUMN_ALAMAT = "ALAMAT";
    private static final String COLUMN_NO_TELP = "NO_TELP";
    private static final String COLUMN_EMAIL_ANGGOTA = "EMAIL_ANGGOTA";
    private static final String COLUMN_FOTO_ANGGOTA = "FOTO_ANGGOTA";

    private static final String COLUMN_KD_PINJAM = "KD_PINJAM";
    private static final String COLUMN_TGL_PINJAM = "TGL_PINJAM";

    private static final String COLUMN_QTY_PINJAM = "QTY_PINJAM";
    private static final String COLUMN_KEPERLUAN = "KEPERLUAN";

    private static final String COLUMN_STATUS = "STATUS";

    private static final String COLUMN_KD_INVENTARIS = "KD_INVENTARIS";
    private static final String COLUMN_TGL_KEMBALI = "TGL_KEMBALI";
    private static final String COLUMN_STATUS_KEMBALI = "STATUS_KEMBALI";

    // create table sql query
    private String CREATE_TABLE_ACCOUNT =
            "CREATE TABLE " + TABLE_ACCOUNT + "("
            + COLUMN_EMAIL + " TEXT PRIMARY KEY," + COLUMN_NAMA + " TEXT,"
            + COLUMN_PASSWORD + " TEXT" + ")";

    private String CREATE_TABLE_M_LAPTOP =
            "CREATE TABLE " + TABLE_M_LAPTOP + "("
                    + COLUMN_NO_LAPTOP + " TEXT PRIMARY KEY," + COLUMN_QTY_TOTAL + " INT,"
                    + COLUMN_SATUAN + " TEXT," + COLUMN_QTY_BAIK + " INT,"
                    + COLUMN_QTY_RUSAK + " INT," + COLUMN_QTY_HILANG + " INT" + ")";

    private String CREATE_TABLE_P_LAPTOP =
            "CREATE TABLE " + TABLE_P_LAPTOP + "("
                    + COLUMN_NO_LAPTOP + " TEXT," + COLUMN_KD_MEREK + " TEXT,"
                    + COLUMN_KD_PROCESSOR + " TEXT," + COLUMN_KD_VGA + " TEXT,"
                    + COLUMN_MODEL + " TEXT," + COLUMN_RAM + " TEXT,"
                    + COLUMN_HDD + " TEXT, " +
                    "FOREIGN KEY (" + COLUMN_NO_LAPTOP + ") " +
                    "REFERENCES " + TABLE_M_LAPTOP + "(" + COLUMN_NO_LAPTOP + "))";

    private String CREATE_TABLE_ANGGOTA =
            "CREATE TABLE " + TABLE_ANGGOTA + "("
            + COLUMN_NO_ANGGOTA + " TEXT PRIMARY KEY," + COLUMN_NM_ANGGOTA + " TEXT,"
            + COLUMN_GENDER + " TEXT," + COLUMN_ALAMAT + " TEXT,"
            + COLUMN_NO_TELP + " TEXT," + COLUMN_EMAIL_ANGGOTA + " TEXT,"
            + COLUMN_FOTO_ANGGOTA + " NONE" + ")";

    private String CREATE_TABLE_INVENTARIS =
            "CREATE TABLE " + TABLE_INVENTARIS + "("
            + COLUMN_KD_INVENTARIS + " TEXT PRIMARY KEY," + COLUMN_NO_LAPTOP + " TEXT,"
            + COLUMN_TGL_PINJAM + " TEXT," + COLUMN_TGL_KEMBALI + " TEXT,"
            + COLUMN_STATUS + " INT" + ")";

    private String CREATE_TABLE_M_PINJAM =
            "CREATE TABLE " + TABLE_M_PINJAM + "("
            + COLUMN_KD_PINJAM + " TEXT PRIMARY KEY," + COLUMN_NO_ANGGOTA + " TEXT,"
            + COLUMN_TGL_PINJAM + " TEXT, " +
            "FOREIGN KEY (" + COLUMN_NO_ANGGOTA + ") " +
            "REFERENCES " + TABLE_ANGGOTA + "(" + COLUMN_NO_ANGGOTA + "))";

    private String CREATE_TABLE_P_PINJAM =
            "CREATE TABLE " + TABLE_P_PINJAM + "("
            + COLUMN_KD_PINJAM + " TEXT," + COLUMN_NO_LAPTOP + " TEXT,"
            + COLUMN_QTY_PINJAM + " INT," + COLUMN_KEPERLUAN + " TEXT, " +
            "FOREIGN KEY (" + COLUMN_KD_PINJAM + ") " +
            "REFERENCES " + TABLE_M_PINJAM + "(" + COLUMN_KD_PINJAM + "), " +
            "FOREIGN KEY (" + COLUMN_NO_LAPTOP + ") " +
            "REFERENCES " + TABLE_M_LAPTOP + "(" + COLUMN_NO_LAPTOP + "))";

    private String CREATE_TABLE_KEMBALI =
            "CREATE TABLE " + TABLE_KEMBALI + "("
            + COLUMN_KD_PINJAM + " TEXT," + COLUMN_KD_INVENTARIS + " TEXT,"
            + COLUMN_TGL_KEMBALI + " TEXT," + COLUMN_STATUS_KEMBALI + " INT, " +
            "FOREIGN KEY (" + COLUMN_KD_PINJAM + ") " +
            "REFERENCES " + TABLE_M_PINJAM + "(" + COLUMN_KD_PINJAM + "), " +
            "FOREIGN KEY (" + COLUMN_KD_INVENTARIS + ") " +
            "REFERENCES " + TABLE_INVENTARIS + "(" + COLUMN_KD_INVENTARIS + "))";

    // drop table sql query
    private String DROP_TABLE_ACCOUNT = "DROP TABLE IF EXISTS " + TABLE_ACCOUNT;
    private String DROP_TABLE_M_LAPTOP = "DROP TABLE IF EXISTS " + TABLE_M_LAPTOP;
    private String DROP_TABLE_P_LAPTOP = "DROP TABLE IF EXISTS " + TABLE_P_LAPTOP;
    private String DROP_TABLE_ANGGOTA = "DROP TABLE IF EXISTS " + TABLE_ANGGOTA;
    private String DROP_TABLE_INVENTARIS = "DROP TABLE IF EXISTS " + TABLE_INVENTARIS;
    private String DROP_TABLE_M_PINJAM = "DROP TABLE IF EXISTS " + TABLE_M_PINJAM;
    private String DROP_TABLE_P_PINJAM = "DROP TABLE IF EXISTS " + TABLE_P_PINJAM;
    private String DROP_TABLE_KEMBALI = "DROP TABLE IF EXISTS " + TABLE_KEMBALI;

    SQLiteDatabase sqLiteDatabase;
    Laptop laptop;
    Peminjaman peminjaman;

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        laptop = new Laptop();
        peminjaman = new Peminjaman();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACCOUNT);
        db.execSQL(CREATE_TABLE_M_LAPTOP);
        db.execSQL(CREATE_TABLE_P_LAPTOP);
        db.execSQL(CREATE_TABLE_ANGGOTA);
        db.execSQL(CREATE_TABLE_INVENTARIS);
        db.execSQL(CREATE_TABLE_M_PINJAM);
        db.execSQL(CREATE_TABLE_P_PINJAM);
        db.execSQL(CREATE_TABLE_KEMBALI);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop Account Table if exist
        db.execSQL(DROP_TABLE_ACCOUNT);
        db.execSQL(DROP_TABLE_M_LAPTOP);
        db.execSQL(DROP_TABLE_P_LAPTOP);
        db.execSQL(DROP_TABLE_ANGGOTA);
        db.execSQL(DROP_TABLE_INVENTARIS);
        db.execSQL(DROP_TABLE_M_PINJAM);
        db.execSQL(DROP_TABLE_P_PINJAM);
        db.execSQL(DROP_TABLE_KEMBALI);

        // Create tables again
        onCreate(db);

    }

    /**
     * This method is to create account record
     *
     * @param account
     */
    public void addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA, account.getNama());
        values.put(COLUMN_EMAIL, account.getEmail());
        values.put(COLUMN_PASSWORD, account.getPassword());

        // Inserting Row
        db.insert(TABLE_ACCOUNT, null, values);
        db.close();
    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<Account> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_EMAIL,
                COLUMN_NAMA,
                COLUMN_PASSWORD
        };
        // sorting orders
        String sortOrder =
                COLUMN_NAMA + " ASC";
        List<Account> accountList = new ArrayList<Account>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_ACCOUNT, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Account account = new Account();
                account.setNama(cursor.getString(cursor.getColumnIndex(COLUMN_NAMA)));
                account.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                account.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
                // Adding account record to list
                accountList.add(account);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return accountList;
    }

    /**
     * This method to update account record
     *
     * @param account
     */
    public void updateUser(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA, account.getNama());
        values.put(COLUMN_PASSWORD, account.getPassword());

        // updating row
        db.update(TABLE_ACCOUNT, values, COLUMN_EMAIL + " = ?",
                new String[]{String.valueOf(account.getEmail())});
        db.close();
    }

    /**
     * This method is to delete account record
     *
     * @param account
     */
    public void deleteUser(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete account record by id
        db.delete(TABLE_ACCOUNT, COLUMN_EMAIL + " = ?",
                new String[]{String.valueOf(account.getEmail())});
        db.close();
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_EMAIL
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_ACCOUNT, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_EMAIL
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_EMAIL + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_ACCOUNT, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /*public void addLaptop(Laptop laptop) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TIPE_LAPTOP, laptop.getTipe_Laptop());
        values.put(COLUMN_EMAIL, laptop.getEmail());
        values.put(COLUMN_MEREK_LAPTOP, laptop.getMerek_Laptop());
        values.put(COLUMN_BY_PINJAMHARI, laptop.getBy_PinjamHari());
        values.put(COLUMN_BY_TELATHARI, laptop.getBy_TelatHari());

        // Inserting Row
        db.insert(TABLE_DFTR_LAPTOP, null, values);
        db.close();
    }

    public boolean checkLaptop(String tipe) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_TIPE_LAPTOP
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_TIPE_LAPTOP + " = ?";

        // selection arguments
        String[] selectionArgs = {tipe};

        // query user table with conditions
        *//**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         *//*
        Cursor cursor = db.query(TABLE_DFTR_LAPTOP, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public void deleteLaptop(Laptop laptop) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DFTR_LAPTOP, COLUMN_TIPE_LAPTOP + " = ?",
                new String[]{laptop.getTipe_Laptop()});
        db.close();
    }

    public List<Laptop> getAllLaptop() {
        String[] columns = {
                COLUMN_TIPE_LAPTOP,
                COLUMN_MEREK_LAPTOP,
                COLUMN_BY_PINJAMHARI,
                COLUMN_BY_TELATHARI
        };

        String sortOrder =
                COLUMN_TIPE_LAPTOP + " ASC";
        List<Laptop> laptopList = new ArrayList<Laptop>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DFTR_LAPTOP, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        if (cursor.moveToFirst()) {
            do {
                Laptop laptop = new Laptop();
                laptop.setTipe_Laptop(cursor.getString(cursor.getColumnIndex(COLUMN_TIPE_LAPTOP)));
                laptop.setMerek_Laptop(cursor.getString(cursor.getColumnIndex(COLUMN_MEREK_LAPTOP)));
                laptop.setBy_PinjamHari(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BY_PINJAMHARI))));
                laptop.setBy_TelatHari(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BY_TELATHARI))));
                laptopList.add(laptop);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return laptopList;
    }

    public List<Laptop> cariDataLike(String tipe) {
        List<Laptop> laptopList = new ArrayList<Laptop>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DFTR_LAPTOP +
                " WHERE TIPE_LAPTOP LIKE '%" + tipe + "%'", null);

        if (cursor.moveToFirst()) {
            do {
                laptop.setTipe_Laptop(cursor.getString(cursor.getColumnIndex(COLUMN_TIPE_LAPTOP)));
                laptop.setMerek_Laptop(cursor.getString(cursor.getColumnIndex(COLUMN_MEREK_LAPTOP)));
                laptop.setBy_PinjamHari(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BY_PINJAMHARI))));
                laptop.setBy_TelatHari(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BY_TELATHARI))));
                laptopList.add(laptop);
            } while (cursor.moveToNext());
        }
        return laptopList;
    }
*/
    /*public List<Peminjaman> getAllPinjaman() {
        String[] columns = {
                COLUMN_NO_PINJAM,
                COLUMN_TGL_PINJAM,
                COLUMN_TIPE_LAPTOP,
                COLUMN_PEMINJAM,
                COLUMN_LAMA_PINJAM,
                COLUMN_BY_PINJAM
        };

        String sortOrder =
                COLUMN_NO_PINJAM + " ASC";
        List<Peminjaman> peminjamanList = new ArrayList<Peminjaman>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PEMINJAMAN, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        if (cursor.moveToFirst()) {
            do {
                Peminjaman peminjaman = new Peminjaman();
                peminjaman.setNo_Pinjam(cursor.getString(cursor.getColumnIndex(COLUMN_NO_PINJAM)));
                peminjaman.setTgl_Pinjam(cursor.getString(cursor.getColumnIndex(COLUMN_TGL_PINJAM)));
                peminjaman.setTipe_Laptop(cursor.getString(cursor.getColumnIndex(COLUMN_TIPE_LAPTOP)));
                peminjaman.setPeminjam(cursor.getString(cursor.getColumnIndex(COLUMN_PEMINJAM)));
                peminjaman.setLama_Pinjam(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_LAMA_PINJAM))));
                peminjaman.setBy_Pinjam(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BY_PINJAM))));
                peminjamanList.add(peminjaman);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return peminjamanList;
    }*/

    public void addM_Pinjam(Peminjaman peminjaman) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_KD_PINJAM, peminjaman.getKd_Pinjam());
        values.put(COLUMN_NO_ANGGOTA, peminjaman.getNo_Anggota());
        values.put(COLUMN_TGL_PINJAM, peminjaman.getTgl_Pinjam());

        // Inserting Row
        db.insert(TABLE_M_PINJAM, null, values);
        db.close();
    }

    public void addP_Pinjam(Peminjaman peminjaman) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String,String>> listHash = peminjaman.getListLaptop();

        ContentValues values = new ContentValues();
        for (int i = 0; i < listHash.size() - 1; i++) {
            values.put(COLUMN_KD_PINJAM, peminjaman.getKd_Pinjam());
            values.put(COLUMN_NO_LAPTOP, peminjaman.getNo_Laptop());
            values.put(COLUMN_KEPERLUAN, peminjaman.getKeperluan());

            // Inserting Row
            db.insert(TABLE_P_PINJAM, null, values);
        }

        db.close();
    }

    public void addInventaris(Peminjaman peminjaman) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        for (int i = 0; i < peminjaman.getListLaptop().size() - 1; i++) {
            for (int j = 0; i < peminjaman.getQty_Pinjam() - 1; i++) {
                values.put(COLUMN_KD_INVENTARIS, peminjaman.getNo_Laptop() + "-00" + j);
                values.put(COLUMN_NO_LAPTOP, peminjaman.getNo_Laptop());
                values.put(COLUMN_TGL_PINJAM, peminjaman.getTgl_Pinjam());
                values.put(COLUMN_STATUS, peminjaman.getStatus());

                // Inserting Row
                db.insert(TABLE_INVENTARIS, null, values);
            }
        }

        db.close();
    }

    /*public void deletePinjaman(Peminjaman peminjaman) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PEMINJAMAN, COLUMN_NO_PINJAM + " = ?",
                new String[]{peminjaman.getNo_Pinjam()});
        db.close();
    }*/

    /*public List<Peminjaman> cariDataLikePinj(String tipe) {
        List<Peminjaman> peminjamanList = new ArrayList<Peminjaman>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PEMINJAMAN +
                " WHERE TIPE_LAPTOP LIKE '%" + tipe + "%'", null);

        if (cursor.moveToFirst()) {
            do {
                *//*peminjaman.setTipe_Laptop(cursor.getString(cursor.getColumnIndex(COLUMN_TIPE_LAPTOP)));
                peminjaman.setMerek_Laptop(cursor.getString(cursor.getColumnIndex(COLUMN_MEREK_LAPTOP)));
                peminjaman.setBy_PinjamHari(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BY_PINJAMHARI))));
                peminjaman.setBy_TelatHari(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BY_TELATHARI))));*//*
                peminjamanList.add(peminjaman);
            } while (cursor.moveToNext());
        }
        return peminjamanList;
    }*/

    public String getPass(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_PASSWORD
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_ACCOUNT, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();


        if (cursorCount > 0) {
            if (cursor.moveToFirst()) {
                String pass = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
                cursor.close();
                db.close();
                return pass;
            }
        }

        cursor.close();
        db.close();
        return "";
    }

    public void changePass(String email, String newPass) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPass);

        // updating row
        db.update(TABLE_ACCOUNT, values, COLUMN_EMAIL + " = ?",
                new String[]{email});
        db.close();
    }

    public boolean checkPinjaman(Peminjaman peminjaman) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_KD_PINJAM
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_KD_PINJAM + " = ?";

        // selection arguments
        String[] selectionArgs = {peminjaman.getKd_Pinjam()};

        Cursor cursor = db.query(TABLE_M_PINJAM, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /*public void addPengembalian(Pengembalian pengembalian) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NO_PINJAM, pengembalian.getNo_Pinjam());
        values.put(COLUMN_EMAIL, pengembalian.getEmail());
        values.put(COLUMN_TGL_KEMBALI, pengembalian.getTgl_Kembali());
        values.put(COLUMN_BY_TELAT, pengembalian.getBy_Telat());

        // Inserting Row
        db.insert(TABLE_PENGEMBALIAN, null, values);
        db.close();
    }

    public List<Peminjaman> getAllPengembalian() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Peminjaman> peminjamanList = new ArrayList<Peminjaman>();
        String MY_QUERY = "SELECT A.NO_PINJAM, A.TIPE_LAPTOP, A.TGL_PINJAM, A.BY_PINJAM, B.BY_TELAT, " +
                "B.TGL_KEMBALI FROM PEMINJAMAN A LEFT JOIN PENGEMBALIAN B ON A.NO_PINJAM = B.NO_PINJAM";
        Cursor cursor = db.rawQuery(MY_QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                Peminjaman peminjaman = new Peminjaman();
                peminjaman.setNo_Pinjam(cursor.getString(cursor.getColumnIndex(COLUMN_NO_PINJAM)));
                peminjaman.setTipe_Laptop(cursor.getString(cursor.getColumnIndex(COLUMN_TIPE_LAPTOP)));
                peminjaman.setTgl_Pinjam(cursor.getString(cursor.getColumnIndex(COLUMN_TGL_PINJAM)));
                peminjaman.setBy_Pinjam(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BY_PINJAM))));
                if (cursor.getString(cursor.getColumnIndex(COLUMN_BY_TELAT)) != null) {
                    peminjaman.setBy_Telat(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BY_TELAT))));
                    peminjaman.setTgl_Kembali(cursor.getString(cursor.getColumnIndex(COLUMN_TGL_KEMBALI)));
                }
                peminjamanList.add(peminjaman);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return peminjamanList;
    }

    public String getTglPinjam(String noPinjam) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_TGL_PINJAM
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_NO_PINJAM + " = ?";

        // selection argument
        String[] selectionArgs = {noPinjam};

        // query user table with condition
        *//**//**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         *//**//*
        Cursor cursor = db.query(TABLE_PEMINJAMAN, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();

        if (cursorCount > 0) {
            if (cursor.moveToFirst()) {
                String tglPinjam = cursor.getString(cursor.getColumnIndex(COLUMN_TGL_PINJAM));
                cursor.close();
                db.close();
                return tglPinjam;
            }
        }

        cursor.close();
        db.close();
        return "";
    }*/
}
