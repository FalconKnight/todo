package hu.bute.daai.amorg.todolabor.db;

public class DbConstants {
 
    // Broadcast Action, amely az adatbazis modosulasat jelzi
    public static final String ACTION_DATABASE_CHANGED = "hu.bute.daai.amorg.examples.DATABASE_CHANGED";
 
    // fajlnev, amiben az adatbazis lesz
    public static final String DATABASE_NAME = "data.db";
    // verzioszam
    public static final int DATABASE_VERSION = 1;
    // osszes belso osztaly DATABASE_CREATE szkriptje osszefuzve
    public static String DATABASE_CREATE_ALL = Todo.DATABASE_CREATE;
    // osszes belso osztaly DATABASE_DROP szkriptje osszefuzve
    public static String DATABASE_DROP_ALL = Todo.DATABASE_DROP;
     
    /* Todo osztaly DB konstansai */
    public static class Todo{
        // tabla neve
        public static final String DATABASE_TABLE = "todo";
        // oszlopnevek
        public static final String KEY_ROWID = "_id";
        public static final String KEY_TITLE = "title";
        public static final String KEY_PRIORITY = "priority";
        public static final String KEY_DUEDATE = "dueDate";
        public static final String KEY_DESCRIPTION = "description";
        // sema letrehozo szkript
        public static final String DATABASE_CREATE =
            "create table if not exists "+DATABASE_TABLE+" ( "
            + KEY_ROWID +" integer primary key autoincrement, "
            + KEY_TITLE + " text not null, "
            + KEY_PRIORITY + " text, "
            + KEY_DUEDATE +" text, "
            + KEY_DESCRIPTION +" text"
            + "); ";
        // sema torlo szkript
        public static final String DATABASE_DROP = 
            "drop table if exists " + DATABASE_TABLE + "; ";
    }
}