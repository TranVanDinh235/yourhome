package com.example.homedy.Animation;

import android.animation.Animator;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

public abstract class BaseForcedTransition extends Transition {
    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        transitionValues.values.put("dummy","dummy");
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        transitionValues.values.put("dummy","other_dummy");
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        return getAnimator(endValues.view);
    }

    public abstract Animator getAnimator(View view);
}
