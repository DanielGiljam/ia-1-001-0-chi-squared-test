package com.giljam.daniel.chisquaredtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RowAdapter extends RecyclerView.Adapter<RowAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View exampleView;

        public ViewHolder(View itemView) {
            super(itemView);
            // exampleView = itemView.findViewById();
        }
    }

    private Context context;
    private List<List<Integer>> values;

    public RowAdapter(Context context, List<List<Integer>> values) {
        this.context = context;
        this.values = values;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public RowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.row_item, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(RowAdapter.ViewHolder viewHolder, int position) {
        // List<Integer> cellValues = values.get(position);
        // View exampleView = viewHolder.exampleView;
        // exampleView.doSomething(cellValues);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}
