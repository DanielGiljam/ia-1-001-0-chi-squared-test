package com.giljam.daniel.chisquaredtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class RowSumAdapter extends RecyclerView.Adapter<RowSumAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View exampleView;

        public ViewHolder(View itemView) {
            super(itemView);
            // exampleView = itemView.findViewById();
        }
    }

    private Context context;
    private List<Integer> rowSums;

    public RowSumAdapter(Context context, List<Integer> rowSums) {
        this.context = context;
        this.rowSums = rowSums;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public RowSumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowSumCell = inflater.inflate(R.layout.row_sum_item, parent, false);
        return new ViewHolder(rowSumCell);
    }

    @Override
    public void onBindViewHolder(RowSumAdapter.ViewHolder viewHolder, int position) {
        // String rowSum = rowSums.get(position);
        // View exampleView = viewHolder.exampleView;
        // exampleView.doSomething(rowSum);
    }

    @Override
    public int getItemCount() {
        return rowSums.size();
    }
}
