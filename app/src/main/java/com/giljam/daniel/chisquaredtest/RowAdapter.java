package com.giljam.daniel.chisquaredtest;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class RowAdapter extends RecyclerView.Adapter<RowAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RecyclerView row;

        public ViewHolder(View itemView) {
            super(itemView);
            row = itemView.findViewById(R.id.row_recycler_view);
        }
    }

    private Context context;
    private List<List<Integer>> values;
    private List<CellAdapter> cellAdapters;
    private List<RecyclerView> rows;

    public RowAdapter(Context context, List<List<Integer>> values) {
        this.context = context;
        this.values = values;
        cellAdapters = new ArrayList<>();
        rows = new ArrayList<>();
    }

    /*public int getCellButtonWidth() {
        return cellAdapters.get(0).getButtonWidth();
    }

    public int getCellButtonHeight() {
        return cellAdapters.get(0).getButtonHeight();
    }

    public void setCellButtonWidth(boolean adapt, int width) {
        for (CellAdapter cellAdapter : cellAdapters) cellAdapter.setButtonWidth(adapt, width);
    }

    public void setCellButtonHeight(boolean adapt, int height) {
        for (CellAdapter cellAdapter : cellAdapters) cellAdapter.setButtonHeight(adapt, height);
    }

    public void resetCellButtonWidth() {
        for (CellAdapter cellAdapter : cellAdapters) cellAdapter.setButtonWidth(false, 0);
    }

    public void resetCellButtonHeight() {
        for (CellAdapter cellAdapter : cellAdapters) cellAdapter.setButtonHeight(false, 0);
    }*/

    private Context getContext() {
        return context;
    }

    @Override
    public RowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.row_item, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(RowAdapter.ViewHolder viewHolder, int position) {
        CellAdapter cellAdapter = new CellAdapter(getContext(), values.get(position));
        cellAdapters.add(position, cellAdapter);
        rows.add(position, viewHolder.row);
        rows.get(position).setLayoutManager(new LinearLayoutManager(getContext(), 0, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });
        rows.get(position).setAdapter(cellAdapters.get(position));
    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}
