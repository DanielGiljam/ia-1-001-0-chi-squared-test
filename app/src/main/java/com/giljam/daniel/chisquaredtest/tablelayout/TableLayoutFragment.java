package com.giljam.daniel.chisquaredtest.tablelayout;

import android.app.Fragment;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.giljam.daniel.chisquaredtest.R;

import java.util.ArrayList;
import java.util.List;

public class TableLayoutFragment extends Fragment {

    /**
     * The {@link TableLayoutFragment}'s copy of the row names.
     */
    private List<String> rowNames;

    /**
     * The {@link TableLayoutFragment}'s copy of the column names.
     */
    private List<String> colNames;

    /**
     * The {@link TableLayoutFragment}'s copy of the values.
     */
    private List<List<Integer>> values;

    /**
     * A {@link List} containing the sum of the values for each row.
     */
    private List<Integer> rowSums;

    /**
     * A {@link List} containing the sum of the values for each column.
     */
    private List<Integer> colSums;

    /**
     * Variable for the {@link ConstraintLayout} that is a significant "parent" layout in this fragment.
     */
    private ConstraintLayout constraintRootLayout;

    /**
     * Variable for the {@link RecyclerView} displaying the column names.
     */
    private RecyclerView colHeaders;

    /**
     * Variable for the {@link RecyclerView} displaying the row names.
     */
    private RecyclerView rowHeaders;

    /**
     * Variable for the {@link RecyclerView} that makes up the Table itself.
     */
    private RecyclerView tableLayout;

    /**
     * Variable for the {@link RecyclerView} displaying the sum of each column.
     */
    private RecyclerView colSumRow;

    /**
     * Variable for the {@link RecyclerView} displaying the sum of each row.
     */
    private RecyclerView rowSumCol;

    /**
     * Variable for the {@link TextView} that substitutes with a message when there is no Table data to draw.
     */
    private TextView noTableText;

    /**
     * Custom {@link RecyclerView.Adapter} for translating the column names data
     * so that it can be displayed by the colHeaders {@link RecyclerView}.
     */
    private ColumnHeaderAdapter colHeaderAdapter;

    /**
     * Custom {@link RecyclerView.Adapter} for translating the row names data
     * so that it can be displayed by the rowHeaders {@link RecyclerView}.
     */
    private RowHeaderAdapter rowHeaderAdapter;

    /**
     * Custom {@link RecyclerView.Adapter} for translating the values data
     * so that it can be displayed by the tableLayout {@link RecyclerView}.
     */
    private RowAdapter rowAdapter;

    /**
     * Custom {@link RecyclerView.Adapter} for feeding the sum data for each column
     * so that it can be displayed by the colSumRow {@link RecyclerView}.
     */
    private ColumnSumAdapter colSumAdapter;

    /**
     * Custom {@link RecyclerView.Adapter} for feeding the sum data for each row
     * so that it can be displayed by the rowSumCol {@link RecyclerView}.
     */
    private RowSumAdapter rowSumAdapter;

    /**
     * The initial max width of the {@link TableLayoutFragment#constraintRootLayout} is stored here after
     * {@link TableLayoutFragment#calculateTableDimensions()} is first run.<br>
     * In case the Table is ever empty, the initial max width is restored, which means the substituting
     * "Table is empty" -message is centered on the screen.
     */
    private int initialMaxWidth = 0;

    /**
     * The initial max height of the {@link TableLayoutFragment#constraintRootLayout} is stored here after
     * {@link TableLayoutFragment#calculateTableDimensions()} is first run.<br>
     * In case the Table is ever empty, the initial max height is restored, which means the substituting
     * "Table is empty" -message is centered on the screen.
     */
    private int initialMaxHeight = 0;

    /**
     * Responsible for timing the Table cell dimension distribution.
     */
    @Deprecated
    private boolean tableCellDimensionDistributionIsArmed = false;

