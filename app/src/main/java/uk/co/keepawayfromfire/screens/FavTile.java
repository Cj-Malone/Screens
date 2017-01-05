package uk.co.keepawayfromfire.screens;

import android.content.Intent;
import android.service.quicksettings.TileService;

/**
 * Created by cj on 05/01/17.
 */

public class FavTile extends TileService {

    @Override
    public void onClick() {
        super.onClick();

        if (isLocked())
            return;

        //No way of checking split screen status in a service, so toggling is unreliable
        //startService(new Intent(this, SplitScreenService.class));

        Intent primaryIntent = getPackageManager().getLaunchIntentForPackage("com.android.contacts");
        primaryIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
        primaryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent secondaryIntent = getPackageManager().getLaunchIntentForPackage("com.android.calculator2");
        secondaryIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
        secondaryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivities(new Intent[]{primaryIntent, secondaryIntent});
    }
}
