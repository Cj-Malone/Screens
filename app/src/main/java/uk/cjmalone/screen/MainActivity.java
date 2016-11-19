package uk.cjmalone.screen;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText package1EditText = (EditText) findViewById(R.id.package1EditText);
        final EditText package2EditText = (EditText) findViewById(R.id.package2EditText);
        final Button quickPic1Button = (Button) findViewById(R.id.quickPic1Button);
        final Button quickPic2Button = (Button) findViewById(R.id.quickPic2Button);

        final EditText nameEditText = (EditText) findViewById(R.id.nameEditText);

        final Button createShortcutButton = (Button) findViewById(R.id.createShortcutButton);

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
                                package1EditText.getText().toString(),
                                package2EditText.getText().toString()
                        ));

                view.getContext().sendBroadcast(installIntent);
            }
        });

        quickPic1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PackagePickerActivity.class);
                startActivityForResult(intent, R.id.package1EditText);
            }
        });

        quickPic2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PackagePickerActivity.class);
                startActivityForResult(intent, R.id.package2EditText);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != 0) {
            return;
        }

        final EditText packageEditText = (EditText) findViewById(requestCode);

        ApplicationInfo applicationInfo = (ApplicationInfo) data.getParcelableExtra("pkg");

        packageEditText.setText(applicationInfo.packageName);
    }
}
