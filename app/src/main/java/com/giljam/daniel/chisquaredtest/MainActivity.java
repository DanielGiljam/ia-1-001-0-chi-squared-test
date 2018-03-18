package com.giljam.daniel.chisquaredtest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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
import android.widget.Toast;

import com.giljam.daniel.chisquaredtest.chi.Table;
import com.giljam.daniel.chisquaredtest.setup.SetupDialog;
import com.giljam.daniel.chisquaredtest.tablelayout.CellAdapter;
import com.giljam.daniel.chisquaredtest.tablelayout.TableLayoutFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.giljam.daniel.chisquaredtest.chi.Misc.valuesProjectionGenerator;

public class MainActivity extends AppCompatActivity implements SetupDialog.SetupListener, CellAdapter.ValueListener {

    /**
     * Contains the names of the rows as strings.<br>
     * The position of a string in the list tells which row's (nth row) name that string represents.<br>
     * The size of the list tells how many rows there are in total.
     * Is the first out of four high-priority variables.
     */
    private List<String> rowNames = new ArrayList<>();

    /**
     * Contains the names of the columns as strings.<br>
     * The position of a string in the list tells which column's (nth column) name that string represents.<br>
     * The size of the list tells how many columns there are in total.
     * Is the second out of four high-priority variables.
     */
    private List<String> colNames = new ArrayList<>();

    /**
     * Contains the grid of values.<br>
     * Consists of a list of lists containing values (integer decimals).<br>
     * Each (inner) list holds the values for one row.<br>
     * Each (inner) list's position in its "super-list" tells which row's (nth row) values it holds.<br>
     * Each value in an inner list belongs to a column.<br>
     * The value's position in the (inner) list that it's in, tells which column (nth column) it belongs to.<br>
     * The size of the list should be equal to the size of the rowNames -list.<br>
     * The sizes of the inner lists should be equal to the size of the colNames -list.<br>
     * Is the third out of four high-priority variables.
     */
    private List<List<Integer>> values = new ArrayList<>();

    /**
     * This variable is required for performing chi-squared-tests.<br>
     * It consists of the Table data provided by the other high-priority variables,
     * represented by a specialized Table object model.
     * This is the fourth and last high-priority variable.
     */
    private Table theTable;

    /**
     * Reference to the TextView that displays the significance analysis result.
     */
    private TextView significanceText;

    /**
     * Variable for the fragment that draws the grid of values onto the screen as an interactive Table.
     * Fragment makes up a big part of the app.
     * Variable used for executing the fragment's methods and functions from this activity (the main activity).
     */
    private TableLayoutFragment tableLayoutFragment;

    /**
     * Variable for fragment for setting up the Table that shows as a dialog.
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

        // set up reference to the significance text view
        significanceText = findViewById(R.id.significance_text);

        // initialize variable for Table layout fragment
        tableLayoutFragment = (TableLayoutFragment) getFragmentManager().findFragmentById(R.id.table_layout_fragment);

        // initialize variable for setup dialog
        setupDialog = SetupDialog.newInstance(this, CalculateMaxDisplayableTable());

        // do a significance analysis
        RefreshSignificanceAnalysis();
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
            setupDialog.show(rowNames, colNames, values, getSupportFragmentManager());
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Generates a Table of provided dimensions with placeholder row and column names. For layout previewing purposes during app development phase.
     * @param rows How many rows the Table will have.
     * @param columns How many columns the Table will have.
     */
    @Deprecated
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
     * Changes theTable into a Table with the minimum dimensions allowed (2x2) by reformatting the variables that hold theTable's data (the high-priority variables: rowNames, colNames, values).<br>
     * The subsequently executed Refresh() -method takes care of inflating the new Table layout onto the screen.
     *
     * @param row1Name The first row's name, provided as a string.
     * @param row2Name The second row's name, provided as a string.
     * @param col1Name The first column's name, provided as a string.
     * @param col2Name The second column's name, provided as a string.
     */
    @Deprecated
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
    }

    /**
     * Changes theTable into a Table with implicit dimensions (implied by the sizes of the lists provided as parameters).<br>
     * It reformats the variables that hold theTable's data (the high-priority variables: rowNames, colNames, values).<br>
     * The subsequently executed Refresh() -method takes care of inflating the new Table layout onto the screen.
     * @param rowNames The names of the rows as strings.
     * @param colNames The names of the columns as strings.
     */
    @Deprecated
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
    }

    /**
     * Calculates the maximum supported Table size based on the screen dimension of current device.
     * @return Decimal integer array where index 0 tells maximum supported rows and index 1 tells maximum supported columns.
     */
    public int[] CalculateMaxDisplayableTable() {
        float[] screenDimensions = GetScreenDimensions();
        int rowsThatFit = (int) ((screenDimensions[1] - getResources().getDimension(R.dimen.table_height_other_with_padding)) / getResources().getDimension(R.dimen.table_row_height));
        int colsThatFit = (int) ((screenDimensions[0] - getResources().getDimension(R.dimen.table_width_other_with_padding)) / getResources().getDimension(R.dimen.table_column_width));
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
     * This method updates the chi-squared-test information by supplying the Table object with the latest data.
     */
    private void RefreshSignificanceAnalysis() {
        if (rowNames.isEmpty() || colNames.isEmpty() || values.isEmpty()) {
            significanceText.setVisibility(View.GONE);
        } else {
            if (significanceText.getVisibility() == View.GONE) significanceText.setVisibility(View.VISIBLE);
            theTable = new Table(valuesProjectionGenerator(values), rowNames.size(), colNames.size());
            significanceText.setText(getString(R.string.significance_text, theTable.getSignificance() * 100));
        }
    }

    public void SetupTable(List<String> rowNames, List<String> colNames, List<List<Integer>> values) {
        if (this.rowNames != null) this.rowNames.clear();
        else this.rowNames = new ArrayList<>();
        if (this.colNames != null) this.colNames.clear();
        else this.colNames = new ArrayList<>();
        if (this.values != null) this.values.clear();
        else this.values = new ArrayList<>();

        this.rowNames.addAll(rowNames);
        this.colNames.addAll(colNames);
        this.values.addAll(values);

        RefreshSignificanceAnalysis();
        tableLayoutFragment.notifyDataSetsChanged(rowNames, colNames, values, theTable.getRowSums(), theTable.getColSums());
    }

    public void OnValueChanged(int value, int rowIndex, int colIndex, boolean showBreadcrumb) {
        int oldValue = values.get(rowIndex).set(colIndex, value);
        RefreshSignificanceAnalysis();
        tableLayoutFragment.notifyItemChanged(rowIndex, colIndex, theTable.getRowSums().get(rowIndex), theTable.getColSums().get(colIndex));
        if (showBreadcrumb) {
            CharSequence text = Html.fromHtml(getString(R.string.toast_message, value - oldValue, rowNames.get(rowIndex), colNames.get(colIndex)));
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
    }

    public void ShowSpecifyValueDialog(final String value, final int rowIndex, final int colIndex) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        final View dialogLayout = layoutInflater.inflate(R.layout.dialog_specify_value, null, false);
        final TextView snt = dialogLayout.findViewById(R.id.specify_value_title);
        final CustomEditText cet = dialogLayout.findViewById(R.id.specify_value_input_field);

        snt.setText(getString(R.string.dialog_specify_value_title, rowNames.get(rowIndex), colNames.get(colIndex)));
        cet.setText(value);
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
        final InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        cet.requestFocus();

        dialogLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    OnValueChanged(Integer.parseInt(cet.getText().toString()), rowIndex, colIndex, false);
                    dialog.dismiss();
                }
            }
        });
    }
}
