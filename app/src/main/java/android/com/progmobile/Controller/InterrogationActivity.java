package android.com.progmobile.Controller;

import android.com.progmobile.Model.MyDictionary;
import android.com.progmobile.Model.Question;
import android.com.progmobile.Model.QuestionBank;
import android.com.progmobile.Model.Word;
import android.com.progmobile.R;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class InterrogationActivity extends AppCompatActivity {

    private int mQuestionIndex = 0;
    private String mCorrectAnswer;
    private int mCorrectScore, mWrongScore = 0;
    private int mFinalScore;
    private int mNbQuestion;
    private ArrayList<Integer> mScore;

    private TextView mQuestionText;
    private EditText mPlayersAnswer;
    private Button mCheckButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interrogation);

        mQuestionText = findViewById(R.id.question_text);
        mPlayersAnswer = findViewById(R.id.player_answer);
        mCheckButton = findViewById(R.id.check_answer_button);
        mScore = new ArrayList<>();

        mNbQuestion = getIntent().getIntExtra("NUMBER_OF_QUESTION", 4);

        mCheckButton.setOnClickListener(v -> {
            HashMap<String, ArrayList<Integer>> hsh = QuestionBank.getScoreMap();

            mScore = hsh.get(mCorrectAnswer);

            if(mPlayersAnswer.getText().toString().toLowerCase().equals(mCorrectAnswer.toLowerCase())) {
                mCorrectScore++;
                mScore.set(0, mScore.get(0)+1);
                nextQuestion();
            }
            else {
                mWrongScore++;
                mScore.set(1, mScore.get(1)+1);
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                wrongAnswerPopUp("La réponse correcte est : " + mCorrectAnswer);
            }


        });

        generateQuestion();
        displayQuestion(QuestionBank.getQuestionList().get(mQuestionIndex));
    }

    private void nextQuestion() {
        mPlayersAnswer.getText().clear();
        if(mQuestionIndex < QuestionBank.getQuestionList().size() && mQuestionIndex < mNbQuestion) {
            displayQuestion(QuestionBank.getQuestionList().get(mQuestionIndex));
        } else {
            Toast.makeText(getApplicationContext(), "FINISHED", Toast.LENGTH_LONG).show();
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            mFinalScore = mCorrectScore + mWrongScore;
            addPopUp("Votre score finale est = " + mCorrectScore + "/" + mFinalScore, inflater);

        }
    }

    private void generateQuestion() {
        QuestionBank.getQuestionList().clear();
        while(mQuestionIndex < mNbQuestion && mQuestionIndex < MyDictionary.INSTANCE().size()) {
            Random rand = new Random();
            int index = rand.nextInt(MyDictionary.INSTANCE().size());
            Question question = new Question(MyDictionary.INSTANCE().get(index).getWord());
            if(!QuestionBank.containsQuestionWithWord(question.getQuestion())){
                QuestionBank.getQuestionList().add(question);
                mQuestionIndex++;
            }
        }
        mQuestionIndex = 0;
    }

    private void displayQuestion(Question question) {
        mQuestionText.setText(String.format("%s?", question.getQuestion()));
        for (Word word : MyDictionary.INSTANCE()) {
            if(question.getQuestion().equals(word.getWord()))
                mCorrectAnswer = word.getTranslate();
        }
        mQuestionIndex++;
    }

    public void addPopUp(String text, LayoutInflater inflater) {
        View popupView = inflater.inflate(R.layout.score_popup_window, null);
        PopupWindow pw = setPopUpProperties(text, popupView, R.id.score_popup_text, true);
        Button okBtn = pw.getContentView().findViewById(R.id.scoreOkBtn);
        okBtn.setOnClickListener(v -> {
            pw.dismiss();
            finish();
        });
    }

    private void wrongAnswerPopUp(String text) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.wrong_answer_popup_window, null);
        PopupWindow pw = setPopUpProperties(text, popupView, R.id.correct_answer_popup_text, false);
        Button okBtn = pw.getContentView().findViewById(R.id.next_question_button);
        okBtn.setOnClickListener(v -> {
            pw.dismiss();
            nextQuestion();
        });
        Button raisonBtn = pw.getContentView().findViewById(R.id.raison_button);
        raisonBtn.setOnClickListener(v -> {
            pw.dismiss();
            mWrongScore--;
            mCorrectScore++;
            mScore.set(1, mScore.get(1)-1);
            mScore.set(1, mScore.get(0)+1);
            nextQuestion();
        });
    }

    private PopupWindow setPopUpProperties(String text, View popupView, int resource, boolean focusable) {
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 50);
        ((TextView)popupWindow.getContentView().findViewById(resource)).setText(text);
        return popupWindow;
    }

}
