package com.giljam.daniel.chisquaredtest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    /*The fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";*/

    /*private String mParam1;
    private String mParam2;*/

    private OnFragmentInteractionListener mListener;

    public TableLayoutFragment() {
        // Required empty public constructor
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

        // Pass provided parameter values to this instance of the fragment
        fragment.rowNames = rowNames;
        fragment.colNames = colNames;
        fragment.values = values;

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
        
        return view;
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
