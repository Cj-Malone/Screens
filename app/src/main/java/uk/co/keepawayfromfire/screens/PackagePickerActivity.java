package uk.co.keepawayfromfire.screens;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by cj on 26/11/16.
 */
public class PackagePickerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new PackagePickerFragment()).commit();
    }
}
