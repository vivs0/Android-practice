
package com.vivek.BudgetManager.notif;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Simple Broadcast receiver that is just here to receive events when a package is updated. This is
 * made to awake our application on an update (especially EasyBudget ones) to perform action. Those
 * actions are made on {@link com.vivek.BudgetManager.BudgetManager#onUpdate(int, int)}.
 *
 * @author vivek singh
 */
public class AppUpdateBroadcastReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {

    }
}
