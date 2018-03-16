package com.giljam.daniel.chisquaredtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

public class SetupListAdapter extends RecyclerView.Adapter<SetupListAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {

        EditText listItemInputField;

        ViewHolder(View itemView) {
            super(itemView);
            listItemInputField = itemView.findViewById(R.id.list_item_input_field);
        }
    }

    interface AdapterListener {
        void MonitorInputField(EditText listItemInputField, final int position, final int rowOrColList);
    }

    private AdapterListener adapterListener;

    private int rowOrColList;

    private List<String> listItems;

    SetupListAdapter(SetUpDialog dialog, int rowOrColList, List<String> listItems) {
        adapterListener = dialog;
        this.rowOrColList = rowOrColList;
        this.listItems = listItems;
    }

    @Override
    public SetupListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View listItemView = inflater.inflate(R.layout.setup_list_item, parent, false);
        return new ViewHolder(listItemView);
    }

    @Override
    public void onBindViewHolder(SetupListAdapter.ViewHolder viewHolder, int position) {
        viewHolder.listItemInputField.setText(listItems.get(position));
        adapterListener.MonitorInputField(viewHolder.listItemInputField, position, rowOrColList);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
