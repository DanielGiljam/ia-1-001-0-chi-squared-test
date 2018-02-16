package com.giljam.daniel.chisquaredtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ColumnHeaderAdapter extends RecyclerView.Adapter<ColumnHeaderAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View exampleView;

        public ViewHolder(View itemView) {
            super(itemView);
            // exampleView = itemView.findViewById();
        }
    }

    private Context context;
    private List<String> colHeaders;

    public ColumnHeaderAdapter(Context context, List<String> colHeaders) {
        this.context = context;
        this.colHeaders = colHeaders;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public ColumnHeaderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View colHeaderCell = inflater.inflate(R.layout.col_header_item, parent, false);
        return new ViewHolder(colHeaderCell);
    }

    @Override
    public void onBindViewHolder(ColumnHeaderAdapter.ViewHolder viewHolder, int position) {
        // String colHeader = colHeaders.get(position);
        // View exampleView = viewHolder.exampleView;
        // exampleView.doSomething(colHeader);
    }

    @Override
    public int getItemCount() {
        return colHeaders.size();
    }
}
