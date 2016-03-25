package hu.bute.daai.amorg.todolabor.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import hu.bute.daai.amorg.todolabor.data.Todo;

public class TodoDbLoader {
     
    private Context ctx;
    private DatabaseHelper dbHelper;
    static private SQLiteDatabase mDb;
     
    public TodoDbLoader(Context ctx) {
        this.ctx = ctx;
    }

    public void deleteall(){
        mDb.execSQL(DbConstants.DATABASE_DROP_ALL);
        mDb.execSQL(DbConstants.DATABASE_CREATE_ALL);
    }
     
    public void open() throws SQLException {
        // DatabaseHelper objektum
        dbHelper = new DatabaseHelper(
                ctx, DbConstants.DATABASE_NAME);
        // adatbázis objektum
        mDb = dbHelper.getWritableDatabase();
        // ha nincs még séma, akkor létrehozzuk
        dbHelper.onCreate(mDb);
    }
     
    public void close(){
        dbHelper.close();
    }
    // CRUD és egyéb metódusok

    // INSERT
    public long createTodo(Todo todo){
        ContentValues values = new ContentValues();
        values.put(DbConstants.Todo.KEY_TITLE, todo.getTitle());
        values.put(DbConstants.Todo.KEY_DUEDATE, todo.getDueDate());
        values.put(DbConstants.Todo.KEY_DESCRIPTION, todo.getDescription());
        values.put(DbConstants.Todo.KEY_PRIORITY, todo.getPriority().name());

        return mDb.insert(DbConstants.Todo.DATABASE_TABLE, null, values);
    }

    // DELETE
    static public boolean deleteTodo(long rowId){
        return mDb.delete(
                DbConstants.Todo.DATABASE_TABLE,
                DbConstants.Todo.KEY_ROWID + "=" + rowId,
                null) > 0;
    }

    // UPDATE
    public boolean updateProduct(long rowId, Todo newTodo){
        ContentValues values = new ContentValues();
        values.put(DbConstants.Todo.KEY_TITLE, newTodo.getTitle());
        values.put(DbConstants.Todo.KEY_DUEDATE, newTodo.getDueDate());
        values.put(DbConstants.Todo.KEY_DESCRIPTION, newTodo.getDescription());
        values.put(DbConstants.Todo.KEY_PRIORITY, newTodo.getPriority().name());
        return mDb.update(
                DbConstants.Todo.DATABASE_TABLE,
                values,
                DbConstants.Todo.KEY_ROWID + "=" + rowId,
                null) > 0;
    }

    // minden Todo lekérése
    public Cursor fetchAll(){
        // cursor minden rekordra (where = null)
        return mDb.query(
                DbConstants.Todo.DATABASE_TABLE,
                new String[]{
                        DbConstants.Todo.KEY_ROWID,
                        DbConstants.Todo.KEY_TITLE,
                        DbConstants.Todo.KEY_DESCRIPTION,
                        DbConstants.Todo.KEY_DUEDATE,
                        DbConstants.Todo.KEY_PRIORITY
                }, null, null, null, null, DbConstants.Todo.KEY_TITLE);
    }

    // egy Todo lekérése
    public Todo fetchTodo(long rowId){
        // a Todo-ra mutato cursor
        Cursor c = mDb.query(
                DbConstants.Todo.DATABASE_TABLE,
                new String[]{
                        DbConstants.Todo.KEY_ROWID,
                        DbConstants.Todo.KEY_TITLE,
                        DbConstants.Todo.KEY_DESCRIPTION,
                        DbConstants.Todo.KEY_DUEDATE,
                        DbConstants.Todo.KEY_PRIORITY
                }, DbConstants.Todo.KEY_ROWID + "=" + rowId,
                null, null, null, DbConstants.Todo.KEY_TITLE);
        // ha van rekord amire a Cursor mutat
        if(c.moveToFirst())
            return getTodoByCursor(c);
        // egyebkent null-al terunk vissza
        return null;
    }
    public static Todo getTodoByCursor(Cursor c){
        return new Todo(
                c.getString(c.getColumnIndex(DbConstants.Todo.KEY_TITLE)), // title
                Todo.Priority.valueOf(c.getString(c.getColumnIndex(DbConstants.Todo.KEY_PRIORITY))), // priority
                c.getString(c.getColumnIndex(DbConstants.Todo.KEY_DUEDATE)), // dueDate
                c.getString(c.getColumnIndex(DbConstants.Todo.KEY_DESCRIPTION)) // description
        );
    }
}