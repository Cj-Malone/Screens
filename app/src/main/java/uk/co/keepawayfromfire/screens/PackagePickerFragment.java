package uk.co.keepawayfromfire.screens;

import android.app.ListFragment;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PackagePickerFragment extends ListFragment {

    public static final String INTENT_EXTRA_PACKAGE = "pkg";
    public static Set<String> BLACKLISTED_PACKAGES = new HashSet<>(Arrays.asList(
            "com.actionlauncher.playstore" //Action launcher dosen't even show this
    ));

    AppInfoChangeListener listener;
    private boolean isTabletLayout;
    private int selectedItem;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        PackageManager packageManager = getActivity().getPackageManager();

        ArrayList<ApplicationInfo> acceptablePackages = new ArrayList<>();
        List<PackageInfo> allPackages = packageManager.getInstalledPackages(
                PackageManager.GET_META_DATA);

        for (PackageInfo packageInfo : allPackages) {
            // Only add packages that would show up in the launcher
            if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) == null)
                continue;
            if (BLACKLISTED_PACKAGES.contains(packageInfo.packageName))
                continue;

            acceptablePackages.add(packageInfo.applicationInfo);
        }

        Collections.sort(acceptablePackages, new ApplicationInfoSorter(packageManager));

        if (savedInstanceState != null) {
            selectedItem = savedInstanceState.getInt("selectedItem", 0);
            getListView().setSelection(selectedItem);
        }

        isTabletLayout = getActivity().findViewById(R.id.nameEditText) != null;
        if (isTabletLayout)
            // By default it appears to have no selector?
            getListView().setSelector(R.drawable.package_list_selector);

        setListAdapter(new ApplicationInfoAdapter(this.getContext(), acceptablePackages));
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        getListView().setItemChecked(position, true);

        ApplicationInfo applicationInfo = (ApplicationInfo) l.getItemAtPosition(position);

        if (isTabletLayout) {
            if (listener != null)
                listener.onAppInfoChanged(applicationInfo);
        } else {
            // Maybe I should just use the listener here as well,
            // but currently starting for result when it's an activity
            Intent resultIntent = new Intent();
            resultIntent.putExtra(INTENT_EXTRA_PACKAGE, applicationInfo);

            getActivity().setResult(getActivity().RESULT_OK, resultIntent);
            getActivity().finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("selectedItem", selectedItem);
    }

    public void setApplicationInfoLisener(AppInfoChangeListener applicationInfoLisener) {
        this.listener = applicationInfoLisener;
    }

    public interface AppInfoChangeListener {
        void onAppInfoChanged(ApplicationInfo applicationInfo);
    }
}
