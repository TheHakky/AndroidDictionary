package android.com.progmobile.Controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.com.progmobile.Model.MyDictionary;
import android.com.progmobile.Model.QuestionBank;
import android.com.progmobile.Model.Word;
import android.com.progmobile.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private static final int INTERROGATION_ACTIVITY_REQUEST_CODE = 2;
    private static final int PARAM_ACTIVITY_REQUEST_CODE = 3;
    public static final int REQUEST_CODE = 31;
    public static final String WORD_PREFERENCE_KEY = "MyDictionary";
    public static final String HISTORY_PREFERENCE_KEY = "QuestionBank";
    private Button mMotsButton;
    private Button mInterrogationButton;
    private Button mHistoriqueButton;
    private Button mParamButton;
    private Button mExitButton;
    private static SharedPreferences mPrefs;
    private int mNbQuestionForInterrogation = 4;
    private HashMap<String, String> mScoreHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMotsButton = findViewById(R.id.activity_main_mots_btn);
        mInterrogationButton = findViewById(R.id.activity_main_interrogation_btn);
        mHistoriqueButton = findViewById(R.id.activity_main_histo_btn);
        mParamButton = findViewById(R.id.activity_main_param_btn);
        mExitButton = findViewById(R.id.activity_main_exit_btn);


        mPrefs = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(WORD_PREFERENCE_KEY, "");
        Type wordType = new TypeToken<List<Word>>() {}.getType();
        List<Word> list = gson.fromJson(json, wordType);
        List<Word> listWithoutDuplicate = null;
        if (list != null) {
            listWithoutDuplicate = list.stream().distinct().collect(Collectors.toList());
        }
        if(listWithoutDuplicate != null) {
            listWithoutDuplicate.forEach(elt -> {
                if(!MyDictionary.INSTANCE().contains(elt))
                    MyDictionary.INSTANCE().add(elt);
            });
            list.clear();
        }

        json = mPrefs.getString(HISTORY_PREFERENCE_KEY, "");
        HashMap<String, ArrayList<Integer>> historyList = gson.fromJson(json, new TypeToken<HashMap<String, ArrayList<Integer>>>() {}.getType());
        if(historyList != null) {
            historyList.forEach((key, value) -> {
                if(QuestionBank.getScoreMap().containsKey(key))
                    QuestionBank.getScoreMap().replace(key, value);
            });
            historyList.clear();
        }


        mScoreHistory = new HashMap<>();


        mMotsButton.setOnClickListener(v -> {
            Intent wordsActivity = new Intent(MainActivity.this, WordActivity.class);
            startActivity(wordsActivity);
        });

        mInterrogationButton.setOnClickListener(v -> {
            Intent interrogationActivity = new Intent(MainActivity.this, InterrogationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("NUMBER_OF_QUESTION", mNbQuestionForInterrogation);
            interrogationActivity.putExtras(bundle);
            startActivityForResult(interrogationActivity, INTERROGATION_ACTIVITY_REQUEST_CODE);
        });

        mHistoriqueButton.setOnClickListener(v -> {
            Intent historyActivity = new Intent(MainActivity.this, HistoryActivity.class);
            historyActivity.putExtra("HISTORY_MAP", mScoreHistory);
            startActivity(historyActivity);
        });

        mParamButton.setOnClickListener(v -> {
            Intent paramActivity = new Intent(MainActivity.this, ParamActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("NBQUESTION", mNbQuestionForInterrogation);
            paramActivity.putExtras(bundle);
            startActivityForResult(paramActivity, PARAM_ACTIVITY_REQUEST_CODE);
        });

        mExitButton.setOnClickListener(v -> System.exit(0));


        setNotification(InterrogationReceiver.class, 7);
        setNotification(WordListReceiver.class, 30);
    }

    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (PARAM_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode && data != null) {
            mNbQuestionForInterrogation = data.getIntExtra("PARAM_NUMBER_OF_QUESTION", 4);
        }
    }

    private void setNotification(Class receiverClass, int frequency) {
        Intent intent = new Intent(MainActivity.this, receiverClass);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, REQUEST_CODE, intent, 0);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.setRepeating(am.RTC_WAKEUP, System.currentTimeMillis(), am.INTERVAL_DAY*frequency, pendingIntent);
    }


    public static void save(Type type, Object src, String refKey) {
        SharedPreferences.Editor editor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(src, type);
        editor.putString(refKey, json);
        editor.apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        save(new TypeToken<List<Word>>() {}.getType(), MyDictionary.INSTANCE(), WORD_PREFERENCE_KEY);
        save(new TypeToken<HashMap<String, ArrayList<Integer>>>() {}.getType(), QuestionBank.getScoreMap(), HISTORY_PREFERENCE_KEY);
    }
}
