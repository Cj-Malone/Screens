package uk.co.keepawayfromfire.screens;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button quickPic1Button = (Button) findViewById(R.id.quickPic1Button);
        final Button quickPic2Button = (Button) findViewById(R.id.quickPic2Button);

        final EditText nameEditText = (EditText) findViewById(R.id.nameEditText);

        final Button createShortcutButton = (Button) findViewById(R.id.createShortcutButton);

        final TextView package1NameTextView = (TextView) findViewById(R.id.package1View).findViewById(R.id.packageNameTextView);
        final TextView package2NameTextView = (TextView) findViewById(R.id.package2View).findViewById(R.id.packageNameTextView);

        createShortcutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent installIntent = new Intent();
                installIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

                installIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                        (String) nameEditText.getText().toString());
                installIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                        Intent.ShortcutIconResource.fromContext(view.getContext(),
                                R.drawable.logo));
                installIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                        ShortcutActivity.createShortcutIntent(view.getContext(),
                                package1NameTextView.getText().toString(),
                                package2NameTextView.getText().toString()
                        ));

                view.getContext().sendBroadcast(installIntent);

                Intent launcherIntent = new Intent();
                launcherIntent.setAction(Intent.ACTION_MAIN);
                launcherIntent.addCategory(Intent.CATEGORY_HOME);

                startActivity(launcherIntent);
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        ApplicationInfo applicationInfo = (ApplicationInfo) data.getParcelableExtra("pkg");

        PackageManager packageManager = getPackageManager();

        View packageView = findViewById(requestCode);

        ImageView iconImageView = (ImageView) packageView.findViewById(R.id.iconImageView);
        TextView nameTextView = (TextView) packageView.findViewById(R.id.nameTextView);
        TextView packageNameTextView = (TextView) packageView.findViewById(R.id.packageNameTextView);

        iconImageView.setImageDrawable(applicationInfo.loadIcon(packageManager));
        nameTextView.setText(applicationInfo.loadLabel(packageManager));
        packageNameTextView.setText(applicationInfo.packageName);
    }
}
