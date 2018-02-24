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
            text.post(new Runnable() {
                @Override
                public void run() {
                    width = text.getWidth();
                    height = text.getHeight();
                    System.out.println("[DEBUG]: (from post) rowHeader " + debugIterator + " text dimensions " + width + ", " + height);
                    debugIterator++;
                }
            });
        }
    }

    private Context context;
    private List<String> rowHeaders;

    private int width = 0;
    private int height = 0;

    private boolean adaptWidth = false;
    private boolean adaptHeight = false;

    private int debugIterator = 0;

    public RowHeaderAdapter(Context context, List<String> rowHeaders) {
        this.context = context;
        this.rowHeaders = rowHeaders;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(boolean adapt, int width) {
        adaptWidth = adapt;
        this.width = width;
    }

    public void setHeight(boolean adapt, int height) {
        adaptHeight = adapt;
        this.height = height;
    }

    public void resetWidth() {
        adaptWidth = false;
        width = 0;
    }

    public void resetHeight() {
        adaptHeight = false;
        height = 0;
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
        if (adaptWidth) viewHolder.text.setWidth(width);
        if (adaptHeight) viewHolder.text.setHeight(height);
    }

    @Override
    public int getItemCount() {
        return rowHeaders.size();
    }
}
