package com.giljam.daniel.chisquaredtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ColumnSumAdapter extends RecyclerView.Adapter<ColumnSumAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView sumText;

        public ViewHolder(View itemView) {
            super(itemView);
            sumText = itemView.findViewById(R.id.col_sum_item_text);
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
        viewHolder.sumText.setText(colSums.get(position));
    }

    @Override
    public int getItemCount() {
        return colSums.size();
    }
}
