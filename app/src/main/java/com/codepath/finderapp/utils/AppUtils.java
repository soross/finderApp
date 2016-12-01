package com.codepath.finderapp.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;

/**
 * Created by hison7463 on 11/12/16.
 */

public class AppUtils {

    public static void disPlayFragment(FragmentManager fragmentManager, Fragment fragment, int layout) {
        fragmentManager.beginTransaction().replace(layout, fragment).commit();
    }

    public static int getPixelsFromDp(Context context, float dp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return (int) px;
    }

    public static void SendInvite(Context context) {
        String appLinkUrl =  "https://fb.me/161679120966718";
        String previewImageUrl = "https://i.imgur.com/XgxWfyF.png";
        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(appLinkUrl)
                    .setPreviewImageUrl(previewImageUrl)
                    .build();
            AppInviteDialog.show((AppCompatActivity) context, content);
        }
    }
}
