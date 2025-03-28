package views.cardstackview.internal;

import android.view.animation.Interpolator;

import views.cardstackview.Direction;

public interface AnimationSetting {
    Direction getDirection();
    int getDuration();
    Interpolator getInterpolator();
}
