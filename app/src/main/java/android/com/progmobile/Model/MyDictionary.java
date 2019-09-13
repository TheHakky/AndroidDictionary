package android.com.progmobile.Model;

import java.util.ArrayList;
import java.util.List;

public final class MyDictionary {

    private static List<Word> mDictionary;

    public static List<Word> INSTANCE() {
        if(mDictionary == null)
            mDictionary = new ArrayList<>();
        return mDictionary;
    }

    public static boolean containsObjectWithWord(String word) {
        return mDictionary.stream().anyMatch(elt -> elt.getWord().toLowerCase().equals(word.toLowerCase()));
    }
}
