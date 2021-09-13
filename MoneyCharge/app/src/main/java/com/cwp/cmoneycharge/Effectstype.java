package com.cwp.cmoneycharge;

import com.cwp.cmoneycharge.RotateBottom;
import com.cwp.cmoneycharge.Shake;


/**
 * Created by lee on 2014/7/30.
 */
public enum  Effectstype {

	Slidetop(SlideTop.class),
    RotateBottom(RotateBottom.class),
    Shake(Shake.class);
    
    private Class effectsClazz;

    private Effectstype(Class mclass) {
        effectsClazz = mclass;
    }

    public BaseEffects getAnimator() {
        try {
            return (BaseEffects) effectsClazz.newInstance();
        } catch (Exception e) {
            throw new Error("Can not init animatorClazz instance");
        }
    }
}
