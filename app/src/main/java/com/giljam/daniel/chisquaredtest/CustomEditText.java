package com.giljam.daniel.chisquaredtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

public class CustomEditText extends android.support.v7.widget.AppCompatEditText {

    interface SoftKeyboardBackInterceptor {
        void OnBackPressed();
    }

    private SoftKeyboardBackInterceptor skbInterceptor;

    void setSkbInterceptor(SoftKeyboardBackInterceptor skbInterceptor) {
        this.skbInterceptor = skbInterceptor;
    }

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (skbInterceptor != null && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            KeyEvent.DispatcherState state = getKeyDispatcherState();
            if (state != null) {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                    event.getRepeatCount() == 0) {
                    state.startTracking(event, this);
                    return true;
                } else if ( event.getAction() == KeyEvent.ACTION_UP &&
                            !event.isCanceled() && state.isTracking(event)) {
                    skbInterceptor.OnBackPressed();
                    return true;
                }
            }
        }
        return super.dispatchKeyEventPreIme(event);
    }
}
