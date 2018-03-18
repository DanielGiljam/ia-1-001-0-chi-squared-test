package com.giljam.daniel.chisquaredtest.setup;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.giljam.daniel.chisquaredtest.CustomEditText;
import com.giljam.daniel.chisquaredtest.R;

import java.util.List;

public class SetupListAdapter extends RecyclerView.Adapter<SetupListAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {

        CustomEditText listItemInputField;

        ViewHolder(View itemView) {
            super(itemView);
            listItemInputField = itemView.findViewById(R.id.list_item_input_field);
        }
    }

    interface AdapterListener {
        void MonitorInputField(CustomEditText listItemInputField, final int position, final int placeHolderId);
    }

    private AdapterListener adapterListener;

    private int placeHolderId;

    private List<String> listItems;

    SetupListAdapter(SetupDialog dialog, int placeHolderId, List<String> listItems) {
        adapterListener = dialog;
        this.placeHolderId = placeHolderId;
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
        adapterListener.MonitorInputField(viewHolder.listItemInputField, position, placeHolderId);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
