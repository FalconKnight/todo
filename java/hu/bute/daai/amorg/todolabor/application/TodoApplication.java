package hu.bute.daai.amorg.todolabor.application;

import android.app.Application;

import java.sql.SQLException;

import hu.bute.daai.amorg.todolabor.db.TodoDbLoader;

public class TodoApplication extends Application {
    private static TodoDbLoader dbLoader;
 
    public static TodoDbLoader getTodoDbLoader() {
        return dbLoader;
    }
 
    @Override
    public void onCreate() {
        super.onCreate();
 
        dbLoader = new TodoDbLoader(this);
        try {
            dbLoader.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    @Override
    public void onTerminate() {
        // Close the internal db
        dbLoader.close();
         
        super.onTerminate();
    }
}