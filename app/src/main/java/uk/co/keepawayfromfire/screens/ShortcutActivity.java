package uk.co.keepawayfromfire.screens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

public class ShortcutActivity extends Activity {

    public static final String INTENT_EXTRA_PACKAGE_1 = "pkg1";
    public static final String INTENT_EXTRA_PACKAGE_2 = "pkg2";

    public static final String INTENT_EXTRA_1 = "intent1";
    public static final String INTENT_EXTRA_2 = "intent2";

    public static Intent createShortcutIntent(Context context, String package1, String package2) {
        Intent shortcutIntent = new Intent(context, ShortcutActivity.class);

        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

        shortcutIntent.putExtra(INTENT_EXTRA_PACKAGE_1, package1);
        shortcutIntent.putExtra(INTENT_EXTRA_PACKAGE_2, package2);

        return shortcutIntent;
    }

    public static Intent createShortcutIntent(Context context, Intent intent1, Intent intent2) {
        Intent shortcutIntent = new Intent(context, ShortcutActivity.class);

        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

        shortcutIntent.putExtra(INTENT_EXTRA_1, intent1);
        shortcutIntent.putExtra(INTENT_EXTRA_2, intent2);

        return shortcutIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isInMultiWindowMode()) {
            thunderbirdsAreGo();
        } else {
            setContentView(R.layout.activity_shortcut);

            Button accessibilityOptionsButton = (Button) findViewById(R.id.accessibilityOptionsButton);
            accessibilityOptionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent accessibilityIntent = new Intent();
                    accessibilityIntent.setAction(Settings.ACTION_ACCESSIBILITY_SETTINGS);

                    startActivity(accessibilityIntent);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        if (!isInMultiWindowMode())
            // This should trigger the activity being re created and this time
            // onCreate should do thunderbirdsAreGo
            // should should should.
            startService(new Intent(this, SplitScreenService.class));

        super.onResume();
    }

    public void thunderbirdsAreGo() {
        Intent primaryIntent;
        Intent secondaryIntent;

        String pkg1 = getIntent().getStringExtra(INTENT_EXTRA_PACKAGE_1);
        String pkg2 = getIntent().getStringExtra(INTENT_EXTRA_PACKAGE_2);

        if (pkg1 == null || pkg2 == null || pkg1.isEmpty() || pkg2.isEmpty()) {
            primaryIntent = getIntent().getParcelableExtra(ShortcutActivity.INTENT_EXTRA_1);
            secondaryIntent = getIntent().getParcelableExtra(ShortcutActivity.INTENT_EXTRA_2);
        } else {
            primaryIntent = getPackageManager().getLaunchIntentForPackage(pkg1);
            primaryIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
            primaryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            secondaryIntent = getPackageManager().getLaunchIntentForPackage(pkg2);
            secondaryIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
            secondaryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        startActivities(new Intent[]{secondaryIntent, primaryIntent});
        finish();
    }
}