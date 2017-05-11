package uk.co.keepawayfromfire.screens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ShortcutActivity extends Activity {

    public static final String INTENT_EXTRA_PACKAGE_1 = "pkg1";
    public static final String INTENT_EXTRA_PACKAGE_2 = "pkg2";

    public static final String INTENT_EXTRA_1 = "intent1";
    public static final String INTENT_EXTRA_2 = "intent2";

    public static final String INTENT_TYPE = "version";
    public static final String INTENT_TYPE_PACKAGES = "package";
    public static final String INTENT_TYPE_INTENTS = "intent";
    public static final String INTENT_TYPE_INTENTS_STRING = "intent_string";

    public Intent primaryIntent;
    public Intent secondaryIntent;

    public static Intent createShortcutIntent(Context context, String package1, String package2) {
        Intent shortcutIntent = new Intent(context, ShortcutActivity.class);

        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

        shortcutIntent.putExtra(INTENT_EXTRA_PACKAGE_1, package1);
        shortcutIntent.putExtra(INTENT_EXTRA_PACKAGE_2, package2);

        shortcutIntent.putExtra(INTENT_TYPE, INTENT_TYPE_PACKAGES);

        return shortcutIntent;
    }

    public static Intent createShortcutIntent(Context context, Intent intent1, Intent intent2) {
        Intent shortcutIntent = new Intent(context, ShortcutActivity.class);

        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

        shortcutIntent.putExtra(INTENT_EXTRA_1, intent1);
        shortcutIntent.putExtra(INTENT_EXTRA_2, intent2);

        shortcutIntent.putExtra(INTENT_TYPE, INTENT_TYPE_INTENTS);

        return shortcutIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadIntent(getIntent());

        if (isInMultiWindowMode()) {
            thunderbirdsAreGo();
        } else {
            setContentView(R.layout.activity_shortcut);

            Button accessibilityOptionsButton = (Button) findViewById(
                    R.id.accessibilityOptionsButton);

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

    public void loadIntent(Intent intent) {
        if (intent == null) {
            goHome();
            return;
        }

        if (intent.getStringExtra(INTENT_TYPE) == null || intent.getStringExtra(INTENT_TYPE)
                .equals(INTENT_TYPE_PACKAGES)) {
            String pkg1 = intent.getStringExtra(INTENT_EXTRA_PACKAGE_1);
            String pkg2 = intent.getStringExtra(INTENT_EXTRA_PACKAGE_2);

            primaryIntent = getPackageManager().getLaunchIntentForPackage(pkg1);
            secondaryIntent = getPackageManager().getLaunchIntentForPackage(pkg2);

            if (primaryIntent == null || secondaryIntent == null) {
                goHome();
                return;
            }
            primaryIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
            primaryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            primaryIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

            secondaryIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
            secondaryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            secondaryIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        } else if (intent.getStringExtra(INTENT_TYPE).equals(INTENT_TYPE_INTENTS)) {
            primaryIntent = intent.getParcelableExtra(ShortcutActivity.INTENT_EXTRA_1);
            secondaryIntent = intent.getParcelableExtra(ShortcutActivity.INTENT_EXTRA_2);

            if (primaryIntent == null || secondaryIntent == null) {
                goHome();
                return;
            }
        }
    }

    public void thunderbirdsAreGo() {
        if (primaryIntent == null || secondaryIntent == null) {
            goHome();
            return;
        }

        startActivities(new Intent[]{secondaryIntent, primaryIntent});
        finishAndRemoveTask();
    }

    public void goHome() {
        Toast.makeText(this, R.string.not_installed, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
