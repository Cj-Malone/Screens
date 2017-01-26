package uk.co.keepawayfromfire.screens;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.service.quicksettings.TileService;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import uk.co.keepawayfromfire.screens.tasker.Consts;

public class MainActivity extends Activity {

    public static final String ACTION_INSTALL_SHORTCUT
            = "com.android.launcher.action.INSTALL_SHORTCUT";

    private boolean isTabletLayout;
    private boolean isEditing;

    private ApplicationInfo package1;
    private ApplicationInfo package2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isEditing = false;
        isTabletLayout = findViewById(R.id.quickPic1Button) == null;

        if (savedInstanceState != null) {
            package1 = savedInstanceState.getParcelable("package1");
            package2 = savedInstanceState.getParcelable("package2");
        }

        loadFromIntent(getIntent());

        if (isTabletLayout) {
            FragmentManager fragmentManager = getFragmentManager();
            final PackagePickerFragment a = (PackagePickerFragment) fragmentManager
                    .findFragmentById(R.id.pkg1);
            final PackagePickerFragment b = (PackagePickerFragment) fragmentManager
                    .findFragmentById(R.id.pkg2);

            a.setApplicationInfoLisener(new PackagePickerFragment.AppInfoChangeListener() {
                @Override
                public void onAppInfoChanged(ApplicationInfo applicationInfo) {
                    package1 = applicationInfo;

                    updateUi();
                }
            });
            b.setApplicationInfoLisener(new PackagePickerFragment.AppInfoChangeListener() {
                @Override
                public void onAppInfoChanged(ApplicationInfo applicationInfo) {
                    package2 = applicationInfo;

                    updateUi();
                }
            });
        } else { //"phone"
            final Button quickPic1Button = (Button) findViewById(R.id.quickPic1Button);
            final Button quickPic2Button = (Button) findViewById(R.id.quickPic2Button);

            quickPic1Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), PackagePickerActivity.class);
                    startActivityForResult(intent, R.id.package1View);
                }
            });

            quickPic2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), PackagePickerActivity.class);
                    startActivityForResult(intent, R.id.package2View);
                }
            });
        }

        final EditText nameEditText = (EditText) findViewById(R.id.nameEditText);
        final Button createShortcutButton = (Button) findViewById(R.id.createShortcutButton);
        final Button setFavButton = (Button) findViewById(R.id.setFavourite);
        final Button saveButton = (Button) findViewById(R.id.saveButton);

        if (isEditing) {
            nameEditText.setVisibility(View.GONE);
            createShortcutButton.setVisibility(View.GONE);
            setFavButton.setVisibility(View.GONE);
            saveButton.setVisibility(View.VISIBLE);
        }

        createShortcutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (package1 == null || package2 == null) {
                    Toast.makeText(view.getContext(), R.string.select_packages, Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                Intent installIntent = new Intent();
                installIntent.setAction(ACTION_INSTALL_SHORTCUT);

                installIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                        nameEditText.getText().toString());
                installIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                        Intent.ShortcutIconResource.fromContext(view.getContext(),
                                R.drawable.logo));
                installIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                        ShortcutActivity.createShortcutIntent(view.getContext(),
                                package1.packageName,
                                package2.packageName
                        ));

                view.getContext().sendBroadcast(installIntent);

                // Go home
                Intent launcherIntent = new Intent();
                launcherIntent.setAction(Intent.ACTION_MAIN);
                launcherIntent.addCategory(Intent.CATEGORY_HOME);

                startActivity(launcherIntent);
            }
        });

        setFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (package1 == null || package2 == null) {
                    Toast.makeText(view.getContext(), R.string.select_packages, Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                SharedPreferences.Editor prefs = getSharedPreferences("prefs_tile", MODE_PRIVATE)
                        .edit();
                prefs.putString(ShortcutActivity.INTENT_TYPE,
                        ShortcutActivity.INTENT_TYPE_PACKAGES);
                prefs.putString(ShortcutActivity.INTENT_EXTRA_PACKAGE_1, package1.packageName);
                prefs.putString(ShortcutActivity.INTENT_EXTRA_PACKAGE_2, package2.packageName);
                prefs.apply();

                // Enable the tile
                PackageManager pm = getPackageManager();
                pm.setComponentEnabledSetting(new ComponentName(view.getContext(), FavTile.class),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);

                Toast.makeText(view.getContext(),
                        String.format(getString(R.string.quicksettings_added),
                                nameEditText.getText().toString()), Toast.LENGTH_SHORT).show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (package1 == null || package2 == null) {
                    Toast.makeText(view.getContext(), R.string.select_packages, Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (getIntent().getAction().equals(TileService.ACTION_QS_TILE_PREFERENCES)) {
                    SharedPreferences.Editor prefs = getSharedPreferences("prefs_tile",
                            MODE_PRIVATE).edit();
                    prefs.putString(ShortcutActivity.INTENT_TYPE,
                            ShortcutActivity.INTENT_TYPE_PACKAGES);
                    prefs.putString(ShortcutActivity.INTENT_EXTRA_PACKAGE_1, package1.packageName);
                    prefs.putString(ShortcutActivity.INTENT_EXTRA_PACKAGE_2, package2.packageName);
                    prefs.apply();
                } else if (getIntent().getAction().equals(Consts.ACTION_EDIT_SETTING)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ShortcutActivity.INTENT_TYPE,
                            ShortcutActivity.INTENT_TYPE_PACKAGES);
                    bundle.putString(ShortcutActivity.INTENT_EXTRA_PACKAGE_1, package1.packageName);
                    bundle.putString(ShortcutActivity.INTENT_EXTRA_PACKAGE_2, package2.packageName);

                    Intent intent = new Intent();
                    intent.putExtra(Consts.EXTRA_BUNDLE, bundle);

                    setResult(RESULT_OK, intent);
                }

                finish();
            }
        });

        updateUi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about_menu) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        ApplicationInfo applicationInfo = data.getParcelableExtra(
                PackagePickerFragment.INTENT_EXTRA_PACKAGE);

        if (requestCode == R.id.package1View) {
            package1 = applicationInfo;
        } else {
            package2 = applicationInfo;
        }

        updateUi();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putParcelable("package1", package1);
        outState.putParcelable("package2", package2);
    }

    public void updateUi() {
        if (isTabletLayout) {

        } else {
            updatePackageView(package1, findViewById(R.id.package1View));
            updatePackageView(package2, findViewById(R.id.package2View));
        }
    }

    private void updatePackageView(ApplicationInfo applicationInfo, View packageView) {
        ImageView iconImageView = (ImageView) packageView.findViewById(R.id.iconImageView);
        TextView nameTextView = (TextView) packageView.findViewById(R.id.nameTextView);
        TextView packageNameTextView = (TextView) packageView.findViewById(R.id.packageNameTextView);

        if (applicationInfo == null) {
            iconImageView.setImageDrawable(null);
            nameTextView.setText("");
            packageNameTextView.setText("");
        } else {
            PackageManager packageManager = getPackageManager();

            iconImageView.setImageDrawable(applicationInfo.loadIcon(packageManager));
            nameTextView.setText(applicationInfo.loadLabel(packageManager));
            packageNameTextView.setText(applicationInfo.packageName);
        }
    }

    public void loadFromIntent(Intent intent) {
        if(intent == null)
            return;
        if(intent.getAction() == null)
            return;

        if (intent.getAction().equals(TileService.ACTION_QS_TILE_PREFERENCES)) {
            isEditing = true;

            PackageManager pm = getPackageManager();
            try {
                SharedPreferences prefs = getSharedPreferences("prefs_tile", MODE_PRIVATE);

                if (prefs.getString(ShortcutActivity.INTENT_TYPE,
                        ShortcutActivity.INTENT_TYPE_PACKAGES)
                        .equals(ShortcutActivity.INTENT_TYPE_INTENTS))
                    return;

                String pkg1 = prefs.getString(ShortcutActivity.INTENT_EXTRA_PACKAGE_1, null);
                String pkg2 = prefs.getString(ShortcutActivity.INTENT_EXTRA_PACKAGE_2, null);

                if (pkg1 != null && !pkg1.isEmpty()) {
                    package1 = pm.getApplicationInfo(pkg1, 0);
                }
                if (pkg2 != null && !pkg2.isEmpty()) {
                    package2 = pm.getApplicationInfo(pkg2, 0);
                }
            } catch (PackageManager.NameNotFoundException ex) {
            }
        } else if (intent.getAction().equals(Consts.ACTION_EDIT_SETTING)) {
            isEditing = true;
            Bundle taskerBundle = intent.getBundleExtra(Consts.EXTRA_BUNDLE);

            if (taskerBundle.getString(ShortcutActivity.INTENT_TYPE)
                    .equals(ShortcutActivity.INTENT_TYPE_INTENTS))
                return;

            if (taskerBundle != null) {
                PackageManager pm = getPackageManager();
                try {
                    package1 = pm.getApplicationInfo(taskerBundle.getString(
                            ShortcutActivity.INTENT_EXTRA_PACKAGE_1), 0);
                    package2 = pm.getApplicationInfo(taskerBundle.getString(
                            ShortcutActivity.INTENT_EXTRA_PACKAGE_2), 0);
                } catch (PackageManager.NameNotFoundException ex) {
                }
            }
        }
    }
}
