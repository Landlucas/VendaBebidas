package br.feevale.vendabebidas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class VendasDatabase {
    private Context ctx;
    public static final String DATABASE_NAME = "vendasbebidas.db";
    public static final Integer DATABASE_VERSION = 1;
    private SQLiteDatabase db;
    private VendasDatabaseHelper dbHelper;

    public VendasDatabase(Context ctx){
        this.ctx = ctx;
        dbHelper = new VendasDatabaseHelper();
        db = dbHelper.getWritableDatabase();
    }

    public static class CustomerTable implements BaseColumns{
        public static final String TABLE_NAME = "customer";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_ADDRESS = "address";

        public static String getSQL(){
            String sql = "CREATE TABLE " + TABLE_NAME + " ("+
                    _ID                  + " INTEGER PRIMARY KEY, " +
                    COLUMN_NAME          + " TEXT, " +
                    COLUMN_EMAIL         + " TEXT, " +
                    COLUMN_ADDRESS       + " TEXT)";
            return sql;
        }
    }

    public static class DrinkTable implements BaseColumns{
        public static final String TABLE_NAME = "drink";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_VOLUME = "volume";
        public static final String COLUMN_ISALCOHOLIC = "isalcoholic";
        public static final String COLUMN_PRICE = "price";

        public static String getSQL(){
            String sql = "CREATE TABLE " + TABLE_NAME + " ("+
                    _ID                  + " INTEGER PRIMARY KEY, " +
                    COLUMN_NAME          + " TEXT, " +
                    COLUMN_VOLUME        + " INTEGER, " +
                    COLUMN_ISALCOHOLIC   + " INTEGER, " +
                    COLUMN_PRICE         + " TEXT)";
            return sql;
        }
    }

    public Long addCustomer(Customer c){
        ContentValues values = new ContentValues();
        values.put(CustomerTable.COLUMN_NAME, c.getName());
        values.put(CustomerTable.COLUMN_EMAIL, c.getEmail());
        values.put(CustomerTable.COLUMN_ADDRESS, c.getAddress());

        return db.insert(CustomerTable.TABLE_NAME, null, values);
    }

    public Long addDrink(Drink s){
        ContentValues values = new ContentValues();
        values.put(DrinkTable.COLUMN_NAME, s.getName());
        values.put(DrinkTable.COLUMN_VOLUME, s.getVolume());
        values.put(DrinkTable.COLUMN_ISALCOHOLIC, s.getAlcoholic());
        values.put(DrinkTable.COLUMN_PRICE, s.getPrice());

        return db.insert(DrinkTable.TABLE_NAME, null, values);
    }

    public Customer getCustomer(Long id){
        String cols[] = {CustomerTable._ID, CustomerTable.COLUMN_NAME, CustomerTable.COLUMN_EMAIL, CustomerTable.COLUMN_ADDRESS};
        String args[] = {id.toString()};
        Cursor cursor = db.query(CustomerTable.TABLE_NAME, cols, CustomerTable._ID+"=?", args, null, null, CustomerTable._ID);

        if(cursor.getCount() != 0){
            return null;
        }

        cursor.moveToNext();
        Customer customer = new Customer();
        customer.setId(cursor.getLong(cursor.getColumnIndex(CustomerTable._ID)));
        customer.setName(cursor.getString(cursor.getColumnIndex(CustomerTable.COLUMN_NAME)));
        customer.setEmail(cursor.getString(cursor.getColumnIndex(CustomerTable.COLUMN_EMAIL)));
        customer.setAddress(cursor.getString(cursor.getColumnIndex(CustomerTable.COLUMN_ADDRESS)));

        return customer;
    }

    public Drink getDrink(Long id){
        String cols[] = {DrinkTable._ID, DrinkTable.COLUMN_NAME, DrinkTable.COLUMN_VOLUME, DrinkTable.COLUMN_ISALCOHOLIC, DrinkTable.COLUMN_PRICE};
        String args[] = {id.toString()};
        Cursor cursor = db.query(DrinkTable.TABLE_NAME, cols, DrinkTable._ID+"=?", args, null, null, DrinkTable._ID);

        if(cursor.getCount() != 0){
            return null;
        }

        cursor.moveToNext();
        Drink drink = new Drink();
        drink.setId(cursor.getLong(cursor.getColumnIndex(DrinkTable._ID)));
        drink.setName(cursor.getString(cursor.getColumnIndex(DrinkTable.COLUMN_NAME)));
        drink.setVolume(cursor.getInt(cursor.getColumnIndex(DrinkTable.COLUMN_VOLUME)));
        drink.setAlcoholic(cursor.getInt(cursor.getColumnIndex(DrinkTable.COLUMN_ISALCOHOLIC)));
        drink.setPrice(cursor.getLong(cursor.getColumnIndex(DrinkTable.COLUMN_PRICE)));

        return drink;
    }

    public List<Customer> getCustomers(){
        String cols[] = {CustomerTable._ID, CustomerTable.COLUMN_NAME, CustomerTable.COLUMN_EMAIL, CustomerTable.COLUMN_ADDRESS};
        Cursor cursor = db.query(CustomerTable.TABLE_NAME, cols, null, null, null, null, CustomerTable._ID);
        List<Customer> customers = new ArrayList<>();
        Customer customer;

        while(cursor.moveToNext()){
            customer = new Customer();
            customer.setId(cursor.getLong(cursor.getColumnIndex(CustomerTable._ID)));
            customer.setName(cursor.getString(cursor.getColumnIndex(CustomerTable.COLUMN_NAME)));
            customer.setEmail(cursor.getString(cursor.getColumnIndex(CustomerTable.COLUMN_EMAIL)));
            customer.setAddress(cursor.getString(cursor.getColumnIndex(CustomerTable.COLUMN_ADDRESS)));
            customers.add(customer);
        }

        return customers;
    }

    private class VendasDatabaseHelper extends SQLiteOpenHelper{

        VendasDatabaseHelper(){
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DrinkTable.getSQL());
            db.execSQL(CustomerTable.getSQL());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DrinkTable.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + CustomerTable.TABLE_NAME);
            onCreate(db);
        }

        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.setForeignKeyConstraintsEnabled(true);
        }
    }
}
