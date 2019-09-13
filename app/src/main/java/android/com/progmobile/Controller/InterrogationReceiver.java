package android.com.progmobile.Controller;

import android.content.Context;
import android.content.Intent;

public class InterrogationReceiver extends AbstractReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        setTextToShow("N'oubliez pas votre quiz hebdomadaire !");
        super.onReceive(context, intent);
    }

}
