package uk.co.keepawayfromfire.screens;

import android.app.ListActivity;
import android.app.UiModeManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class SessionsListActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UiModeManager uiModeManager = getSystemService(UiModeManager.class);
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1)
            uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_AUTO);
        else
            uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);

        ArrayList<Session> savedSessions = new ArrayList<>();

        Session s = new Session();
        s.name = "CAH Bubble";
        savedSessions.add(s);

        setListAdapter(new SessionAdapter(this, savedSessions));
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.session_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about_menu) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.createSession) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.report_broken) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://github.com/Cj-Malone/Screens/issues/new?title=[Broken]&body=Please%20enter%20the%20app%20name%20and%20id,%20or%20a%20link%20to%20the%20app%20on%20the%20Google%20Play%20Store."));
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.recommend) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://github.com/Cj-Malone/Screens/issues/new?title=[Session%20recommendation]&body=Please%20enter%20the%20app%20names%20and%20ids,%20or%20links%20to%20them%20on%20the%20Google%20Play%20Store."));
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        getListView().setItemChecked(position, true);

        Session session = (Session) l.getItemAtPosition(position);

        // startActivity(ShortcutActivity.createShortcutIntent(this, session.package1.packageName, session.package2.packageName));
        startActivity(ShortcutActivity.createShortcutIntent(this, "arnold.cja.cah", "com.nkanaev.comics"));
    }
}
