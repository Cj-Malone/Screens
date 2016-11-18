package uk.cjmalone.screen;

import android.app.Activity;
import android.content.Intent;
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

        final EditText nameEditText = (EditText) findViewById(R.id.nameEditText);

        final Button createShortcutButton = (Button) findViewById(R.id.createShortcutButton);

        createShortcutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent installIntent = new Intent();
                installIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

                installIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, (String) nameEditText.getText().toString());
                installIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                        ShortcutActivity.createShortcutIntent(view.getContext(),
                                package1EditText.getText().toString(),
                                package2EditText.getText().toString()
                        ));

                installIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                        Intent.ShortcutIconResource.fromContext(view.getContext(), R.drawable.logo));

                view.getContext().sendBroadcast(installIntent);
            }
        });
    }
}