    public TableLayoutFragment() {
        rowNames = new ArrayList<>();
        colNames = new ArrayList<>();
        values = new ArrayList<>();
        rowSums = new ArrayList<>();
        colSums = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootLayout = inflater.inflate(R.layout.fragment_table_layout, container, false);

        // assigning "bridges" to corresponding variable for each significant view in this fragments layout
        constraintRootLayout = rootLayout.findViewById(R.id.table_layout_fragment_root_layout);
        colHeaders = rootLayout.findViewById(R.id.col_headers);
        rowHeaders = rootLayout.findViewById(R.id.row_headers);
        tableLayout = rootLayout.findViewById(R.id.table_layout);
        colSumRow = rootLayout.findViewById(R.id.col_sums);
        rowSumCol = rootLayout.findViewById(R.id.row_sums);
        noTableText = rootLayout.findViewById(R.id.no_table_text);

        // Determine height of Table
        calculateTableDimensions();

        // set up item decorations
        colHeaders.addItemDecoration(new CustomDividerItemDecoration(getContext(), 0, 0));
        rowHeaders.addItemDecoration(new CustomDividerItemDecoration(getContext(), 1, 1));
        colSumRow.addItemDecoration(new CustomDividerItemDecoration(getContext(), 0, 1));
        rowSumCol.addItemDecoration(new CustomDividerItemDecoration(getContext(), 1, 0));

        // initialize the adapters of the RecyclerViews
        colHeaderAdapter = new ColumnHeaderAdapter(getContext(), colNames);
        rowHeaderAdapter = new RowHeaderAdapter(getContext(), rowNames);
        rowAdapter = new RowAdapter(getContext(), values);
        colSumAdapter = new ColumnSumAdapter(getContext(), colSums);
        rowSumAdapter = new RowSumAdapter(getContext(), rowSums);

        // set layout managers for the RecyclerViews
        // NOTE! Scrolling being disabled in these layout managers is a part of the explicit choice to not support any scrolling in this Table layout,
        // as this was way too complicated to achieve when using RecyclerViews as the layout's foundation.
        colHeaders.setLayoutManager(new LinearLayoutManager(getContext(), 0, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });
        rowHeaders.setLayoutManager(new LinearLayoutManager(getContext(), 1, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        tableLayout.setLayoutManager(new LinearLayoutManager(getContext(), 1, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        colSumRow.setLayoutManager(new LinearLayoutManager(getContext(), 0, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });
        rowSumCol.setLayoutManager(new LinearLayoutManager(getContext(), 1, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        // set adapters for corresponding RecyclerViews
        colHeaders.setAdapter(colHeaderAdapter);
        rowHeaders.setAdapter(rowHeaderAdapter);
        tableLayout.setAdapter(rowAdapter);
        colSumRow.setAdapter(colSumAdapter);
        rowSumCol.setAdapter(rowSumAdapter);

        // if the variables – that store this fragment's copy of the Table data – are empty, then Table won't be shown
        if (rowNames.isEmpty() || colNames.isEmpty() || values.isEmpty()) changeTableVisibility(false);
        
        return rootLayout;
    }

    /**
     * Optimizes the Table layout by cropping blank canvas
     * and making row height and column widths uniform.
     */
    private void calculateTableDimensions() {
        if (initialMaxWidth == 0 && initialMaxHeight == 0) {
            initialMaxWidth = constraintRootLayout.getMaxWidth();
            initialMaxHeight = constraintRootLayout.getMaxHeight();
        }
        if (rowNames.isEmpty() || colNames.isEmpty()) {
            if (initialMaxWidth == 0 && initialMaxHeight == 0) return;
            constraintRootLayout.setMinWidth(0);
            constraintRootLayout.setMaxWidth(initialMaxWidth);
            constraintRootLayout.setMinHeight(0);
            constraintRootLayout.setMaxHeight(initialMaxHeight);
            return;
        }
        int width = Math.round
        (
            (getResources().getDimension(R.dimen.table_width_other) +
            colNames.size() * getResources().getDimension(R.dimen.table_column_width))
        );
        int height = Math.round
        (
            (getResources().getDimension(R.dimen.table_height_other) +
            rowNames.size() * getResources().getDimension(R.dimen.table_row_height))
        );
        constraintRootLayout.setMinWidth(width);
        constraintRootLayout.setMaxWidth(width);
        constraintRootLayout.setMinHeight(height);
        constraintRootLayout.setMaxHeight(height);
    }

    /**
     * Hides the {@link View} objects making up the Table and shows the {@link TextView} that substitutes with a message,
     * or shows the {@link View} objects making up the Table and hides the substituting {@link TextView}.
     * @param visible If set to true, Table will be shown. If set to false, Table will be hidden.
     */
    private void changeTableVisibility(boolean visible) {
        if (visible) {
            colHeaders.setVisibility(View.VISIBLE);
            rowHeaders.setVisibility(View.VISIBLE);
            tableLayout.setVisibility(View.VISIBLE);
            colSumRow.setVisibility(View.VISIBLE);
            rowSumCol.setVisibility(View.VISIBLE);
            noTableText.setVisibility(View.GONE);
        } else {
            colHeaders.setVisibility(View.GONE);
            rowHeaders.setVisibility(View.GONE);
            tableLayout.setVisibility(View.GONE);
            colSumRow.setVisibility(View.GONE);
            rowSumCol.setVisibility(View.GONE);
            noTableText.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Notifies appropriate {@link CellAdapter} that an item in its data set was modified.
     * @param rowIndex Indicates which {@link CellAdapter}.
     * @param colIndex Indicates which item.
     */
    public void notifyItemChanged(int rowIndex, int colIndex, int newRowSum, int newColSum) {
        rowAdapter.notifyItemChanged(rowIndex, colIndex);
        rowSums.set(rowIndex, newRowSum);
        rowSumAdapter.notifyItemChanged(rowIndex);
        colSums.set(colIndex, newColSum);
        colSumAdapter.notifyItemChanged(colIndex);
    }

    /**
     * Updates the {@link TableLayoutFragment}'s copy of the data and notifies all the adapters about it.
     * @param rowNames The {@link TableLayoutFragment}'s copy of the row names.
     * @param colNames The {@link TableLayoutFragment}'s copy of the column names.
     * @param values   The {@link TableLayoutFragment}'s copy of the values.
     */
    public void notifyDataSetsChanged(List<String> rowNames, List<String> colNames, List<List<Integer>> values, List<Integer> rowSums, List<Integer> colSums) {

        // properly resetting following variables
        if (this.rowNames != null) this.rowNames.clear();
        else this.rowNames = new ArrayList<>();
        if (this.colNames != null) this.colNames.clear();
        else this.colNames = new ArrayList<>();
        if (this.values != null) this.values.clear();
        else this.values = new ArrayList<>();
        if (this.rowSums != null) this.rowSums.clear();
        else this.rowSums = new ArrayList<>();
        if (this.colSums != null) this.colSums.clear();
        else this.colSums = new ArrayList<>();

        // adding provided data to the reset variables
        this.rowNames.addAll(rowNames);
        this.colNames.addAll(colNames);
        this.values.addAll(values);
        this.rowSums.addAll(rowSums);
        this.colSums.addAll(colSums);

        // determine new height of Table
        calculateTableDimensions();

        // notifying all adapters that the data set has changed
        colHeaderAdapter.notifyDataSetChanged();
        rowHeaderAdapter.notifyDataSetChanged();
        rowAdapter.notifyDataSetChanged();
        colSumAdapter.notifyDataSetChanged();
        rowSumAdapter.notifyDataSetChanged();

        // toggle Table visibility depending on the state of the data set (empty or not empty)
        if (this.rowNames.isEmpty() || this.colNames.isEmpty() || this.values.isEmpty())
            changeTableVisibility(false);
        else changeTableVisibility(true);
    }

    /**
     * For, outside of this fragment, checking whether
     * the Table cell dimension distribution mechanism is armed or not.
     * @return True if the mechanism is armed, false if its not.
     */
    @Deprecated
    public boolean getTableCellDimensionDistributionIsArmed() {
        return tableCellDimensionDistributionIsArmed;
    }

    /**
     * Method that activates a rig that at the right moment
     * performs a distribution of dynamically determined Table cell dimension values.
     * The rig resets and "arms" itself when the distribution is finished,
     * allowing for it to be activated again. The method also has built-in functionality for "dodging"
     * overlapping activations.
     */
    @Deprecated
    public void distributeTableCellDimensions() {

        RecyclerView aRow = tableLayout.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.row_recycler_view);
        Button aCellButton = aRow.findViewHolderForAdapterPosition(0).itemView.findViewById(R.id.cell_button);

        // getting the cell button dimensions
        int cellButtonWidth = aCellButton.getWidth();
        int cellButtonHeight = aCellButton.getHeight();

        // getting the relevant header dimensions
        int colHeaderWidth = 0;
        int rowHeaderHeight = 0;

        for (int i = 0; i < colNames.size(); i++) {
            TextView colHeaderText = colHeaders.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.col_header_item_text);
            if (colHeaderText.getWidth() > colHeaderWidth) colHeaderWidth = colHeaderText.getWidth();
        }

        for (int i = 0; i < rowNames.size(); i++) {
            TextView rowHeaderText = rowHeaders.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.row_header_item_text);
            if (rowHeaderText.getWidth() > rowHeaderHeight) rowHeaderHeight = rowHeaderText.getWidth();
        }

        // checking whether the button's width will adapt to the header's width or vice versa
        // if (colHeaderWidth > cellButtonWidth) {
            // rowAdapter.setCellButtonWidth(true, colHeaderWidth);
            // colSumAdapter.setWidth(true, colHeaderWidth);
        // } else {
            // colHeaderAdapter.setWidth(true, cellButtonWidth);
            // colSumAdapter.setWidth(true, cellButtonWidth);
        // }

        // checking whether the button's height will adapt to the header's height or vice versa
        // if (rowHeaderHeight > cellButtonHeight) {
            // rowAdapter.setCellButtonHeight(true, rowHeaderHeight);
            // rowSumAdapter.setHeight(true, rowHeaderHeight);
        // } else {
            // rowHeaderAdapter.setHeight(true, cellButtonHeight);
            // rowSumAdapter.setHeight(true, cellButtonHeight);
        // }

        // check off all involved View's as invalid to have them redrawn with the distributed dimensions
        colHeaders.invalidate();
        rowHeaders.invalidate();
        tableLayout.invalidate();
        colSumRow.invalidate();
        rowSumCol.invalidate();

        // the Table cell dimension distribution mechanism is reloaded again (to allow being armed again)
        tableCellDimensionDistributionIsArmed = false;
    }
}
