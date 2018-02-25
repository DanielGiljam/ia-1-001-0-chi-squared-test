package com.giljam.daniel.chisquaredtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

public class CellAdapter extends RecyclerView.Adapter<CellAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Button button;

        public ViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.cell_button);
            /*button.post(new Runnable() {
                @Override
                public void run() {
                    if (buttonWidth == 0) buttonWidth = button.getWidth();
                    if (buttonHeight == 0) buttonHeight = button.getHeight();
                    System.out.println("[DEBUG]: (from post) cellButton " + debugIterator + " dimensions " + buttonWidth + ", " + buttonHeight);
                    debugIterator++;
                }
            });*/
        }
    }

    private Context context;
    private List<Integer> cellValues;

    /*private int buttonWidth = 0;
    private int buttonHeight = 0;

    private boolean adaptWidth = false;
    private boolean adaptHeight = false;

    private int debugIterator = 0;*/

    public CellAdapter(Context context, List<Integer> cellValues) {
        this.context = context;
        this.cellValues = cellValues;
    }

    /*public int getButtonWidth() {
        return buttonWidth;
    }

    public int getButtonHeight() {
        return buttonHeight;
    }

    public void setButtonWidth(boolean adapt, int width) {
        adaptWidth = adapt;
        buttonWidth = width;
    }

    public void setButtonHeight(boolean adapt, int height) {
        adaptHeight = adapt;
        buttonHeight = height;
    }*/

    private Context getContext() {
        return context;
    }

    @Override
    public CellAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View cell = inflater.inflate(R.layout.cell_item, parent, false);
        return new ViewHolder(cell);
    }

    @Override
    public void onBindViewHolder(CellAdapter.ViewHolder viewHolder, int position) {
        Button button = viewHolder.button;
        CharSequence text = Html.fromHtml(getContext().getString(R.string.cell_button_text, cellValues.get(position)));
        button.setText(text);
        /*if (adaptWidth) button.setWidth(buttonWidth);
        if (adaptHeight) button.setHeight(buttonHeight);*/
    }

    @Override
    public int getItemCount() {
        return cellValues.size();
    }
}
