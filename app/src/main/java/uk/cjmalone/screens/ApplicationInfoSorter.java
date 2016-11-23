package uk.cjmalone.screens;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.Comparator;

public class ApplicationInfoSorter implements Comparator<ApplicationInfo> {
    private final PackageManager packageManager;

    public ApplicationInfoSorter(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    @Override
    public int compare(ApplicationInfo a1, ApplicationInfo a2) {
        return a1.loadLabel(packageManager).toString().compareToIgnoreCase(
                a2.loadLabel(packageManager).toString());
    }
}
