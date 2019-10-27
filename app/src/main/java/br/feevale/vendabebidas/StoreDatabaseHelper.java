package br.feevale.vendabebidas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class StoreDatabaseHelper extends SQLiteOpenHelper {

    private static StoreDatabaseHelper sInstance;

    public static final String DATABASE_NAME = "vendasbebidas.db";
    public static final Integer DATABASE_VERSION = 10;
    private SQLiteDatabase db;
    private Context ctx;

    private StoreDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.ctx = ctx;
        this.db = this.getWritableDatabase();
    }

    public static synchronized StoreDatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new StoreDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public static class CustomerTable implements BaseColumns {
        public static final String TABLE_NAME = "customer";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_ADDRESS = "address";

        public static String getSQL() {
            String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_EMAIL + " TEXT, " +
                    COLUMN_ADDRESS + " TEXT)";
            return sql;
        }
    }

    public static class DrinkTable implements BaseColumns {
        public static final String TABLE_NAME = "drink";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_VOLUME = "volume";
        public static final String COLUMN_ISALCOHOLIC = "isalcoholic";
        public static final String COLUMN_PRICE = "price";

        public static String getSQL() {
            String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_VOLUME + " INTEGER, " +
                    COLUMN_ISALCOHOLIC + " INTEGER, " +
                    COLUMN_PRICE + " REAL)";
            return sql;
        }
    }

    public static class OrderTable implements BaseColumns {
        public static final String TABLE_NAME = "orders";
        public static final String COLUMN_CUSTOMER = "customer";
        public static final String COLUMN_TOTAL = "total";

        public static String getSQL() {
            String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_CUSTOMER + " INTEGER, " +
                    COLUMN_TOTAL + " REAL, " +
                    "FOREIGN KEY(" + COLUMN_CUSTOMER + ") REFERENCES " + CustomerTable.TABLE_NAME + "(" + CustomerTable._ID + "))";
            return sql;
        }
    }

    public static class OrderItemTable implements BaseColumns {
        public static final String TABLE_NAME = "orderitems";
        public static final String COLUMN_DRINK = "drink";
        public static final String COLUMN_QTY = "qty";
        public static final String COLUMN_ORDER = "order_id";

        public static String getSQL() {
            String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_DRINK + " INTEGER, " +
                    COLUMN_QTY + " INTEGER, " +
                    COLUMN_ORDER + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_DRINK + ") REFERENCES " + DrinkTable.TABLE_NAME + "(" + DrinkTable._ID + ")" +
                    "FOREIGN KEY(" + COLUMN_ORDER + ") REFERENCES " + OrderTable.TABLE_NAME + "(" + OrderTable._ID + "))";
            return sql;
        }
    }

    public Long addCustomer(Customer c) {
        ContentValues values = new ContentValues();
        values.put(CustomerTable.COLUMN_NAME, c.getName());
        values.put(CustomerTable.COLUMN_EMAIL, c.getEmail());
        values.put(CustomerTable.COLUMN_ADDRESS, c.getAddress());

        return db.insert(CustomerTable.TABLE_NAME, null, values);
    }

    public Long addDrink(Drink d) {
        ContentValues values = new ContentValues();
        values.put(DrinkTable.COLUMN_NAME, d.getName());
        values.put(DrinkTable.COLUMN_VOLUME, d.getVolume());
        values.put(DrinkTable.COLUMN_ISALCOHOLIC, d.getAlcoholic());
        values.put(DrinkTable.COLUMN_PRICE, d.getPrice());

        return db.insert(DrinkTable.TABLE_NAME, null, values);
    }

    public Long addOrder(Order o) {
        ContentValues values = new ContentValues();
        values.put(OrderTable.COLUMN_CUSTOMER, o.getCustomer().getId());
        values.put(OrderTable.COLUMN_TOTAL, o.getTotal());

        return db.insert(OrderTable.TABLE_NAME, null, values);
    }

    public Long addOrderItem(OrderItem o) {
        ContentValues values = new ContentValues();
        values.put(OrderItemTable.COLUMN_DRINK, o.getDrink().getId());
        values.put(OrderItemTable.COLUMN_QTY, o.getQty());
        values.put(OrderItemTable.COLUMN_ORDER, o.getOrder().getId());

        return db.insert(OrderItemTable.TABLE_NAME, null, values);
    }

    public Customer getCustomer(Long id) {
        String cols[] = {CustomerTable._ID, CustomerTable.COLUMN_NAME, CustomerTable.COLUMN_EMAIL, CustomerTable.COLUMN_ADDRESS};
        String args[] = {id.toString()};
        Cursor cursor = db.query(CustomerTable.TABLE_NAME, cols, CustomerTable._ID + "=?", args, null, null, CustomerTable._ID);

        if (cursor.getCount() == 0) {
            return null;
        }

        cursor.moveToFirst();
        Customer customer = new Customer();
        customer.setId(cursor.getLong(cursor.getColumnIndex(CustomerTable._ID)));
        customer.setName(cursor.getString(cursor.getColumnIndex(CustomerTable.COLUMN_NAME)));
        customer.setEmail(cursor.getString(cursor.getColumnIndex(CustomerTable.COLUMN_EMAIL)));
        customer.setAddress(cursor.getString(cursor.getColumnIndex(CustomerTable.COLUMN_ADDRESS)));

        return customer;
    }

    public Drink getDrink(Long id) {
        String cols[] = {DrinkTable._ID, DrinkTable.COLUMN_NAME, DrinkTable.COLUMN_VOLUME, DrinkTable.COLUMN_ISALCOHOLIC, DrinkTable.COLUMN_PRICE};
        String args[] = {id.toString()};
        Cursor cursor = db.query(DrinkTable.TABLE_NAME, cols, DrinkTable._ID + "=?", args, null, null, DrinkTable._ID);

        if (cursor.getCount() == 0) {
            return null;
        }

        cursor.moveToNext();
        Drink drink = new Drink();
        drink.setId(cursor.getLong(cursor.getColumnIndex(DrinkTable._ID)));
        drink.setName(cursor.getString(cursor.getColumnIndex(DrinkTable.COLUMN_NAME)));
        drink.setVolume(cursor.getInt(cursor.getColumnIndex(DrinkTable.COLUMN_VOLUME)));
        drink.setAlcoholic(cursor.getInt(cursor.getColumnIndex(DrinkTable.COLUMN_ISALCOHOLIC)));
        drink.setPrice(cursor.getDouble(cursor.getColumnIndex(DrinkTable.COLUMN_PRICE)));

        return drink;
    }

    public Order getOrder(Long id) {
        String cols[] = {OrderTable._ID, OrderTable.COLUMN_CUSTOMER, OrderTable.COLUMN_TOTAL};
        String args[] = {id.toString()};
        Cursor cursor = db.query(OrderTable.TABLE_NAME, cols, OrderTable._ID + "=?", args, null, null, OrderTable._ID);

        if (cursor.getCount() == 0) {
            return null;
        }

        cursor.moveToNext();
        Order order = new Order();
        order.setId(cursor.getLong(cursor.getColumnIndex(OrderTable._ID)));
        order.setCustomer(getCustomer(cursor.getLong(cursor.getColumnIndex(OrderTable.COLUMN_CUSTOMER))));
        order.setTotal(cursor.getDouble(cursor.getColumnIndex(OrderTable.COLUMN_TOTAL)));

        return order;
    }

    public List<Customer> getCustomers() {
        String cols[] = {CustomerTable._ID, CustomerTable.COLUMN_NAME, CustomerTable.COLUMN_EMAIL, CustomerTable.COLUMN_ADDRESS};
        Cursor cursor = db.query(CustomerTable.TABLE_NAME, cols, null, null, null, null, CustomerTable._ID);
        List<Customer> customers = new ArrayList<>();
        Customer customer;

        while (cursor.moveToNext()) {
            customer = new Customer();
            customer.setId(cursor.getLong(cursor.getColumnIndex(CustomerTable._ID)));
            customer.setName(cursor.getString(cursor.getColumnIndex(CustomerTable.COLUMN_NAME)));
            customer.setEmail(cursor.getString(cursor.getColumnIndex(CustomerTable.COLUMN_EMAIL)));
            customer.setAddress(cursor.getString(cursor.getColumnIndex(CustomerTable.COLUMN_ADDRESS)));
            customers.add(customer);
        }

        return customers;
    }

    public List<Drink> getDrinks() {
        String cols[] = {DrinkTable._ID, DrinkTable.COLUMN_NAME, DrinkTable.COLUMN_VOLUME, DrinkTable.COLUMN_ISALCOHOLIC, DrinkTable.COLUMN_PRICE};
        Cursor cursor = db.query(DrinkTable.TABLE_NAME, cols, null, null, null, null, DrinkTable._ID);
        List<Drink> drinks = new ArrayList<>();
        Drink drink;

        while (cursor.moveToNext()) {
            drink = new Drink();
            drink.setId(cursor.getLong(cursor.getColumnIndex(DrinkTable._ID)));
            drink.setName(cursor.getString(cursor.getColumnIndex(DrinkTable.COLUMN_NAME)));
            drink.setVolume(cursor.getInt(cursor.getColumnIndex(DrinkTable.COLUMN_VOLUME)));
            drink.setAlcoholic(cursor.getInt(cursor.getColumnIndex(DrinkTable.COLUMN_ISALCOHOLIC)));
            drink.setPrice(cursor.getDouble(cursor.getColumnIndex(DrinkTable.COLUMN_PRICE)));
            drinks.add(drink);
        }

        return drinks;
    }

    public List<Order> getOrders() {
        String cols[] = {OrderTable._ID, OrderTable.COLUMN_CUSTOMER, OrderTable.COLUMN_TOTAL};
        Cursor cursor = db.query(OrderTable.TABLE_NAME, cols, null, null, null, null, OrderTable._ID);
        List<Order> orders = new ArrayList<>();
        Order order;

        while (cursor.moveToNext()) {
            order = new Order();
            order.setId(cursor.getLong(cursor.getColumnIndex(OrderTable._ID)));
            Long customerId = Long.parseLong(cursor.getString(cursor.getColumnIndex(OrderTable.COLUMN_CUSTOMER)));
            order.setCustomer(getCustomer(customerId));
            order.setTotal(cursor.getDouble(cursor.getColumnIndex(OrderTable.COLUMN_TOTAL)));
            orders.add(order);
        }

        return orders;
    }

    public List<Order> getOrdersFromCustomer(Customer customer) {
        String cols[] = {OrderTable._ID, OrderTable.COLUMN_CUSTOMER, OrderTable.COLUMN_TOTAL};
        Cursor cursor = db.query(OrderTable.TABLE_NAME, cols, OrderTable.COLUMN_CUSTOMER + "=" + customer.getId(), null, null, null, OrderTable._ID);
        List<Order> orders = new ArrayList<>();
        Order order;

        while (cursor.moveToNext()) {
            order = new Order();
            order.setId(cursor.getLong(cursor.getColumnIndex(OrderTable._ID)));
            Long customerId = Long.parseLong(cursor.getString(cursor.getColumnIndex(OrderTable.COLUMN_CUSTOMER)));
            order.setCustomer(getCustomer(customerId));
            order.setTotal(cursor.getDouble(cursor.getColumnIndex(OrderTable.COLUMN_TOTAL)));
            orders.add(order);
        }

        return orders;
    }

    public List<OrderItem> getOrderItemsFromOrder(Order order) {
        String cols[] = {OrderItemTable._ID, OrderItemTable.COLUMN_ORDER, OrderItemTable.COLUMN_DRINK, OrderItemTable.COLUMN_QTY};
        Cursor cursor = db.query(OrderItemTable.TABLE_NAME, cols, OrderItemTable.COLUMN_ORDER + "=" + order.getId(), null, null, null, OrderItemTable._ID);
        List<OrderItem> items = new ArrayList<>();
        OrderItem item;

        while (cursor.moveToNext()) {
            item = new OrderItem();
            item.setId(cursor.getLong(cursor.getColumnIndex(OrderItemTable._ID)));
            item.setOrder(getOrder(cursor.getLong(cursor.getColumnIndex(OrderItemTable.COLUMN_ORDER))));
            item.setDrink(getDrink(cursor.getLong(cursor.getColumnIndex(OrderItemTable.COLUMN_DRINK))));
            item.setQty(cursor.getInt(cursor.getColumnIndex(OrderItemTable.COLUMN_QTY)));
            items.add(item);
        }

        return items;
    }

    public List<OrderItem> getOrderItemsFromDrink(Drink drink) {
        String cols[] = {OrderItemTable._ID, OrderItemTable.COLUMN_ORDER, OrderItemTable.COLUMN_DRINK, OrderItemTable.COLUMN_QTY};
        Cursor cursor = db.query(OrderItemTable.TABLE_NAME, cols, OrderItemTable.COLUMN_ORDER + "=" + drink.getId(), null, null, null, OrderItemTable._ID);
        List<OrderItem> items = new ArrayList<>();
        OrderItem item;

        while (cursor.moveToNext()) {
            item = new OrderItem();
            item.setId(cursor.getLong(cursor.getColumnIndex(OrderItemTable._ID)));
            item.setOrder(getOrder(cursor.getLong(cursor.getColumnIndex(OrderItemTable.COLUMN_ORDER))));
            item.setDrink(getDrink(cursor.getLong(cursor.getColumnIndex(OrderItemTable.COLUMN_DRINK))));
            item.setQty(cursor.getInt(cursor.getColumnIndex(OrderItemTable.COLUMN_QTY)));
            items.add(item);
        }

        return items;
    }

    public int updateCustomer(Customer c) {
        ContentValues values = new ContentValues();
        values.put(CustomerTable.COLUMN_NAME, c.getName());
        values.put(CustomerTable.COLUMN_EMAIL, c.getEmail());
        values.put(CustomerTable.COLUMN_ADDRESS, c.getAddress());
        String args[] = {c.getId().toString()};

        return db.update(CustomerTable.TABLE_NAME, values, CustomerTable._ID + "=?", args);
    }

    public int updateDrink(Drink d) {
        ContentValues values = new ContentValues();
        values.put(DrinkTable.COLUMN_NAME, d.getName());
        values.put(DrinkTable.COLUMN_VOLUME, d.getVolume());
        values.put(DrinkTable.COLUMN_ISALCOHOLIC, d.getAlcoholic());
        values.put(DrinkTable.COLUMN_PRICE, d.getPrice());
        String args[] = {d.getId().toString()};

        return db.update(DrinkTable.TABLE_NAME, values, DrinkTable._ID + "=?", args);
    }

    public int updateOrder(Order o) {
        ContentValues values = new ContentValues();
        values.put(OrderTable.COLUMN_CUSTOMER, o.getCustomer().getId());
        values.put(OrderTable.COLUMN_TOTAL, o.getTotal());
        String args[] = {o.getId().toString()};

        return db.update(OrderTable.TABLE_NAME, values, OrderTable._ID + "=?", args);
    }

    public void removeCustomer(Customer c) {
        String args[] = {c.getId().toString()};
        db.delete(CustomerTable.TABLE_NAME, CustomerTable._ID + "=?", args);
    }

    public void removeDrink(Drink d) {
        String args[] = {d.getId().toString()};
        db.delete(DrinkTable.TABLE_NAME, DrinkTable._ID + "=?", args);
    }

    public void removeOrder(Order o) {
        String args[] = {o.getId().toString()};
        db.delete(OrderItemTable.TABLE_NAME, OrderItemTable.COLUMN_ORDER + "=?", args);
        db.delete(OrderTable.TABLE_NAME, OrderTable._ID + "=?", args);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DrinkTable.getSQL());
        db.execSQL(CustomerTable.getSQL());
        db.execSQL(OrderTable.getSQL());
        db.execSQL(OrderItemTable.getSQL());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DrinkTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CustomerTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OrderTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + OrderItemTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}
