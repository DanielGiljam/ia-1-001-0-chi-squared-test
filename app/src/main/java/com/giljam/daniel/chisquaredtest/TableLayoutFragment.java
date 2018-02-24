package com.giljam.daniel.chisquaredtest;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
     * Responsible for timing the distribution of table cell dimensions to the different parts of the table.
     */
    private boolean colHeadersIsGo;

    /**
     * Responsible for timing the distribution of table cell dimensions to the different parts of the table.
     */
    private boolean rowHeadersIsGo;

    /**
     * Responsible for timing the distribution of table cell dimensions to the different parts of the table.
     */
    private boolean tableLayoutIsGo;

    /**
     * Responsible for timing the distribution of table cell dimensions to the different parts of the table.
     */
    private boolean colSumIsGo;

    /**
     * Responsible for timing the distribution of table cell dimensions to the different parts of the table.
     */
    private boolean rowSumIsGo;

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
        View view = inflater.inflate(R.layout.fragment_table_layout, container, false);

        // assigning "bridges" to corresponding variable for each significant view in this fragments layout
        colHeaders = view.findViewById(R.id.col_headers);
        rowHeaders = view.findViewById(R.id.row_headers);
        tableLayout = view.findViewById(R.id.table_layout);
        colSumRow = view.findViewById(R.id.col_sums);
        rowSumCol = view.findViewById(R.id.row_sums);
        noTableText = view.findViewById(R.id.no_table_text);

        // initialize the adapters of the RecyclerViews
        colHeaderAdapter = new ColumnHeaderAdapter(getContext(), colNames);
        rowHeaderAdapter = new RowHeaderAdapter(getContext(), rowNames);
        rowAdapter = new RowAdapter(getContext(), values);
        colSumAdapter = new ColumnSumAdapter(getContext(), colSums);
        rowSumAdapter = new RowSumAdapter(getContext(), rowSums);

        // set layout managers for the RecyclerViews
        colHeaders.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        rowHeaders.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
        tableLayout.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
        colSumRow.setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        rowSumCol.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));

        // set adapters for corresponding RecyclerViews
        colHeaders.setAdapter(colHeaderAdapter);
        rowHeaders.setAdapter(rowHeaderAdapter);
        tableLayout.setAdapter(rowAdapter);
        colSumRow.setAdapter(colSumAdapter);
        rowSumCol.setAdapter(rowSumAdapter);

        getReadyToDistributeTableCellDimensions();

        // if the variables – that store this fragment's copy of the table data – are empty, then table won't be shown
        if (rowNames.isEmpty() || colNames.isEmpty() || values.isEmpty()) changeTableVisibility(false);
        
        return view;
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

    private void getReadyToDistributeTableCellDimensions() {
        colHeaders.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                System.out.println("[DEBUG]: view ATTACHED to colHeaders.");
                colHeadersIsGo = true;
                distributeTableCellDimensions();
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                System.out.println("[DEBUG]: view DETACHED from colHeaders.");
            }
        });
        rowHeaders.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                System.out.println("[DEBUG]: view ATTACHED to rowHeaders.");
                rowHeadersIsGo = true;
                distributeTableCellDimensions();
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                System.out.println("[DEBUG]: view DETACHED from rowHeaders.");
            }
        });
        tableLayout.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                System.out.println("[DEBUG]: view ATTACHED to tableLayout.");
                tableLayoutIsGo = true;
                distributeTableCellDimensions();
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                System.out.println("[DEBUG]: view DETACHED from tableLayout.");
            }
        });
        colSumRow.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                System.out.println("[DEBUG]: view ATTACHED to colSumRow.");
                colSumIsGo = true;
                distributeTableCellDimensions();
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                System.out.println("[DEBUG]: view DETACHED from colSumRow.");
            }
        });
        rowSumCol.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                System.out.println("[DEBUG]: view ATTACHED to rowSumCol.");
                rowSumIsGo = true;
                distributeTableCellDimensions();
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                System.out.println("[DEBUG]: view DETACHED from rowSumCol.");
            }
        });
    }

    private void distributeTableCellDimensions() {
        if (colHeadersIsGo && rowHeadersIsGo && tableLayoutIsGo && colSumIsGo && rowSumIsGo) {
            int cellButtonWidth = rowAdapter.getCellButtonWidth();
            int cellButtonHeight = rowAdapter.getCellButtonHeight();
            int colHeaderWidth = colHeaderAdapter.getColHeaderWidth();
            int rowHeaderHeight = rowHeaderAdapter.getRowHeaderHeight();
            if (colHeaderWidth > cellButtonWidth) {

            } else {

            }
            if (rowHeaderHeight > cellButtonHeight) {

            } else {

            }
            colHeadersIsGo = false;
            rowHeadersIsGo = false;
            tableLayoutIsGo = false;
            colSumIsGo = false;
            rowSumIsGo = false;
            System.out.println("[DEBUG]: attempted distributing table cell dimensions.");
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

        // notifying all adapters that the data set has changed
        colHeaderAdapter.notifyDataSetChanged();
        rowHeaderAdapter.notifyDataSetChanged();
        rowAdapter.notifyDataSetChanged();
        colSumAdapter.notifyDataSetChanged();
        rowSumAdapter.notifyDataSetChanged();

        getReadyToDistributeTableCellDimensions();

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
