package vst.manager;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.view.View;

import java.util.List;
import java.util.Stack;

public class Helpers {
    public static class ViewStack extends Stack<View> {

        @Override
        public View push(final View item) {
            final View returnView = super.push(item);

            return returnView;
        }
    }

    public static class PackageManager {
        public android.content.pm.PackageManager pm = null;
        PackageManager(Activity activity) {
            pm = activity.getApplicationContext().getPackageManager();
        }

        public Stack<PackageInfo> getPackageInfo(String containing) {
            Stack<PackageInfo> pkgList = null;
            for (PackageInfo pkg : pm.getInstalledPackages(0)) {
                if (pkgList == null) pkgList = new Stack<>();
                if (pkg.packageName.contains(containing)) pkgList.push(pkg);
            }
            return pkgList;
        }

        public String getDeviceNativeLibraryDir(PackageInfo pkg) {
            return pkg.applicationInfo.nativeLibraryDir;
        }
    }
}
