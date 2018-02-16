package com.giljam.daniel.chisquaredtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

// TODO: copy this class twice and make a separate specific column name adapter and a column sum adapter
public class CellAdapter extends RecyclerView.Adapter<CellAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View exampleView;

        public ViewHolder(View itemView) {
            super(itemView);
            // exampleView = itemView.findViewById();
        }
    }

    private Context context;
    private List<Integer> cellValues;

    public CellAdapter(Context context, List<Integer> cellValues) {
        this.context = context;
        this.cellValues = cellValues;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public CellAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View cellView = inflater.inflate(R.layout.cell_item, parent, false);
        return new ViewHolder(cellView);
    }

    @Override
    public void onBindViewHolder(CellAdapter.ViewHolder viewHolder, int position) {
        // int cellValue = cellValues.get(position);
        // View exampleView = viewHolder.exampleView;
        // exampleView.doSomething(cellValue);
    }

    @Override
    public int getItemCount() {
        return cellValues.size();
    }
}
