package uk.cjmalone.screens;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PackagePickerActivity extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PackageManager packageManager = getPackageManager();

        ArrayList<ApplicationInfo> acceptablePackages = new ArrayList<>();
        List<PackageInfo> allPackages = packageManager.getInstalledPackages(
                PackageManager.GET_META_DATA);

        for (PackageInfo packageInfo : allPackages) {
            if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                acceptablePackages.add(packageInfo.applicationInfo);
            }
        }

        Collections.sort(acceptablePackages, new ApplicationInfoSorter(packageManager));

        setListAdapter(new ApplicationInfoAdapter(this, acceptablePackages));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("pkg", (ApplicationInfo) l.getItemAtPosition(position));

        setResult(0, resultIntent);
        finish();
    }
}
