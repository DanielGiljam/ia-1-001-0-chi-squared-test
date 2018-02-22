package com.giljam.daniel.chisquaredtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class ColumnHeaderAdapter extends RecyclerView.Adapter<ColumnHeaderAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.col_header_item_text);
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
        viewHolder.text.setText(colHeaders.get(position));
    }

    @Override
    public int getItemCount() {
        return colHeaders.size();
    }
}
