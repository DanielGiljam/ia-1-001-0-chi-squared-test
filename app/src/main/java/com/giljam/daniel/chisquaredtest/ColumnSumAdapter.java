package com.giljam.daniel.chisquaredtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ColumnSumAdapter extends RecyclerView.Adapter<ColumnSumAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View exampleView;

        public ViewHolder(View itemView) {
            super(itemView);
            // exampleView = itemView.findViewById();
        }
    }

    private Context context;
    private List<Integer> colSums;

    public ColumnSumAdapter(Context context, List<Integer> colSums) {
        this.context = context;
        this.colSums = colSums;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public ColumnSumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View colSumCell = inflater.inflate(R.layout.col_sum_item, parent, false);
        return new ViewHolder(colSumCell);
    }

    @Override
    public void onBindViewHolder(ColumnSumAdapter.ViewHolder viewHolder, int position) {
        // String colSum = colSums.get(position);
        // View exampleView = viewHolder.exampleView;
        // exampleView.doSomething(colSum);
    }

    @Override
    public int getItemCount() {
        return colSums.size();
    }
}
