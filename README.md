# Screens

Screens is an Android&trade; utility that allows you to quickly enter splitscreen mode with pre selected apps. It creates shortcuts that can be opened from any launcher. This might be good for productivity or having [SeriesGuide](https://seriesgui.de/) and [Kore](https://github.com/xbmc/Kore) at the same time.

The name and logo are based upon the [GNU Screen](https://www.gnu.org/software/screen/) and [tmux](https://tmux.github.io/) utlitys however functionality is closer to [tmuxinator](https://github.com/tmuxinator/tmuxinator">tmuxinator).

##Tasker
Tasker and Screens can be used in combination together, by using the send intent action you can trigger Screens to launch from any task.

Example action:
```
Send Intent [
  Action:android.intent.action.VIEW
  Cat:Default
  Mime Type:
  Data:
  Extra:pkg1:com.android.gallery3d
  Extra:pkg2:com.android.calculator2
  Extra:
  Package:uk.co.keepawayfromfire.screens
  Class:uk.co.keepawayfromfire.screens.ShortcutActivity
  Target:Activity
]
```
