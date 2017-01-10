/**
 * 
 */
package com.ixhuiyunproject.abtotest.voiptest;

import android.app.IntentService;
import android.content.Intent;

/**
 * @author "Volodymyr Kurniavka"
 *
 */
public class StartAVActivityService extends IntentService {

    public StartAVActivityService() {
        super("StartAVService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent activityIntent = intent;
        activityIntent.setClass(this, ScreenAV.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(activityIntent);
    }

}
