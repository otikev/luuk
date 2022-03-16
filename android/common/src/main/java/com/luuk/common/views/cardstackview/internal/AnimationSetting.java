package com.luuk.common.views.cardstackview.internal;

import android.view.animation.Interpolator;

import com.luuk.common.views.cardstackview.Direction;

public interface AnimationSetting {
    Direction getDirection();
    int getDuration();
    Interpolator getInterpolator();
}
