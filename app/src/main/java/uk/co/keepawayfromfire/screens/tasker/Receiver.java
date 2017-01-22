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

        if (bundle.getString(ShortcutActivity.INTENT_TYPE)
                .equals(ShortcutActivity.INTENT_TYPE_INTENTS)) {

            Intent intent1 = bundle.getParcelable(ShortcutActivity.INTENT_EXTRA_1);
            Intent intent2 = bundle.getParcelable(ShortcutActivity.INTENT_EXTRA_2);

            if (intent1 == null || intent2 == null)
                return;

            context.startActivity(ShortcutActivity.createShortcutIntent(context, intent1, intent2));
        } else {
            String pkg1 = bundle.getString(ShortcutActivity.INTENT_EXTRA_PACKAGE_1);
            String pkg2 = bundle.getString(ShortcutActivity.INTENT_EXTRA_PACKAGE_2);

            if (pkg1 == null || pkg2 == null || pkg1.isEmpty() || pkg2.isEmpty())
                return;

            context.startActivity(ShortcutActivity.createShortcutIntent(context, pkg1, pkg2));
        }
    }
}
