package android.com.progmobile.Controller;

import android.com.progmobile.Model.QuestionBank;
import android.com.progmobile.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    private LinearLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mLayout = findViewById(R.id.history_layout);
        QuestionBank.getScoreMap().forEach(this::createHistory);
    }

    private void createHistory(String word, ArrayList<Integer> score) {
        TextView text = new TextView(this);
        text.setText(String.format("%s : %s/%s", word, score.get(0), score.get(0) + score.get(1)));
        text.setTextSize(30);
        mLayout.addView(text,mParams);
    }
}
