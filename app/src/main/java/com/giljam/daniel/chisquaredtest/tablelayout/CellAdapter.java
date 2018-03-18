package com.giljam.daniel.chisquaredtest.tablelayout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.giljam.daniel.chisquaredtest.MainActivity;
import com.giljam.daniel.chisquaredtest.R;

import java.util.List;

public class CellAdapter extends RecyclerView.Adapter<CellAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {

        Button button;

        ViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.cell_button);
            /*button.post(new Runnable() {
                @Override
                public void run() {
                    if (buttonWidth == 0) buttonWidth = button.getWidth();
                    if (buttonHeight == 0) buttonHeight = button.getHeight();
                    debugIterator++;
                }
            });*/
        }
    }

    public interface ValueListener {
        void OnValueChanged(int value, int rowIndex, int colIndex, boolean showBreadcrumb);
        void ShowSpecifyValueDialog(final String value, final int rowIndex, final int colIndex);
    }

    private ValueListener valueListener;

    private Context context;

    private int rowIndex;

    private List<Integer> cellValues;

    /*private int buttonWidth = 0;
    private int buttonHeight = 0;

    private boolean adaptWidth = false;
    private boolean adaptHeight = false;

    private int debugIterator = 0;*/

    CellAdapter(Context context, int rowIndex, List<Integer> cellValues) {
        valueListener = ((MainActivity)context);
        this.context = context;
        this.rowIndex = rowIndex;
        this.cellValues = cellValues;
    }

    private void MonitorButton(final Button button, final int rowIndex, final int colIndex) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueListener.OnValueChanged(cellValues.get(colIndex) + 1, rowIndex, colIndex, true);
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                valueListener.ShowSpecifyValueDialog(button.getText().toString(), rowIndex, colIndex);
                return true;
            }
        });
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
        CharSequence text = Html.fromHtml(context.getString(R.string.cell_button_text, cellValues.get(position)));
        button.setText(text);
        MonitorButton(button, rowIndex, position);
        /*if (adaptWidth) button.setWidth(buttonWidth);
        if (adaptHeight) button.setHeight(buttonHeight);*/
    }

    @Override
    public int getItemCount() {
        return cellValues.size();
    }
}
