package com.giljam.daniel.chisquaredtest.setup;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

public class SetupListLayoutManager extends LinearLayoutManager {

    private boolean canScrollVertically = false;

    public SetupListLayoutManager(Context context) {
        super(context, 1, false);
    }

    void setCanScrollVertically(boolean canScrollVertically) {
        this.canScrollVertically = canScrollVertically;
    }

    @Override
    public boolean canScrollVertically() {
        return canScrollVertically;
    }
}
