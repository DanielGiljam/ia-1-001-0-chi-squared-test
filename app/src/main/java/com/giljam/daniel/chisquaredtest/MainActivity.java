package com.giljam.daniel.chisquaredtest;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TableLayoutFragment.OnFragmentInteractionListener {

    /**
     * The width of the border columns in the table layout.
     * Needed for final view size calculations.
     * Can always be expected to be constant.
     */
    public static final int TABLE_WIDTH_OTHER = 96 + 48;

    /**
     * The height of the border rows in the table layout.
     * Needed for final view size calculations.
     * Can always be expected to be constant.
     */
    public static final int TABLE_HEIGHT_OTHER = 32 + 32;

    /**
     * The width of a column in the table layout.
     * Needed for final view size calculations.
     * Can always be expected to be constant.
     */
    public static final int TABLE_COLUMN_WIDTH = 96;

    /**
     * The height of a row in the table layout.
     * Needed for final view size calculations.
     * Can always be expected to be constant.
     */
    public static final int TABLE_ROW_HEIGHT = 64;

    /**
     * This variable is required for performing chi-squared-tests.<br>
     * It consists of the table data provided by the other high-priority variables, represented by a specialized table object model.
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // default onCreate() code lines
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize high-priority variables
        theTable = null;
        rowNames = null;
        colNames = null;
        values = null;

        // initialize UI-related variables
        tableLayoutFragment = (TableLayoutFragment) getFragmentManager().findFragmentById(R.id.table_layout_fragment);

        // set up managers and adapters
        // code comes here...

        // set main listeners
        // code comes here...

        // set utility listeners
        // code comes here...

        // other
        CreateMinimumTable("Row 1", "Row 2", "Column 1", "Column 2");
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

    public int[] CalculateMaxDisplayableTable() {
        float[] screenDimensions = GetScreenDimensionsInDP();
        int rowsThatFit = (int) Math.floor((screenDimensions[1] - TABLE_HEIGHT_OTHER) / TABLE_ROW_HEIGHT);
        System.out.println("[DEBUG] rows that fit: " + rowsThatFit);
        int colsThatFit = (int) Math.floor((screenDimensions[0] - TABLE_WIDTH_OTHER) / TABLE_COLUMN_WIDTH);
        System.out.println("[DEBUG] columns that fit: " + colsThatFit);
        return new int[] {rowsThatFit, colsThatFit};
    }

    /**
     * Fetches the multiplier needed to convert px to dp.
     *
     * @return the multiplier as a float.
     */
    public float GetDPMultiplier() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float dpMultiplier = getResources().getDisplayMetrics().density;
        System.out.println("[DEBUG] screen density: " + dpMultiplier);

        return dpMultiplier;
    }

    /**
     * Fetches the dimensions of the available viewport.
     * @return two floats packed in an array, representing x and y dimensions in dp.
     */
    private float[] GetScreenDimensionsInDP() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = getResources().getDisplayMetrics().density;
        System.out.println("[DEBUG] screen density: " + density);
        float dpHeight = outMetrics.heightPixels / density;
        System.out.println("[DEBUG] screen height: " + outMetrics.heightPixels);
        float dpWidth = outMetrics.widthPixels / density;
        System.out.println("[DEBUG] screen width: " + outMetrics.widthPixels);

        return new float[] {dpWidth, dpHeight};
    }

    /**
     * Should be triggered each time a modification has occured in any of the high-priority variables.<br>
     * This method updates the chi-squared-test information by first supplying the specialized table object with the latest data (as it's needed for the chi-squared-test).<br>
     * Then it updates the table layout shown on the screen by notifying all adapters about data change and, if necessary, taking care of any inflation/deflation of the table layout.
     */
    private void Refresh() {
        tableLayoutFragment.notifyDataSetsChanged(rowNames, colNames, values);
    }

    /**
     * Required for communication between {@link TableLayoutFragment} and this activity (the {@link MainActivity}).
     * @param uri Hasn't been programmed yet...
     */
    public void onFragmentInteraction(Uri uri) {
        // Code comes here (maybe?)...
    }
}
