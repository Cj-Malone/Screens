package uk.co.keepawayfromfire.screens.tasker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import uk.co.keepawayfromfire.screens.ShortcutActivity;

/**
 * Created by cj on 09/01/17.
 */

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra(Consts.EXTRA_BUNDLE);
        if (bundle == null)
            return;
        String pkg1 = bundle.getString(ShortcutActivity.INTENT_EXTRA_PACKAGE_1);
        String pkg2 = bundle.getString(ShortcutActivity.INTENT_EXTRA_PACKAGE_2);

        if (pkg1 == null || pkg2 == null || pkg1.isEmpty() || pkg2.isEmpty())
            return;
        else
            context.startActivity(ShortcutActivity.createShortcutIntent(context, pkg1, pkg2));
    }
}
