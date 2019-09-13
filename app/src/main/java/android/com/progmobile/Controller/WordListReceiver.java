package android.com.progmobile.Controller;

import android.content.Context;
import android.content.Intent;

public class WordListReceiver extends AbstractReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        setTextToShow("N'oubliez pas d'ajouter des nouveaux mots à votre dictionnaire !");
        super.onReceive(context, intent);
    }

}
