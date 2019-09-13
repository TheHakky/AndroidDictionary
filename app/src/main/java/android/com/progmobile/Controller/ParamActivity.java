package android.com.progmobile.Controller;

import android.com.progmobile.R;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ParamActivity extends AppCompatActivity {

    private TextView mNbQuestionText;
    private Button mSaveAndExitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param);

        mNbQuestionText = findViewById(R.id.nbQuestionText);
        mSaveAndExitButton = findViewById(R.id.saveButton);

        mNbQuestionText.setText(String.valueOf(getIntent().getIntExtra("NBQUESTION", 4)));


        mSaveAndExitButton.setOnClickListener(v -> {
            if(mNbQuestionText.length() != 0) {
                Bundle bundle = new Bundle();
                bundle.putInt("PARAM_NUMBER_OF_QUESTION", Integer.valueOf(mNbQuestionText.getText().toString()));
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
