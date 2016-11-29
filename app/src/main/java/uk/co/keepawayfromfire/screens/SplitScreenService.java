package uk.co.keepawayfromfire.screens;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

public class SplitScreenService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        performGlobalAction(GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN);

        return super.onStartCommand(intent, flags, startId);
    }
}
