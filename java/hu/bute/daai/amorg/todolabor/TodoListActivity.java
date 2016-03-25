package hu.bute.daai.amorg.todolabor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import hu.bute.daai.amorg.todolabor.adapter.TodoAdapter;
import hu.bute.daai.amorg.todolabor.application.TodoApplication;
import hu.bute.daai.amorg.todolabor.data.Todo;
import hu.bute.daai.amorg.todolabor.db.DbConstants;
import hu.bute.daai.amorg.todolabor.db.TodoDbLoader;

/**
 * An activity representing a list of Todos. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link TodoDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class TodoListActivity extends AppCompatActivity implements TodoCreateFragment.ITodoCreateFragment {

    private RecyclerView recyclerView;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    // State
    private TodoAdapter adapter;
    private LocalBroadcastManager lbm;

    // DBloader
    private TodoDbLoader dbLoader;
    private GetAllTask getAllTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.todo_list);
        assert recyclerView != null;

        if (findViewById(R.id.todo_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        lbm = LocalBroadcastManager.getInstance(this);
        dbLoader = TodoApplication.getTodoDbLoader();
    }
    @Override
    public void onResume() {
        super.onResume();

        // Kódból regisztraljuk az adatbazis modosulasara figyelmezteto     Receiver-t
        IntentFilter filter = new IntentFilter(
                DbConstants.ACTION_DATABASE_CHANGED);
        lbm.registerReceiver(updateDbReceiver, filter);

        // Frissitjuk a lista tartalmat, ha visszater a user
        refreshList();
    }
    @Override
    public void onPause() {
        super.onPause();

        // Kiregisztraljuk az adatbazis modosulasara figyelmezteto  Receiver-t
        lbm.unregisterReceiver(updateDbReceiver);

        if (getAllTask != null) {
            getAllTask.cancel(false);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        // Ha van Cursor rendelve az Adapterhez, lezarjuk
        if (adapter != null && adapter.getCursor() != null) {
            adapter.getCursor().close();
        }
    }
     private void refreshList() {
        if (getAllTask != null) {
            getAllTask.cancel(false);
        }

        getAllTask = new GetAllTask();
        getAllTask.execute();
    }
    private void setupRecyclerView(@NonNull RecyclerView recyclerView, TodoAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }
    // ITodoCreateFragment
    @Override
// ITodoCreateFragment
    public void onTodoCreated(Todo newTodo) {
        dbLoader.createTodo(newTodo);

        refreshList();
    }
        private class GetAllTask extends AsyncTask<Void, Void, Cursor> {

                private static final String TAG = "GetAllTask";

                @Override
                protected Cursor doInBackground(Void... params) {
                    try {

                        Cursor result = dbLoader.fetchAll();

                        if (!isCancelled()) {
                            return result;
                        } else {
                            Log.d(TAG, "Cancelled, closing cursor");
                            if (result != null) {
                                result.close();
                            }

                            return null;
                        }
                    } catch (Exception e) {
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(Cursor result) {
                    super.onPostExecute(result);

                    Log.d(TAG, "Fetch completed, displaying cursor results!");
                    try {
                        if (adapter == null) {
                            adapter = new TodoAdapter(getApplicationContext(), result, mTwoPane);
                            setupRecyclerView(recyclerView, adapter);
                        } else {
                            adapter.changeCursor(result);
                        }
                        getAllTask = null;
                    } catch (Exception e) {
                    }
                }
            }

            private BroadcastReceiver updateDbReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    refreshList();
                }
            };

    /*
    public void deleteRow(int position) {
            /*todos.remove(position);
            notifyDataSetChanged();
    }


    public void addItem(Todo aTodo) {
        // todos.add(aTodo);
    }

    @Override
    public int getItemCount() {
        //return todos.size();
    }
     */



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itemCreateTodo) {
            TodoCreateFragment createFragment = new TodoCreateFragment();
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            createFragment.show(fm, TodoCreateFragment.TAG);
        }

        if (item.getItemId() == R.id.delete) {
            dbLoader.deleteall();
            refreshList();
        }
        return super.onOptionsItemSelected(item);
    }
}
