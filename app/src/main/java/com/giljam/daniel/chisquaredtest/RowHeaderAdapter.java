package com.giljam.daniel.chisquaredtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RowHeaderAdapter extends RecyclerView.Adapter<RowHeaderAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.row_header_item_text);
        }
    }

    private Context context;
    private List<String> rowHeaders;

    public RowHeaderAdapter(Context context, List<String> rowHeaders) {
        this.context = context;
        this.rowHeaders = rowHeaders;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public RowHeaderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowHeaderCell = inflater.inflate(R.layout.row_header_item, parent, false);
        return new ViewHolder(rowHeaderCell);
    }

    @Override
    public void onBindViewHolder(RowHeaderAdapter.ViewHolder viewHolder, int position) {
        viewHolder.text.setText(rowHeaders.get(position));
    }

    @Override
    public int getItemCount() {
        return rowHeaders.size();
    }
}
