package android.com.progmobile.Controller;

import android.com.progmobile.Model.MyDictionary;
import android.com.progmobile.Model.Word;
import android.com.progmobile.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class WordActivity extends AppCompatActivity {


    private LinearLayout mLayout;
    private LayoutInflater mInflater;
    private static int id = 1000;
    public static final int ADD_NEW_WORD_ACTIVITY_REQUEST_CODE = 10;
    public static final int MODIFY_WORD = 11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words);
        mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mLayout = findViewById(R.id.activity_word_layout);
        setAddButton();
        MyDictionary.INSTANCE().sort((o1, o2) -> o1.getWord().compareToIgnoreCase(o2.getWord()));
        for (Word word : MyDictionary.INSTANCE()) {
            ArrayList<View> out = new ArrayList<>();
            mLayout.findViewsWithText(out,word.getWord(),View.FIND_VIEWS_WITH_TEXT);
            if(out.size() == 0){
                createButton(word, mInflater);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        Word word;
        if((ADD_NEW_WORD_ACTIVITY_REQUEST_CODE == requestCode || MODIFY_WORD == requestCode) && RESULT_OK == resultCode && data != null) {
            word = new Word(data.getStringExtra(AddWordActivity.EXTRA_NEW_WORD), data.getStringExtra(AddWordActivity.EXTRA_NEW_TRANSLATION));
            MyDictionary.INSTANCE().add(word);
            MyDictionary.INSTANCE().sort((o1, o2) -> o1.getWord().compareToIgnoreCase(o2.getWord()));
            for (Word words : MyDictionary.INSTANCE()) {
                if(mLayout.findViewWithTag(words.getWord()) == null) {
                    createButton(words, mInflater);
                }
            }
        }
    }

    private void createButton(Word word, LayoutInflater inf) {
        LinearLayout ll = (LinearLayout) inf.inflate(R.layout.word_styled_button, mLayout);
        LinearLayout childLl = (LinearLayout)ll.getChildAt(ll.getChildCount()-1);
        int localId = id++;
        childLl.setId(localId);
        Button btn = (Button)childLl.getChildAt(0);
        ImageButton imgBtn = (ImageButton)childLl.getChildAt(1);
        btn.setText(word.getWord());
        btn.setTag(word.getWord());
        btn.setOnClickListener(v -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            addPopUp(word, inflater);
        });
        imgBtn.setOnClickListener(v -> {
            ll.removeView(findViewById(localId));
            MyDictionary.INSTANCE().remove(word);
        });
    }

    public void addPopUp(Word word, LayoutInflater inflater) {
        View popupView = inflater.inflate(R.layout.translate_popup_window, null);
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height,focusable);
        popupWindow.showAtLocation(popupView,Gravity.CENTER, 0, 0);
        ((TextView)popupWindow.getContentView().findViewById(R.id.translate_popup_text)).setText(word.getTranslate());
        Button modifyButton = popupWindow.getContentView().findViewById(R.id.modify_button);
        modifyButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("MODIFY_WORD_TEXT", word.getWord());
            bundle.putString("MODIFY_TRANSLATE_TEXT", word.getTranslate());
            Intent intent = new Intent(WordActivity.this, AddWordActivity.class);
            intent.putExtras(bundle);
            Button bn = mLayout.findViewWithTag(word.getWord());
            mLayout.removeView(bn);
            MyDictionary.INSTANCE().remove(word);
            popupWindow.dismiss();
            recreate();
            startActivityForResult(intent, MODIFY_WORD);

        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.save(new TypeToken<List<Word>>() {}.getType(), MyDictionary.INSTANCE(), MainActivity.WORD_PREFERENCE_KEY);
    }

    private void setAddButton() {
        ImageButton addButton = findViewById(R.id.add_word_button);
        addButton.setOnClickListener(v -> {
            Intent addWordActivity = new Intent(WordActivity.this, AddWordActivity.class);
            startActivityForResult(addWordActivity, ADD_NEW_WORD_ACTIVITY_REQUEST_CODE);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mLayout.removeAllViews();
    }
}
