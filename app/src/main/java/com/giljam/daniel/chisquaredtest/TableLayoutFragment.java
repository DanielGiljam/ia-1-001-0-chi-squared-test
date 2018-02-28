package com.giljam.daniel.chisquaredtest;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.<br>
 * In this case, cares for the rendering of an interactive table onto the screen.<br>
 * Activities that contain this fragment must implement the
 * {@link TableLayoutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.<br>
 * Use the {@link TableLayoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
     * Variable for the {@link View} that is the root layout of this fragment.
     */
    private View rootLayout;

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
     * Variable for the {@link RecyclerView} that makes up the table itself.
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
     * Variable for the {@link TextView} that substitutes with a message when there is no table data to draw.
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
     * Responsible for timing the table cell dimension distribution.
     */
    @Deprecated
    private boolean tableCellDimensionDistributionIsArmed = false;

    /*The fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";*/

    /*private String mParam1;
    private String mParam2;*/

    private OnFragmentInteractionListener mListener;

    public TableLayoutFragment() {
        rowNames = new ArrayList<>();
        colNames = new ArrayList<>();
        values = new ArrayList<>();
        rowSums = new ArrayList<>();
        colSums = new ArrayList<>();
    }

    /**
     * Use this factory method to create an instance of this fragment.
     * @return A new instance of fragment TableLayoutFragment.
     */
    public static TableLayoutFragment newInstance(  List<String> rowNames,
                                                    List<String> colNames,
                                                    List<List<Integer>> values) {

        // Instantiate this fragment
        TableLayoutFragment fragment = new TableLayoutFragment();

        // Pass provided parameter values (if they exist) to this instance of the fragment
        if (rowNames == null || colNames == null || values == null) {
            fragment.rowNames = new ArrayList<>();
            fragment.colNames = new ArrayList<>();
            fragment.values = new ArrayList<>();
            fragment.rowSums = new ArrayList<>();
            fragment.colSums = new ArrayList<>();
        } else {
            fragment.rowNames = rowNames;
            fragment.colNames = colNames;
            fragment.values = values;
            fragment.calculateSums();
        }

        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(   LayoutInflater inflater,
                                ViewGroup container,
                                Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootLayout = inflater.inflate(R.layout.fragment_table_layout, container, false);

        // assigning "bridges" to corresponding variable for each significant view in this fragments layout
        constraintRootLayout = rootLayout.findViewById(R.id.table_layout_fragment_root_layout);
        colHeaders = rootLayout.findViewById(R.id.col_headers);
        rowHeaders = rootLayout.findViewById(R.id.row_headers);
        tableLayout = rootLayout.findViewById(R.id.table_layout);
        colSumRow = rootLayout.findViewById(R.id.col_sums);
        rowSumCol = rootLayout.findViewById(R.id.row_sums);
        noTableText = rootLayout.findViewById(R.id.no_table_text);

        // Determine height of table
        CalculateTableDimensions();

        // create divider item decorations
        CustomDividerItemDecoration verticalDivider = new CustomDividerItemDecoration(getContext(), 0);
        CustomDividerItemDecoration horizontalDivider = new CustomDividerItemDecoration(getContext(), 1);

        // set up dividers
        colHeaders.addItemDecoration(verticalDivider);
        rowHeaders.addItemDecoration(horizontalDivider);
        colSumRow.addItemDecoration(verticalDivider);
        rowSumCol.addItemDecoration(horizontalDivider);

        // initialize the adapters of the RecyclerViews
        colHeaderAdapter = new ColumnHeaderAdapter(getContext(), colNames);
        rowHeaderAdapter = new RowHeaderAdapter(getContext(), rowNames);
        rowAdapter = new RowAdapter(getContext(), values);
        colSumAdapter = new ColumnSumAdapter(getContext(), colSums);
        rowSumAdapter = new RowSumAdapter(getContext(), rowSums);

        // set layout managers for the RecyclerViews
        // NOTE! Scrolling being disabled in these layout managers is a part of the explicit choice to not support any scrolling in this table layout,
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

        // if the variables – that store this fragment's copy of the table data – are empty, then table won't be shown
        if (rowNames.isEmpty() || colNames.isEmpty() || values.isEmpty()) changeTableVisibility(false);
        
        return rootLayout;
    }

    private void CalculateTableDimensions() {
        int width = Math.round
        (
            (MainActivity.TABLE_WIDTH_OTHER +
            colNames.size() * MainActivity.TABLE_COLUMN_WIDTH) *
            ((MainActivity)getActivity()).GetDPMultiplier()
        );
        int height = Math.round
        (
            (MainActivity.TABLE_HEIGHT_OTHER +
            rowNames.size() * MainActivity.TABLE_ROW_HEIGHT) *
            ((MainActivity) getActivity()).GetDPMultiplier()
        );
        System.out.println("[DEBUG] constraint root layout min width: " + constraintRootLayout.getMinWidth());
        System.out.println("[DEBUG] constraint root layout max width: " + constraintRootLayout.getMaxWidth());
        System.out.println("[DEBUG] constraint root layout min height: " + constraintRootLayout.getMinHeight());
        System.out.println("[DEBUG] constraint root layout max height: " + constraintRootLayout.getMaxHeight());
        constraintRootLayout.setMinWidth(width);
        constraintRootLayout.setMaxWidth(width);
        constraintRootLayout.setMinHeight(height);
        constraintRootLayout.setMaxHeight(height);
    }

    /**
     * Hides the {@link View} objects making up the table and shows the {@link TextView} that substitutes with a message,
     * or shows the {@link View} objects making up the table and hides the substituting {@link TextView}.
     * @param visible If set to true, table will be shown. If set to false, table will be hidden.
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
     * Function that populates the {@link TableLayoutFragment#colSums} and {@link TableLayoutFragment#rowSums} {@link List}s
     * with suitable values calculated based on the {@link TableLayoutFragment#values} variable. Consequentially a pre-condition for
     * this function to work, is that the {@link TableLayoutFragment#values} variable already is populated with suitable data.
     * This function should be executed every time after the {@link TableLayoutFragment#values} variable has been re-assigned/updated,
     * so that the sum-values stay up to date.
     */
    // TODO: Implement better sum calculation!
    private void calculateSums() {

        // properly resetting following variables
        if (rowSums != null) rowSums.clear();
        else rowSums = new ArrayList<>();
        if (colSums != null) colSums.clear();
        else colSums = new ArrayList<>();

        for (List<Integer> cellValues : values) rowSums.add(0);

        for (int columnValue : values.get(0)) colSums.add(0);
    }

    /**
     * For, outside of this fragment, checking whether
     * the table cell dimension distribution mechanism is armed or not.
     * @return True if the mechanism is armed, false if its not.
     */
    @Deprecated
    public boolean getTableCellDimensionDistributionIsArmed() {
        return tableCellDimensionDistributionIsArmed;
    }

    /**
     * Method that activates a rig that at the right moment
     * performs a distribution of dynamically determined table cell dimension values.
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

        // the table cell dimension distribution mechanism is reloaded again (to allow being armed again)
        tableCellDimensionDistributionIsArmed = false;
    }

    /**
     * Updates the {@link TableLayoutFragment}'s copy of the data and notifies all the adapters about it.
     * @param rowNames The {@link TableLayoutFragment}'s copy of the row names.
     * @param colNames The {@link TableLayoutFragment}'s copy of the column names.
     * @param values The {@link TableLayoutFragment}'s copy of the values.
     */
    public void notifyDataSetsChanged(List<String> rowNames,
                                      List<String> colNames,
                                      List<List<Integer>> values) {

        // properly resetting following variables
        if (this.rowNames != null) this.rowNames.clear();
        else this.rowNames = new ArrayList<>();
        if (this.colNames != null) this.colNames.clear();
        else this.colNames = new ArrayList<>();
        if (this.values != null) this.values.clear();
        else this.values = new ArrayList<>();

        // adding provided data to the reset variables
        this.rowNames.addAll(rowNames);
        this.colNames.addAll(colNames);
        this.values.addAll(values);
        calculateSums();

        // determine new height of table
        CalculateTableDimensions();

        // notifying all adapters that the data set has changed
        colHeaderAdapter.notifyDataSetChanged();
        rowHeaderAdapter.notifyDataSetChanged();
        rowAdapter.notifyDataSetChanged();
        colSumAdapter.notifyDataSetChanged();
        rowSumAdapter.notifyDataSetChanged();

        // toggle table visibility depending on the state of the data set (empty or not empty)
        if (this.rowNames.isEmpty() || this.colNames.isEmpty() || this.values.isEmpty()) changeTableVisibility(false);
        else changeTableVisibility(true);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) mListener = (OnFragmentInteractionListener) context;
        else throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
