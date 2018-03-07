package com.giljam.daniel.chisquaredtest;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SetUpDialog extends DialogFragment implements SetupListAdapter.AdapterListener {

    /**
     * The {@link SetUpDialog}'s copy of the row names.
     */
    private List<String> rowNames = new ArrayList<>();

    /**
     * The {@link SetUpDialog}'s copy of the column names.
     */
    private List<String> colNames = new ArrayList<>();

    /**
     * The {@link SetUpDialog}'s copy of the values.
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
     * Variable for the {@link Button} that resets entire table,
     * emptying {@link SetUpDialog#rowList} and {@link SetUpDialog#colList}.
     */
    private Button resetTableButton;

    /**
     * Variable for the message that tells the max amount of rows and columns that can be set up.
     */
    private TextView tableSizeLimitationsText;

    /**
     * Variable for the {@link Button} that the user can add new rows to the {@link SetUpDialog#rowList} with.
     */
    private Button newRowButton;

    /**
     * Custom {@link RecyclerView.Adapter} for translating data
     * so that it can be displayed by the rowList {@link RecyclerView}.
     */
    private SetupListAdapter rowListAdapter;

    /**
     * List of the row headers in the table that's being set up.
     */
    private RecyclerView rowList;

    /**
     * Variable for the {@link Button} that the user can add new columns to the {@link SetUpDialog#colList} with.
     */
    private Button newColButton;

    /**
     * Custom {@link RecyclerView.Adapter} for translating data
     * so that it can be displayed by the colList {@link RecyclerView}.
     */
    private SetupListAdapter colListAdapter;

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
     * Indicates whether the table is identical to the default "new" table.
     * Determines if the "Reset table" -button is shown.
     */
    private boolean tableIsModified = false;

    public SetUpDialog() {

    }

    /**
     * Use this factory method to create an instance of this fragment.
     * @return A new instance of fragment TableLayoutFragment.
     */
    public static SetUpDialog newInstance(List<String> rowNames,
                                          List<String> colNames,
                                          List<List<Integer>> values) {
        // Instantiate this dialog
        SetUpDialog dialog = new SetUpDialog();

        // Pass provided parameter values (if they exist) to this instance of the dialog
        if (rowNames == null || colNames == null || values == null) {
            dialog.setUpNewTable();
        } else {
            dialog.rowNames = rowNames;
            dialog.colNames = colNames;
            dialog.values = values;
            dialog.areValuesPresent();
        }

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
     * View setup part extracted from the onCreateDialog() -method just to organize things a little bit more.
     */
    private void setUpViews() {

        clearValuesAndResetTableButton = dialogLayout.findViewById(R.id.clear_values_and_reset_table_button);
        if (valuesArePresent) {
            clearValuesAndResetTableButton.setText(R.string.clear_values_button_text);
            clearValuesAndResetTableButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clearValues();
                }
            });
        } else if (tableIsModified()) {
            clearValuesAndResetTableButton.setText(R.string.reset_table_button_text);
            clearValuesAndResetTableButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setUpNewTable();
                }
            });
        } else {
            clearValuesAndResetTableButton.setVisibility(View.GONE);
        }


        tableSizeLimitationsText = dialogLayout.findViewById(R.id.table_size_limitations_text);
        CharSequence tslText = Html.fromHtml(getString(R.string.table_size_limitations_text, 2, 2));
        tableSizeLimitationsText.setText(tslText);

        newRowButton = dialogLayout.findViewById(R.id.new_row_button);
        newRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newListItem(R.string.row_x);
            }
        });

        rowList = dialogLayout.findViewById(R.id.row_list);
        rowList.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
        rowListAdapter = new SetupListAdapter(this, rowNames);
        rowList.setAdapter(rowListAdapter);

        newColButton = dialogLayout.findViewById(R.id.new_col_button);
        newColButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newListItem(R.string.col_x);
            }
        });

        colList = dialogLayout.findViewById(R.id.col_list);
        colList.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
        colListAdapter = new SetupListAdapter(this, colNames);
        colList.setAdapter(colListAdapter);
    }

    /**
     * Sets a value to {@link SetUpDialog#valuesArePresent}
     * based on the contents of {@link SetUpDialog#values}.
     */
    private boolean areValuesPresent() {
        int i = 0;
        while (i < values.size() && !valuesArePresent) {
            int j = 0;
            while (j < values.get(i).size() && !valuesArePresent) {
                if (values.get(i).get(j) != 0) valuesArePresent = true;
                j++;
            }
            i++;
        }
        return valuesArePresent;
    }

    /**
     * Sets a value to {@link SetUpDialog#tableIsModified}
     * based on the contents of {@link SetUpDialog#rowNames} and {@link SetUpDialog#colNames}.
     */
    private boolean tableIsModified() {
        // TODO! Make this.
        return tableIsModified;
    }

    private void newListItem(int placeHolderId) {
        if (placeHolderId == R.string.row_x) {
            int place = rowNames.size();
            rowNames.add(getString(placeHolderId, place + 1));
            rowListAdapter.notifyItemInserted(place);
        } else if (placeHolderId == R.string.col_x) {
            int place = colNames.size();
            rowNames.add(getString(placeHolderId, place + 1));
            rowListAdapter.notifyItemInserted(place);
        }
    }

    /**
     * Clear the values contained within the {@link SetUpDialog#values} variable.
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

        rowListAdapter.notifyDataSetChanged();
        colListAdapter.notifyDataSetChanged();
    }

    public void MonitorInputField(EditText listItemInputField) {
        // TODO! Make this.
    }
}
