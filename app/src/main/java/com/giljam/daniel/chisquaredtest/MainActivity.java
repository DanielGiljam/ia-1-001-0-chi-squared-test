package com.giljam.daniel.chisquaredtest;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.giljam.daniel.chisquaredtest.setup.SetupDialog;
import com.giljam.daniel.chisquaredtest.tablelayout.CellAdapter;
import com.giljam.daniel.chisquaredtest.tablelayout.TableLayoutFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements CellAdapter.ValueListener {

    /**
     * This variable is required for performing chi-squared-tests.<br>
     * It consists of the table data provided by the other high-priority variables,
     * represented by a specialized table object model.
     * Is the first out of four high-priority variables.
     */
    private Table theTable;

    /**
     * Contains the names of the rows as strings.<br>
     * The position of a string in the list tells which row's (nth row) name that string represents.<br>
     * The size of the list tells how many rows there are in total.
     * Is the second out of four high-priority variables.
     */
    private List<String> rowNames;

    /**
     * Contains the names of the columns as strings.<br>
     * The position of a string in the list tells which column's (nth column) name that string represents.<br>
     * The size of the list tells how many columns there are in total.
     * Is the third out of four high-priority variables.
     */
    private List<String> colNames;

    /**
     * Contains the grid of values.<br>
     * Consists of a list of lists containing values (integer decimals).<br>
     * Each (inner) list holds the values for one row.<br>
     * Each (inner) list's position in its "super-list" tells which row's (nth row) values it holds.<br>
     * Each value in an inner list belongs to a column.<br>
     * The value's position in the (inner) list that it's in, tells which column (nth column) it belongs to.<br>
     * The size of the list should be equal to the size of the rowNames -list.<br>
     * The sizes of the inner lists should be equal to the size of the colNames -list.<br>
     * This is the fourth and last high-priority variable.
     */
    private List<List<Integer>> values;

    /**
     * Variable for the fragment that draws the grid of values onto the screen as an interactive table.
     * Fragment makes up a big part of the app.
     * Variable used for executing the fragment's methods and functions from this activity (the main activity).
     */
    private TableLayoutFragment tableLayoutFragment;

    /**
     * Variable for fragment for setting up the table that shows as a dialog.
     */
    private SetupDialog setupDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // default onCreate() code lines
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initialize high-priority variables
        theTable = null;
        rowNames = null;
        colNames = null;
        values = null;

        // initialize variable for table layout fragment
        tableLayoutFragment = (TableLayoutFragment) getFragmentManager().findFragmentById(R.id.table_layout_fragment);

        // initialize variable for setup dialog
        setupDialog = SetupDialog.newInstance(CalculateMaxDisplayableTable());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.settings_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // Course of actions taken is determined based on the id of the action bar item
        if (id == R.id.set_up) {
            // Show the setup dialog
            setupDialog.show(getSupportFragmentManager(), rowNames, colNames, values);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Changes theTable into a table with the minimum dimensions allowed (2x2) by reformatting the variables that hold theTable's data (the high-priority variables: rowNames, colNames, values).<br>
     * The subsequently executed Refresh() -method takes care of inflating the new table layout onto the screen.
     * @param row1Name The first row's name, provided as a string.
     * @param row2Name The second row's name, provided as a string.
     * @param col1Name The first column's name, provided as a string.
     * @param col2Name The second column's name, provided as a string.
     */
    private void CreateMinimumTable(String row1Name, String row2Name, String col1Name, String col2Name) {

        // properly resetting following variables
        if (rowNames != null) rowNames.clear();
        else rowNames = new ArrayList<>();
        if (colNames != null) colNames.clear();
        else colNames = new ArrayList<>();
        if (values != null) values.clear();
        else values = new ArrayList<>();

        // (re)initializing row and column names with strings provided as parameters
        rowNames.add(row1Name);
        rowNames.add(row2Name);
        colNames.add(col1Name);
        colNames.add(col2Name);

        // for loop within a for loop in order to (re)initialize each cell with a default (0) value
        for (int i = 0; i < rowNames.size(); i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < colNames.size(); j++) row.add(0);
            values.add(row);
        }
        Refresh();
    }

    /**
     * Generates a table of provided dimensions with placeholder row and column names. For layout previewing purposes during app development phase.
     * @param rows How many rows the table will have.
     * @param columns How many columns the table will have.
     */
    private void CreateExampleTable(int rows, int columns) {

        if (rows > 2 && columns > 2) {

            List<String> exampleRowNames = new ArrayList<>();
            List<String> exampleColNames = new ArrayList<>();

            for (int i = 1; i <= rows; i++)
                exampleRowNames.add(String.format(Locale.getDefault(),"Row %1$d", i));
            for (int i = 1; i <= columns; i++)
                exampleColNames.add(String.format(Locale.getDefault(), "Column %1$d", i));

            CreateTable(exampleRowNames, exampleColNames);

        } else CreateMinimumTable("Row 1", "Row 2", "Column 1", "Column 2");
    }

    /**
     * Changes theTable into a table with implicit dimensions (implied by the sizes of the lists provided as parameters).<br>
     * It reformats the variables that hold theTable's data (the high-priority variables: rowNames, colNames, values).<br>
     * The subsequently executed Refresh() -method takes care of inflating the new table layout onto the screen.
     * @param rowNames The names of the rows as strings.
     * @param colNames The names of the columns as strings.
     */
    private void CreateTable(List<String> rowNames, List<String> colNames) {

        // properly resetting following variables
        if (this.rowNames != null) this.rowNames.clear();
        else this.rowNames = new ArrayList<>();
        if (this.colNames != null) this.colNames.clear();
        else this.colNames = new ArrayList<>();
        if (this.values != null) this.values.clear();
        else this.values = new ArrayList<>();

        // adding provided data to the rowNames and colNames variables
        this.rowNames.addAll(rowNames);
        this.colNames.addAll(colNames);

        // for loop within a for loop in order to (re)initialize each cell with a default (0) value
        for (int i = 0; i < rowNames.size(); i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < colNames.size(); j++) row.add(0);
            this.values.add(row);
        }
        Refresh();
    }

    /**
     * Calculates the maximum supported table size based on the screen dimension of current device.
     * @return Decimal integer array where index 0 tells maximum supported rows and index 1 tells maximum supported columns.
     */
    public int[] CalculateMaxDisplayableTable() {
        float[] screenDimensions = GetScreenDimensions();
        int rowsThatFit = (int) ((screenDimensions[1] - getResources().getDimension(R.dimen.table_height_other)) / getResources().getDimension(R.dimen.table_row_height));
        int colsThatFit = (int) ((screenDimensions[0] - getResources().getDimension(R.dimen.table_width_other)) / getResources().getDimension(R.dimen.table_column_width));
        if (rowsThatFit % 2 != 0) rowsThatFit -= 1;
        if (colsThatFit % 2 != 0) colsThatFit -= 1;
        return new int[] {rowsThatFit, colsThatFit};
    }

    /**
     * Fetches the dimensions of the available viewport.
     * @return two floats packed in an array, representing x and y dimensions in dp.
     */
    private float[] GetScreenDimensions() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        return new float[] {outMetrics.widthPixels, outMetrics.heightPixels};
    }

    /**
     * Should be triggered each time a modification has occured in any of the high-priority variables.<br>
     * This method updates the chi-squared-test information by first supplying the specialized table object with the latest data (as it's needed for the chi-squared-test).<br>
     * Then it updates the table layout shown on the screen by notifying all adapters about data change and, if necessary, taking care of any inflation/deflation of the table layout.
     */
    private void Refresh() {
        tableLayoutFragment.notifyDataSetsChanged(rowNames, colNames, values);
    }

    public void OnValueChanged(int value, int rowIndex, int colIndex, boolean showBreadcrumb) {
        // TODO! Implement OnValueChanged(...) method in MainActivity
    }

    public void ShowSpecifyValueDialog(final String value, final int rowIndex, final int colIndex) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        final View dialogLayout = layoutInflater.inflate(R.layout.dialog_specify_value, null, false);
        final TextView snt = dialogLayout.findViewById(R.id.specify_value_title);
        final CustomEditText cet = dialogLayout.findViewById(R.id.specify_value_input_field);

        snt.setText(getString(R.string.dialog_specify_value_title, rowNames.get(rowIndex), colNames.get(colIndex)));
        cet.setText(getString(R.string.specify_value_input_field_text, value));
        cet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String inputFieldText = ((EditText) view).getText().toString();
                if (!b && inputFieldText.isEmpty()) ((EditText)view).setText(value);
            }
        });
        cet.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    dialogLayout.requestFocus();
                    return false;
                }
                return true;
            }
        });
        cet.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    dialogLayout.requestFocus();
                    return false;
                }
                return false;
            }
        });
        cet.setSkbInterceptor(new CustomEditText.SoftKeyboardBackInterceptor() {
            @Override
            public void OnBackPressed() {
                dialogLayout.requestFocus();
            }
        });

        builder.setView(dialogLayout);

        final AlertDialog dialog = builder.create();

        dialog.show();
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        cet.requestFocus();

        dialogLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                String inputFieldText = cet.getText().toString();
                if (cet.isFocused()) {
                    if (inputFieldText.isEmpty()) inputFieldText = value;
                    InputMethodManager inputMethodManager = (InputMethodManager) cet.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(cet.getWindowToken(), 0);
                } else {
                    InputMethodManager inputMethodManager = (InputMethodManager) dialogLayout.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(dialogLayout.getWindowToken(), 0);
                }
                OnValueChanged(Integer.parseInt(inputFieldText), rowIndex, colIndex, false);
            }
        });
    }
}
