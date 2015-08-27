package com.example.sportsdialogdemo;

import android.content.Context;
import android.view.View;

/**
 * Created by Maxim Dybarsky | maxim.dybarskyy@gmail.com
 * on 13.01.15 at 14:17
 */
public class AnimatedView extends View {

    private int target;

    public AnimatedView(Context context) {
        super(context);
    }

    public float getXFactor() {
        return getX() / target;
    }

    public void setXFactor(float xFactor) {
        setX(target * xFactor);
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getTarget() {
        return target;
    }
}
