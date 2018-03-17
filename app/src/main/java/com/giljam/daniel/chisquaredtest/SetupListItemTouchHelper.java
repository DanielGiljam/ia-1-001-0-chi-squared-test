package com.giljam.daniel.chisquaredtest;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SetupListItemTouchHelper extends ItemTouchHelper.Callback {

    interface ItemTouchHelperAdapter {
        void MoveListItem(int startIndex, int endIndex, int placeHolderId);
        void RemoveListItem(int index, int placeHolderId);
    }

    private ItemTouchHelperAdapter ithAdapter;

    private int placeHolderId;

    private boolean itemViewSwipeEnabled = false;

    SetupListItemTouchHelper(ItemTouchHelperAdapter dialog, int placeHolderId) {
        ithAdapter = dialog;
        this.placeHolderId = placeHolderId;
    }

    void setItemViewSwipeEnabled(boolean itemViewSwipeEnabled) {
        this.itemViewSwipeEnabled = itemViewSwipeEnabled;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return itemViewSwipeEnabled;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        ithAdapter.MoveListItem(viewHolder.getAdapterPosition(), target.getAdapterPosition(), placeHolderId);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        ithAdapter.RemoveListItem(viewHolder.getAdapterPosition(), placeHolderId);
    }
}
