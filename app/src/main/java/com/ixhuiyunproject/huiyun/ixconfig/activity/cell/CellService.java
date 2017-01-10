/**
 * 
 */
package com.ixhuiyunproject.huiyun.ixconfig.activity.cell;

import android.app.IntentService;
import android.content.Intent;

/**
 * @author "Volodymyr Kurniavka"
 *
 */
public class CellService extends IntentService {

    public CellService() {
        super("StartAVService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent activityIntent = intent;
        activityIntent.setClass(this, DoorFragment.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(activityIntent);
    }

}
