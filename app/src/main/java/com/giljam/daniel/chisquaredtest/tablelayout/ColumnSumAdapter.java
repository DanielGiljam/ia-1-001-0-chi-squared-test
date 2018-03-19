package com.giljam.daniel.chisquaredtest.tablelayout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.giljam.daniel.chisquaredtest.R;

import java.util.List;

/**
 * @author Daniel Giljam
 * @since 2018-02-16
 */
public class ColumnSumAdapter extends RecyclerView.Adapter<ColumnSumAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.col_sum_item_text);
            /*text.post(new Runnable() {
                @Override
                public void run() {
                    width = text.getWidth();
                    height = text.getHeight();
                    debugIterator++;
                }
            });*/
        }
    }

    private Context context;
    private List<Integer> colSums;

    /*private int width = 0;
    private int height = 0;

    private boolean adaptWidth = false;
    private boolean adaptHeight = false;

    private int debugIterator = 0;*/

    public ColumnSumAdapter(Context context, List<Integer> colSums) {
        this.context = context;
        this.colSums = colSums;
    }

    /*public void setWidth(boolean adapt, int width) {
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
    }*/

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
        viewHolder.text.setText(getContext().getString(R.string.sum_item_text, colSums.get(position)));
        /*if (adaptWidth) viewHolder.text.setWidth(width);
        if (adaptHeight) viewHolder.text.setHeight(height);*/
    }

    @Override
    public int getItemCount() {
        return colSums.size();
    }
}
