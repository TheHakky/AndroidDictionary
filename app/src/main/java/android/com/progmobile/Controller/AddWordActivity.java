package android.com.progmobile.Controller;

import android.com.progmobile.Model.MyDictionary;
import android.com.progmobile.R;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddWordActivity extends AppCompatActivity {

    private EditText mNewWordEditTxt;
    private EditText mTranslationEditTxt;
    private Button mAddNewWordButton;

    public static final String EXTRA_NEW_WORD = "EXTRA_NEW_WORD";
    public static final String EXTRA_NEW_TRANSLATION = "EXTRA_NEW_TRANSLATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        mAddNewWordButton = findViewById(R.id.add_new_word_btn);
        mNewWordEditTxt = findViewById(R.id.new_word_edit_txt);
        mTranslationEditTxt = findViewById(R.id.new_translate_edit_txt);

        if(getIntent().getStringExtra("MODIFY_WORD_TEXT") != null &&
                getIntent().getStringExtra("MODIFY_WORD_TEXT") != null) {
            mNewWordEditTxt.setText(getIntent().getStringExtra("MODIFY_WORD_TEXT"));
            mTranslationEditTxt.setText(getIntent().getStringExtra("MODIFY_TRANSLATE_TEXT"));
        }

        mAddNewWordButton.setOnClickListener(v -> {
            if(mNewWordEditTxt.length() == 0 || mTranslationEditTxt.length() == 0)
                Toast.makeText(getApplicationContext(), "Remplissez les champs", Toast.LENGTH_LONG).show();
            else if(MyDictionary.containsObjectWithWord(mNewWordEditTxt.getText().toString()))
                Toast.makeText(getApplicationContext(), "Le mot existe déjà", Toast.LENGTH_LONG).show();
            else {
                Intent intent = new Intent();
                Bundle extras = new Bundle();
                extras.putString("EXTRA_NEW_WORD", mNewWordEditTxt.getText().toString());
                extras.putString("EXTRA_NEW_TRANSLATION", mTranslationEditTxt.getText().toString());
                intent.putExtras(extras);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }


}
