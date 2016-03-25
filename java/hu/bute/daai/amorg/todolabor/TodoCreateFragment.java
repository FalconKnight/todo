package hu.bute.daai.amorg.todolabor;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import hu.bute.daai.amorg.todolabor.data.Todo;

/**
 * Created by android on 2016. 03. 08..
 */
public class TodoCreateFragment extends DialogFragment implements DatePickerDialogFragment.IDatePickerDialogFragment  {

    // Log tag
    public static final String TAG = "TodoCreateFragment";

    // UI
    private EditText editTodoTitle;
    private Spinner spnrTodoPriority;
    private TextView txtDueDate;
    private EditText editTodoDescription;

    // Listener
    private ITodoCreateFragment listener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (getTargetFragment() != null) {
            try {
                listener = (ITodoCreateFragment) getTargetFragment();
            } catch (ClassCastException ce) {
                Log.e(TAG,
                        "Target Fragment does not implement fragment interface!");
            } catch (Exception e) {
                Log.e(TAG, "Unhandled exception!");
                e.printStackTrace();
            }
        } else {
            try {
                listener = (ITodoCreateFragment) activity;
            } catch (ClassCastException ce) {
                Log.e(TAG,
                        "Parent Activity does not implement fragment interface!");
            } catch (Exception e) {
                Log.e(TAG, "Unhandled exception!");
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.createtodo, container, false);

        getDialog().setTitle(R.string.itemCreateTodo);

        editTodoTitle = (EditText) root.findViewById(R.id.todoTitle);

        spnrTodoPriority = (Spinner) root.findViewById(R.id.todoPriority);
        String[] priorities = new String[3];
        priorities[0] = "Low";
        priorities[1] = "Medium";
        priorities[2] = "High";
        spnrTodoPriority.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, priorities));

        txtDueDate = (TextView) root.findViewById(R.id.todoDueDate);
        txtDueDate.setText("  -  ");
        txtDueDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        editTodoDescription = (EditText) root
                .findViewById(R.id.todoDescription);

        // A gombok esemenykezeloinek beallitasa
        Button btnOk = (Button) root.findViewById(R.id.btnCreateTodo);
        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Todo.Priority selectedPriority = Todo.Priority.LOW;

                switch (spnrTodoPriority.getSelectedItemPosition()) {
                    case 0:
                        selectedPriority = Todo.Priority.LOW;
                        break;
                    case 1:
                        selectedPriority = Todo.Priority.MEDIUM;
                        break;
                    case 2:
                        selectedPriority = Todo.Priority.HIGH;
                        break;
                    default:
                        break;
                }

                if (listener != null) {
                    listener.onTodoCreated(new Todo(editTodoTitle.getText()
                            .toString(), selectedPriority, txtDueDate.getText()
                            .toString(), editTodoDescription.getText()
                            .toString()));
                }

                dismiss();
            }
        });

        Button btnCancel = (Button) root.findViewById(R.id.btnCancelCreateTodo);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

        return root;
    }

    private void showDatePickerDialog() {
        FragmentManager fm = getFragmentManager();

        DatePickerDialogFragment datePicker = new DatePickerDialogFragment();
        datePicker.setTargetFragment(this, 0);
        datePicker.show(fm, DatePickerDialogFragment.TAG);
    }

    // IDatePickerDialogFragment
    @Override
    public void onDateSelected(String date) {
        txtDueDate.setText(date);
    }

    // Listener interface
    public interface ITodoCreateFragment {
        public void onTodoCreated(Todo newTodo);
    }
}