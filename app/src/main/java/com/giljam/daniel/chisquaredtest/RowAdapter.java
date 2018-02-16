package com.giljam.daniel.chisquaredtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

// TODO: copy this class twice and make a separate specific row name adapter and a row sum adapter
public class RowAdapter extends RecyclerView.Adapter<RowAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View exampleView;

        public ViewHolder(View itemView) {
            super(itemView);
            // exampleView = itemView.findViewById();
        }
    }

    private Context context;
    private List<String> rowNames;
    private CellAdapter cellAdapter;

    public RowAdapter(Context context, List<String> rowNames, CellAdapter cellAdapter) {
        this.context = context;
        this.rowNames = rowNames;
        this.cellAdapter = cellAdapter;
    }

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
        // String rowName = rowNames.get(position);
        // View exampleView = viewHolder.exampleView;
        // exampleView.doSomething(rowName);
    }

    @Override
    public int getItemCount() {
        return rowNames.size();
    }
}
