package hu.bute.daai.amorg.todolabor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hu.bute.daai.amorg.todolabor.data.Todo;

/**
 * A fragment representing a single Todo detail screen.
 * This fragment is either contained in a {@link TodoListActivity}
 * in two-pane mode (on tablets) or a {@link TodoDetailActivity}
 * on handsets.
 */
public class TodoDetailFragment extends Fragment {

    public static final String TAG = "TodoDetailFragment";

    public static final String KEY_TODO_DESCRIPTION = "todoDesc";

    private TextView todoDescription;

    private static Todo selectedTodo;

    public static TodoDetailFragment newInstance(String todoDesc) {
        TodoDetailFragment result = new TodoDetailFragment();

        Bundle args = new Bundle();
        args.putString(KEY_TODO_DESCRIPTION, todoDesc);
        result.setArguments(args);

        return result;
    }

    public static TodoDetailFragment newInstance(Bundle args) {
        TodoDetailFragment result = new TodoDetailFragment();

        result.setArguments(args);

        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            if (getArguments() != null) {
                selectedTodo = new Todo("cim", Todo.Priority.LOW, "1987.23.12",
                        getArguments().getString(KEY_TODO_DESCRIPTION));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.todo_detail, container,
                false);

        todoDescription = (TextView) root.findViewById(R.id.todo_detail);
        todoDescription.setText(selectedTodo.getDescription());

        return root;
    }

}