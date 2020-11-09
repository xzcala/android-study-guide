package edu.missouri.quizstudyguide;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizStartedActivity extends AppCompatActivity {

    // view components
    private TextView headerTextView;
    private TextView timerTextView;
    private TextView timerTextView2;
    private TextView questionTextView;
    private TextView quizSection;
    private TextView quizSection2;
    private TextView quizSection3;
    private TextView quizSection4;
    private TextView quizSection5;
    private RadioGroup optionsRadioGroup;
    private RadioButton answerRadioButton1;
    private RadioButton answerRadioButton2;
    private RadioButton answerRadioButton3;
    private Button nextButton;
    private ColorStateList defaultRadioButtonColor; //for saving and resetting color of text
    private ColorStateList defaultSectionTextColor;
    private ColorStateList selectedSectionTextColor;

    //timer variables
    private CountDownTimer countDownTimer;
    private static final long COUNTDOWN_MILLIS = 180000; //to do: make timer be a user input?, currently set to 3 minutes
    private long timeLeftMillis;
    private int minutes;
    private int seconds;

    //quiz variables
    private List<Question> questionList;
    private Question currentQuestion;
    private int questionIndex=0;
    private int questionCount;
    private int score;
    private boolean hasAnswer;  //to check if user has selected an option
    private boolean[] scoreCounter;
    private boolean[] attemptedCounter;
    private long exitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_started);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        headerTextView = findViewById(R.id.headerTextView);
        timerTextView = findViewById(R.id.timerTextView);
        timerTextView2 = findViewById(R.id.timerTextView2);
        questionTextView = findViewById(R.id.questionTextView);
        quizSection = findViewById(R.id.quizSection);
        quizSection2 = findViewById(R.id.quizSection2);
        quizSection3 = findViewById(R.id.quizSection3);
        quizSection4 = findViewById(R.id.quizSection4);
        quizSection5 = findViewById(R.id.quizSection5);
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup);
        answerRadioButton1 = findViewById(R.id.answerRadioButton1);
        answerRadioButton2 = findViewById(R.id.answerRadioButton2);
        answerRadioButton3 = findViewById(R.id.answerRadioButton3);
        nextButton = findViewById(R.id.nextButton);

        //save initial color
        defaultRadioButtonColor = answerRadioButton1.getTextColors();
        selectedSectionTextColor = quizSection.getTextColors();
        defaultSectionTextColor = quizSection2.getTextColors();

        //call on database helper to get list of questions
        StudyGuideDatabaseHelper dbHelper = new StudyGuideDatabaseHelper(this);
        questionList = dbHelper.getQuestions();
        Collections.shuffle(questionList);
        questionCount = questionList.size();
        scoreCounter = new boolean[questionList.size()+1];
        attemptedCounter = new boolean[questionList.size()+1];


        //show first question and start countdown
        startCountdown();
        showQuestion();

        //navigation and score tracking
        quizSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionIndex=0;
                updateNavigation();
                showQuestion();
            }
        });
        quizSection2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionIndex=1;
                updateNavigation();
                showQuestion();
            }
        });
        quizSection3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionIndex=2;
                updateNavigation();
                showQuestion();
            }
        });
        quizSection4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionIndex=3;
                updateNavigation();
                showQuestion();
            }
        });
        quizSection5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionIndex=4;
                updateNavigation();
                showQuestion();
            }
        });

        //button functionality: checks if user selected an option, confirm the answer and proceed afterwards
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasAnswer == false) {
                    if (answerRadioButton1.isChecked() || answerRadioButton2.isChecked() || answerRadioButton3.isChecked()) {
                        attemptedCounter[questionIndex] = true;
                        confirmAnswer();
                    } else {
                        Toast.makeText(QuizStartedActivity.this, "Select an answer before proceeding", Toast.LENGTH_LONG).show();
                    }
                } else {
                    updateNavigation();
                    showQuestion();
                }
            }
        });
    }

    //controller of the navigation
    private void updateNavigation() {

        if(questionIndex < questionCount){
            //update header
            headerTextView.setText("Question " + (1+questionIndex) + "/" +questionCount);
            if (scoreCounter[questionIndex+1]!=false){
                currentQuestion = questionList.get(questionIndex);
                switch(currentQuestion.getCorrectIndex()){
                    case 1:
                        answerRadioButton1.setTextColor(selectedSectionTextColor);
                        answerRadioButton1.setChecked(true);
                        answerRadioButton2.setTextColor(Color.RED);
                        answerRadioButton3.setTextColor(Color.RED);
                        break;
                    case 2:
                        answerRadioButton2.setTextColor(selectedSectionTextColor);
                        answerRadioButton2.setChecked(true);
                        answerRadioButton1.setTextColor(Color.RED);
                        answerRadioButton3.setTextColor(Color.RED);
                        break;
                    case 3:
                        answerRadioButton3.setTextColor(selectedSectionTextColor);
                        answerRadioButton3.setChecked(true);
                        answerRadioButton2.setTextColor(Color.RED);
                        answerRadioButton1.setTextColor(Color.RED);
                        break;
                }
            } else {
                if ( attemptedCounter[questionIndex+1] == true) {
                    answerRadioButton1.setEnabled(false);
                    answerRadioButton2.setEnabled(false);
                    answerRadioButton3.setEnabled(false);
                    nextButton.setEnabled(false);
                    currentQuestion = questionList.get(questionIndex);
                    switch(currentQuestion.getCorrectIndex()){
                        case 1:
                            answerRadioButton1.setTextColor(selectedSectionTextColor);
                            answerRadioButton2.setTextColor(Color.RED);
                            answerRadioButton3.setTextColor(Color.RED);
                            break;
                        case 2:
                            answerRadioButton2.setTextColor(selectedSectionTextColor);
                            answerRadioButton1.setTextColor(Color.RED);
                            answerRadioButton3.setTextColor(Color.RED);
                            break;
                        case 3:
                            answerRadioButton3.setTextColor(selectedSectionTextColor);
                            answerRadioButton2.setTextColor(Color.RED);
                            answerRadioButton1.setTextColor(Color.RED);
                            break;
                    }
                } else {
                    //set color of buttons to default and clear radiogroup selection when
                    answerRadioButton1.setEnabled(true);
                    answerRadioButton2.setEnabled(true);
                    answerRadioButton3.setEnabled(true);
                    nextButton.setEnabled(true);
                    answerRadioButton1.setTextColor(defaultRadioButtonColor);
                    answerRadioButton2.setTextColor(defaultRadioButtonColor);
                    answerRadioButton3.setTextColor(defaultRadioButtonColor);
                    optionsRadioGroup.clearCheck();
                }
            }
        }

        quizSection.setTextColor(defaultSectionTextColor);
        quizSection2.setTextColor(defaultSectionTextColor);
        quizSection3.setTextColor(defaultSectionTextColor);
        quizSection4.setTextColor(defaultSectionTextColor);
        quizSection5.setTextColor(defaultSectionTextColor);
        switch (questionIndex) {
            case 0:
                quizSection.setTextColor(selectedSectionTextColor);
                break;
            case 1:
                quizSection2.setTextColor(selectedSectionTextColor);
                break;
            case 2:
                quizSection3.setTextColor(selectedSectionTextColor);
                break;
            case 3:
                quizSection4.setTextColor(selectedSectionTextColor);
                break;
            case 4:
                quizSection5.setTextColor(selectedSectionTextColor);
                break;
        }
    }

    //method for populating the question and its options
    private void showQuestion() {
        //populate question and options, else submit and show score if on last question
        if (questionIndex < questionCount) {
            //set answer toggle to false to be able to check the answer
            hasAnswer = false;
            if (timeLeftMillis>0) {
                nextButton.setText("Check Answer");
            }

            //set view to question and options
            currentQuestion = questionList.get(questionIndex);
            ArrayList<String> answers = currentQuestion.getAnswers();
            questionTextView.setText(currentQuestion.getQuestion());
            answerRadioButton1.setText(answers.get(0));
            answerRadioButton2.setText(answers.get(1));
            answerRadioButton3.setText(answers.get(2));
        } else {
            //show score if at the end and submit
            String totalScore = "Score: " + score + "/" + questionCount;
            Toast.makeText(QuizStartedActivity.this, totalScore, Toast.LENGTH_LONG).show();
            submitQuiz();
        }
        //increase index for next question
        questionIndex = questionIndex + 1;
    }

    //method initiate and start countdown
    private void startCountdown() {
        timeLeftMillis = COUNTDOWN_MILLIS;
        countDownTimer = new CountDownTimer(timeLeftMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillis = millisUntilFinished;
                updateTimerTextView();
            }

            @Override
            public void onFinish() {
                timeLeftMillis = 0;
                updateTimerTextView();
                String totalScore = "Score: " + score + "/" + questionCount;
                Toast.makeText(QuizStartedActivity.this, totalScore, Toast.LENGTH_LONG).show();
                nextButton.setEnabled(false);
                nextButton.setText("Times up!");
                timerTextView2.setText("Remaining  |   Score: "+score+"/"+questionCount);
                answerRadioButton1.setEnabled(false);
                answerRadioButton2.setEnabled(false);
                answerRadioButton3.setEnabled(false);
            }
        }.start();
    }

    //update the timer view
    private void updateTimerTextView() {
        minutes = (int) (timeLeftMillis/1000) / 60;
        seconds = (int) (timeLeftMillis/1000) % 60;

        if(seconds < 10) {
            timerTextView.setText("" + minutes + ":0" + seconds);
        } else {
            timerTextView.setText("" + minutes + ":" + seconds);
        }
        if(timeLeftMillis < 60000) {
            timerTextView.setTextColor(Color.RED);
        }
    }

    //checks if answer is correct
    private void confirmAnswer() {

        //toggle answer variable to true to allow viewing of next question
        hasAnswer = true;

        //compare selected option with correct option and increase score if so
        RadioButton selectedOption = findViewById(optionsRadioGroup.getCheckedRadioButtonId());
        int optionIndex = optionsRadioGroup.indexOfChild(selectedOption) + 1;
        if (optionIndex == currentQuestion.getCorrectIndex() && scoreCounter[questionIndex] == false) {
            score++;
            scoreCounter[questionIndex] = true;
            Toast.makeText(QuizStartedActivity.this, "Score increased!", Toast.LENGTH_SHORT).show();
        } /*else if (optionIndex != currentQuestion.getCorrectIndex()) {
            Toast.makeText(QuizStartedActivity.this, "Incorrect!", Toast.LENGTH_SHORT).show();
        }*/

        //show correct answer
        showCorrectAnswer();
    }

    //switch case that compares user input with correct answer and updates view to show correct answer
    private void showCorrectAnswer() {
        switch (currentQuestion.getCorrectIndex()) {
            case 1:
                answerRadioButton1.setTextColor(Color.GREEN);
                answerRadioButton2.setTextColor(Color.RED);
                answerRadioButton3.setTextColor(Color.RED);
                break;
            case 2:
                answerRadioButton2.setTextColor(Color.GREEN);
                answerRadioButton1.setTextColor(Color.RED);
                answerRadioButton3.setTextColor(Color.RED);
                break;
            case 3:
                answerRadioButton3.setTextColor(Color.GREEN);
                answerRadioButton1.setTextColor(Color.RED);
                answerRadioButton2.setTextColor(Color.RED);
                break;
        }

        if (questionIndex == questionCount) {
            nextButton.setText("Finish");
        } else {
            nextButton.setText("Next");
        }
    }

    //prompts the user to press back twice to exit to avoid accidentally leaving quiz on back key
    @Override
    public void onBackPressed() {
        //2.5 sec buffer time when back is pressed first
        if (exitTime + 2500 > System.currentTimeMillis()) {
            submitQuiz();
        } else {
            Toast.makeText(QuizStartedActivity.this, "Press back again to submit and finish.", Toast.LENGTH_SHORT).show();
        }
        exitTime = System.currentTimeMillis();
    }

    //method for finishing quiz and the associated activity
    private void submitQuiz(){
        finish();
    }

    //end the timer when activity is over
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
