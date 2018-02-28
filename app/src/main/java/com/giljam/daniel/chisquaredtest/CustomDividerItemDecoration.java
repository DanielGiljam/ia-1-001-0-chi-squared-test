/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * See the original DividerItemDecoration.java file at https://android.googlesource.com
 * List of modifications in this modified ("Custom") version of the DividerItemDecoration:
 * - divider is not drawn on the last item
 * - added "angle" parameter
 *   and the drawing of a border on one side
 *   determined by the "angle" parameter
 * Modified version made by Daniel Giljam, 2018
 */

package com.giljam.daniel.chisquaredtest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CustomDividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    private Drawable mDivider;
    private Drawable mBorder;
    private int mOrientation;
    private int mAngle;

    public CustomDividerItemDecoration(Context context, int orientation, int angle) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        mBorder = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
        setAngle(angle);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    public void setAngle(int angle) {
        if (angle != HORIZONTAL_LIST && angle != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid angle value");
        }
        mAngle = angle;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int dividerLeft = parent.getPaddingLeft();
        final int dividerRight = parent.getWidth() - parent.getPaddingRight();
        /*if (mAngle == HORIZONTAL_LIST) {
            final int borderLeft1 = parent.getPaddingLeft();
            final int borderTop1 = parent.getPaddingTop();
            final int borderRight1 = borderLeft1 + mBorder.getIntrinsicHeight();
            final int borderBottom1 = parent.getHeight() - parent.getPaddingBottom();
            mBorder.setBounds(borderLeft1, borderTop1, borderRight1, borderBottom1);
            mBorder.draw(c);
        } else {
            final int borderTop2 = parent.getPaddingTop();
            final int borderRight2 = parent.getWidth() - parent.getPaddingRight();
            final int borderBottom2 = parent.getHeight() - parent.getPaddingBottom();
            final int borderLeft2 = borderRight2 - mBorder.getIntrinsicHeight();
            mBorder.setBounds(borderLeft2, borderTop2, borderRight2, borderBottom2);
            mBorder.draw(c);
        }*/
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int dividerBottom = child.getBottom() + params.bottomMargin +
                    Math.round(ViewCompat.getTranslationY(child));
            final int dividerTop = dividerBottom - mDivider.getIntrinsicHeight();
            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            mDivider.draw(c);
            if (i == 0) {
                final int firstDividerTop = child.getTop() - params.topMargin -
                        Math.round(ViewCompat.getTranslationY(child));
                final int firstDividerBottom = firstDividerTop + mDivider.getIntrinsicHeight();
                mDivider.setBounds(dividerLeft, firstDividerTop, dividerRight, firstDividerBottom);
                mDivider.draw(c);
            }
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int dividerTop = parent.getPaddingTop();
        final int dividerBottom = parent.getHeight() - parent.getPaddingBottom();
        /*if (mAngle == VERTICAL_LIST) {
            final int borderTop1 = parent.getPaddingTop();
            final int borderRight1 = parent.getWidth() - parent.getPaddingRight();
            final int borderBottom1 = borderTop1 + mBorder.getIntrinsicHeight();
            final int borderLeft1 = parent.getPaddingLeft();
            mBorder.setBounds(borderLeft1, borderTop1, borderRight1, borderBottom1);
            mBorder.draw(c);
        } else {
            final int borderRight2 = parent.getWidth() - parent.getPaddingRight();
            final int borderBottom2 = parent.getHeight() - parent.getPaddingBottom();
            final int borderLeft2 = parent.getPaddingLeft();
            final int borderTop2 = borderBottom2 - mBorder.getIntrinsicHeight();
            mBorder.setBounds(borderLeft2, borderTop2, borderRight2, borderBottom2);
            mBorder.draw(c);
        }*/
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int dividerRight = child.getRight() + params.rightMargin +
                    Math.round(ViewCompat.getTranslationX(child));
            final int dividerLeft = dividerRight - mDivider.getIntrinsicHeight();
            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            mDivider.draw(c);
            if (i == 0) {
                final int firstDividerLeft = child.getLeft() - params.leftMargin -
                        Math.round(ViewCompat.getTranslationX(child));
                final int firstDividerRight = firstDividerLeft + mDivider.getIntrinsicHeight();
                mDivider.setBounds(firstDividerLeft, dividerTop, firstDividerRight, dividerBottom);
                mDivider.draw(c);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, 0);
        } else {
            outRect.set(0, 0, 0, 0);
        }
    }
}