package android.com.progmobile.Model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public final class QuestionBank {

    private static List<Question> mQuestionList;
    private static HashMap<String, ArrayList<Integer>> mWordScoreMap;


    public static List<Question> getQuestionList() {
        if(mQuestionList == null)
            mQuestionList = new ArrayList<>();
        return mQuestionList;
    }

    public static HashMap<String, ArrayList<Integer>> getScoreMap() {
        if(mWordScoreMap == null) {
            mWordScoreMap = new HashMap<>();
            MyDictionary.INSTANCE().forEach(elt -> mWordScoreMap.put(elt.getTranslate(), new ArrayList<Integer>(Arrays.asList(0,0)))); //first elt = correctScore, second elt = wrongScore
        }
        return mWordScoreMap;
    }

    public static boolean containsQuestionWithWord(String word) {
        return mQuestionList.stream().anyMatch(elt -> elt.getQuestion().toLowerCase().equals(word.toLowerCase()));
    }
}
