package com.giljam.daniel.chisquaredtest.setup;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.giljam.daniel.chisquaredtest.CustomEditText;
import com.giljam.daniel.chisquaredtest.MainActivity;
import com.giljam.daniel.chisquaredtest.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SetupDialog extends DialogFragment implements SetupListAdapter.AdapterListener, SetupListItemTouchHelper.ItemTouchHelperAdapter {

    /**
     * Maximum amount of rows that user is allowed to create.
     */
    private int maxRows;

    /**
     * Maximum amount of columns that user is allowed to create.
     */
    private int maxCols;

    /**
     * The {@link SetupDialog}'s copy of the row names.
     */
    private List<String> rowNames = new ArrayList<>();

    /**
     * The {@link SetupDialog}'s copy of the column names.
     */
    private List<String> colNames = new ArrayList<>();

    /**
     * The {@link SetupDialog}'s copy of the values.
     */
    private List<List<Integer>> values = new ArrayList<>();

    /**
     * Variable for the {@link ConstraintLayout} that is a significant "parent" layout in this fragment.
     */
    private View dialogLayout;

    /**
     * Variable for the {@link Button} that clears any existing values in the table.
     */
    private Button clearValuesAndResetTableButton;

    /**
     * Variable for the {@link Button} that the user can add new rows to the {@link SetupDialog#rowList} with.
     */
    private Button newRowButton;

    /**
     * Custom {@link RecyclerView.Adapter} for translating data
     * so that it can be displayed by the rowList {@link RecyclerView}.
     */
    private SetupListAdapter rowListAdapter;

    /**
     * Enables swipe and drag functionality for the for the rowList {@link RecyclerView}.
     */
    private SetupListItemTouchHelper rowListItemTouchHelper;

    /**
     * List of the row headers in the table that's being set up.
     */
    private RecyclerView rowList;

    /**
     * Variable for the {@link Button} that the user can add new columns to the {@link SetupDialog#colList} with.
     */
    private Button newColButton;

    /**
     * Custom {@link RecyclerView.Adapter} for translating data
     * so that it can be displayed by the colList {@link RecyclerView}.
     */
    private SetupListAdapter colListAdapter;

    /**
     * Enables swipe and drag functionality for the for the colList {@link RecyclerView}.
     */
    private SetupListItemTouchHelper colListItemTouchHelper;

    /**
     * List of the column headers in the table that's being set up.
     */
    private RecyclerView colList;

    /**
     * Indicates whether a table containing values was set up when this dialog was entered.
     * Determines if the "Clear values" -button is shown.
     */
    private boolean valuesArePresent = false;

    /**
     * Backs up the text that previously in the {@link EditText} input field
     * that the user is currently editing.
     */
    private String targetedInputFieldTextBackup = "";

    public SetupDialog() {

    }

    /**
     * Use this factory method to create an instance of this fragment.
     * @param maxDisplayableTable The return values of {@link MainActivity#CalculateMaxDisplayableTable()}.
     * @return A new instance of fragment TableLayoutFragment.
     */
    public static SetupDialog newInstance(int[] maxDisplayableTable) {

        // Instantiate this dialog
        SetupDialog dialog = new SetupDialog();

        // Pass provided parameter values (if they exist) to this instance of the dialog
        if (maxDisplayableTable[0] < 2) dialog.maxRows = 2;
        else dialog.maxRows = maxDisplayableTable[0];
        if (maxDisplayableTable[1] < 2) dialog.maxCols = 2;
        else dialog.maxCols = maxDisplayableTable[1];

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (rowNames.isEmpty() || colNames.isEmpty() || values.isEmpty()) setUpNewTable();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        dialogLayout = layoutInflater.inflate(R.layout.dialog_set_up, null, false);
        setUpViews();
        builder.setView(dialogLayout);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    /**
     * Custom "show" method that updates the {@link SetupDialog}'s copy
     * of the row names, column names and values.
     * @param manager A instance of {@link FragmentManager} needed to forward
     *                the "show" method to the superclasses' implementation of it.
     * @param rowNames This {@link SetupDialog}'s copy of the row names.
     * @param colNames This {@link SetupDialog}'s copy of the column names.
     * @param values This {@link SetupDialog}'s copy of the values.
     */
    public void show(FragmentManager manager, List<String> rowNames, List<String> colNames, List<List<Integer>> values) {
        if (rowNames != null && colNames != null && values != null) {
            this.rowNames = rowNames;
            this.colNames = colNames;
            this.values = values;
            areValuesPresent();
        }
        super.show(manager, getTag());
    }

    /**
     * View setup part extracted from the onCreateDialog() -method just to organize things a little bit more.
     */
    private void setUpViews() {

        dialogLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        clearValuesAndResetTableButton = dialogLayout.findViewById(R.id.clear_values_and_reset_table_button);
        if (valuesArePresent) {
            clearValuesAndResetTableButton.setText(R.string.clear_values_button_text);
            clearValuesAndResetTableButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clearValues();
                }
            });
        } else {
            clearValuesAndResetTableButton.setText(R.string.reset_table_button_text);
            clearValuesAndResetTableButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setUpNewTable();
                }
            });
        }

        TextView tableSizeLimitationsText = dialogLayout.findViewById(R.id.table_size_limitations_text);
        CharSequence tslText = Html.fromHtml(getString(R.string.table_size_limitations_text, maxRows, maxCols));
        tableSizeLimitationsText.setText(tslText);

        newRowButton = dialogLayout.findViewById(R.id.new_row_button);
        newRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newListItem(R.string.row_x);
            }
        });

        rowList = dialogLayout.findViewById(R.id.row_list);
        rowList.setLayoutManager(new SetupListLayoutManager(getContext()));
        if (rowNames.size() <= maxRows / 2) limitListSize(R.string.row_x, false);
        else limitListSize(R.string.row_x, true);
        rowListAdapter = new SetupListAdapter(this, R.string.row_x, rowNames);
        rowList.setAdapter(rowListAdapter);
        rowListItemTouchHelper = new SetupListItemTouchHelper(this, R.string.row_x);
        new ItemTouchHelper(rowListItemTouchHelper).attachToRecyclerView(rowList);

        newColButton = dialogLayout.findViewById(R.id.new_col_button);
        newColButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newListItem(R.string.col_x);
            }
        });

        colList = dialogLayout.findViewById(R.id.col_list);
        colList.setLayoutManager(new SetupListLayoutManager(getContext()));
        if (colNames.size() <= maxRows / 2) limitListSize(R.string.col_x, false);
        else limitListSize(R.string.col_x, true);
        colListAdapter = new SetupListAdapter(this, R.string.col_x, colNames);
        colList.setAdapter(colListAdapter);
        colListItemTouchHelper = new SetupListItemTouchHelper(this, R.string.col_x);
        new ItemTouchHelper(colListItemTouchHelper).attachToRecyclerView(colList);

        tableIsModified();
        checkTableLimits();
    }

    /**
     * Sets a value to {@link SetupDialog#valuesArePresent}
     * based on the contents of {@link SetupDialog#values}.
     */
    private void areValuesPresent() {
        int i = 0;
        while (i < values.size() && !valuesArePresent) {
            int j = 0;
            while (j < values.get(i).size() && !valuesArePresent) {
                if (values.get(i).get(j) != 0) valuesArePresent = true;
                j++;
            }
            i++;
        }
    }

    /**
     * Sets the visibility of {@link SetupDialog#clearValuesAndResetTableButton}
     * based on the contents of {@link SetupDialog#rowNames} and {@link SetupDialog#colNames}.
     */
    private void tableIsModified() {
        if (rowNames.size() == 2 && colNames.size() == 2) {
            boolean rn1 = rowNames.get(0).equals(getString(R.string.row_x, 1));
            boolean rn2 = rowNames.get(1).equals(getString(R.string.row_x, 2));
            boolean cn1 = colNames.get(0).equals(getString(R.string.col_x, 1));
            boolean cn2 = colNames.get(1).equals(getString(R.string.col_x, 2));
            if (!(rn1 && rn2 && cn1 && cn2)) {
                if (!valuesArePresent)
                    clearValuesAndResetTableButton.setEnabled(true);
            } else {
                if (!valuesArePresent)
                    clearValuesAndResetTableButton.setEnabled(false);
            }
        } else {
            if (!valuesArePresent)
                clearValuesAndResetTableButton.setEnabled(true);
        }
    }

    /**
     * Sets in function restrictions if necessary to prevent user to add/remove rows/columns
     * beyond min/max values.
     */
    private void checkTableLimits() {
        if (!newRowButton.isEnabled() && rowNames.size() < maxRows)
            newRowButton.setEnabled(true);
        if (!newColButton.isEnabled() && colNames.size() < maxCols)
            newColButton.setEnabled(true);
        if (!rowListItemTouchHelper.isItemViewSwipeEnabled() && rowNames.size() > 2)
            rowListItemTouchHelper.setItemViewSwipeEnabled(true);
        if (!colListItemTouchHelper.isItemViewSwipeEnabled() && colNames.size() > 2)
            colListItemTouchHelper.setItemViewSwipeEnabled(true);
        if (rowNames.size() == maxRows)
            newRowButton.setEnabled(false);
        if (colNames.size() == maxCols)
            newColButton.setEnabled(false);
        if (rowNames.size() == 2)
            rowListItemTouchHelper.setItemViewSwipeEnabled(false);
        if (colNames.size() == 2)
            colListItemTouchHelper.setItemViewSwipeEnabled(false);
    }

    private void limitListSize(int placeHolderId, boolean enabled) {
        if (enabled) {
            if (placeHolderId == R.string.row_x) {
                ViewGroup.LayoutParams rowListLayoutParams = rowList.getLayoutParams();
                rowListLayoutParams.height = ((int) getResources().getDimension(R.dimen.setup_list_item_height) * (maxRows / 2)) + 1;
                rowList.setLayoutParams(rowListLayoutParams);
                ((SetupListLayoutManager) rowList.getLayoutManager()).setCanScrollVertically(true);
            } else if (placeHolderId == R.string.col_x) {
                ViewGroup.LayoutParams colListLayoutParams = colList.getLayoutParams();
                colListLayoutParams.height = ((int) getResources().getDimension(R.dimen.setup_list_item_height) * (maxRows / 2)) + 1;
                colList.setLayoutParams(colListLayoutParams);
                ((SetupListLayoutManager) colList.getLayoutManager()).setCanScrollVertically(true);

            }
        } else {
            if (placeHolderId == R.string.row_x) {
                ViewGroup.LayoutParams rowListLayoutParams = rowList.getLayoutParams();
                rowListLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                rowList.setLayoutParams(rowListLayoutParams);
                ((SetupListLayoutManager) rowList.getLayoutManager()).setCanScrollVertically(false);
            } else if (placeHolderId == R.string.col_x) {
                ViewGroup.LayoutParams colListLayoutParams = colList.getLayoutParams();
                colListLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                colList.setLayoutParams(colListLayoutParams);
                ((SetupListLayoutManager) colList.getLayoutManager()).setCanScrollVertically(false);
            }
        }
    }

    private void newListItem(int placeHolderId) {
        if (placeHolderId == R.string.row_x) {
            if (rowNames.size() == maxRows / 2) limitListSize(placeHolderId, true);
            int place = rowNames.size();
            rowNames.add(getString(placeHolderId, place + 1));
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < colNames.size(); j++) row.add(0);
            values.add(row);
            rowListAdapter.notifyItemInserted(place);
            rowList.smoothScrollToPosition(place);
        } else if (placeHolderId == R.string.col_x) {
            if (colNames.size() == maxRows / 2) limitListSize(placeHolderId, true);
            int place = colNames.size();
            colNames.add(getString(placeHolderId, place + 1));
            for (List<Integer> row : values) row.add(0);
            colListAdapter.notifyItemInserted(place);
            colList.smoothScrollToPosition(place);
        }
        tableIsModified();
        checkTableLimits();
    }

    /**
     * Clear the values contained within the {@link SetupDialog#values} variable.
     */
    private void clearValues() {

        // properly resetting the values variable
        if (values != null) values.clear();
        else values = new ArrayList<>();

        // for loop within a for loop in order to (re)initialize each cell with a default (0) value
        for (int i = 0; i < rowNames.size(); i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < colNames.size(); j++) row.add(0);
            values.add(row);
        }

        valuesArePresent = false;
        clearValuesAndResetTableButton.setText(R.string.reset_table_button_text);
        clearValuesAndResetTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpNewTable();
            }
        });
        tableIsModified();
    }

    /**
     * Creates a table with the minimum dimensions allowed (2x2) by reformatting
     * the variables that hold theTable's data (the high-priority variables: rowNames, colNames, values).
     */
    private void setUpNewTable() {

        // properly resetting following variables
        if (rowNames != null) rowNames.clear();
        else rowNames = new ArrayList<>();
        if (colNames != null) colNames.clear();
        else colNames = new ArrayList<>();
        if (values != null) values.clear();
        else values = new ArrayList<>();

        // (re)initializing row and column names with strings provided as parameters
        rowNames.add(getString(R.string.row_x, 1));
        rowNames.add(getString(R.string.row_x, 2));
        colNames.add(getString(R.string.col_x, 1));
        colNames.add(getString(R.string.col_x, 2));

        // for loop within a for loop in order to (re)initialize each cell with a default (0) value
        for (int i = 0; i < rowNames.size(); i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < colNames.size(); j++) row.add(0);
            values.add(row);
        }

        tableIsModified();
        checkTableLimits();
        rowListAdapter.notifyDataSetChanged();
        colListAdapter.notifyDataSetChanged();
    }

    public void RemoveListItem(int index, int placeHolderId) {
        if (placeHolderId == R.string.row_x) {
            rowNames.remove(index);
            values.remove(index);
            rowListAdapter.notifyItemRemoved(index);
            if (rowNames.size() == maxRows / 2) limitListSize(placeHolderId, false);
        } else if (placeHolderId == R.string.col_x) {
            colNames.remove(index);
            for (List<Integer> row : values) row.remove(index);
            colListAdapter.notifyItemRemoved(index);
            if (colNames.size() == maxRows / 2) limitListSize(placeHolderId, false);
        }
        areValuesPresent();
        tableIsModified();
        checkTableLimits();
    }

    public void MoveListItem(int startIndex, int endIndex, int placeHolderId) {
        if (placeHolderId == R.string.row_x) {
            Collections.swap(rowNames, startIndex, endIndex);
            Collections.swap(values, startIndex, endIndex);
            rowListAdapter.notifyItemMoved(startIndex, endIndex);
        } else if (placeHolderId == R.string.col_x) {
            Collections.swap(colNames, startIndex, endIndex);
            for (List<Integer> row : values) Collections.swap(row, startIndex, endIndex);
            colListAdapter.notifyItemMoved(startIndex, endIndex);
        }
        tableIsModified();
        checkTableLimits();
    }

    public void MonitorInputField(CustomEditText listItemInputField, final int position, final int placeHolderId) {
        listItemInputField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String inputFieldText = ((EditText) view).getText().toString();
                if (b) {
                    targetedInputFieldTextBackup = inputFieldText;
                } else {
                    if (inputFieldText.isEmpty())
                        ((EditText)view).setText(targetedInputFieldTextBackup);
                    else if (!inputFieldText.equals(targetedInputFieldTextBackup)) {
                        if (placeHolderId == R.string.row_x) rowNames.set(position, inputFieldText);
                        else if (placeHolderId == R.string.col_x) colNames.set(position, inputFieldText);
                        tableIsModified();
                    }
                }
            }
        });
        listItemInputField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    dialogLayout.requestFocus();
                    return false;
                }
                return true;
            }
        });
        listItemInputField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    dialogLayout.requestFocus();
                    return false;
                }
                return false;
            }
        });
        listItemInputField.setSkbInterceptor(new CustomEditText.SoftKeyboardBackInterceptor() {
            @Override
            public void OnBackPressed() {
                dialogLayout.requestFocus();
            }
        });
    }
}
