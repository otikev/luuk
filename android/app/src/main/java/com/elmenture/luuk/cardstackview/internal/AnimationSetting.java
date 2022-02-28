package com.elmenture.luuk.cardstackview.internal;

import android.view.animation.Interpolator;

import com.elmenture.luuk.cardstackview.Direction;

public interface AnimationSetting {
    Direction getDirection();
    int getDuration();
    Interpolator getInterpolator();
}
