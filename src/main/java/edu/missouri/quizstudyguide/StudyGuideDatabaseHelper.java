package edu.missouri.quizstudyguide;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

//this class extends the SQLiteOpenHelper to be able to create the database for the app along with a getter to return a question object
public class StudyGuideDatabaseHelper extends SQLiteOpenHelper {

    //variables required for creating a database in sqlite
    private static final String database = "studyguide.db";
    private static final int version = 1;
    private SQLiteDatabase db;

    public StudyGuideDatabaseHelper(Context context) {
        super(context, database, null, version);
    }

    //method for creating the table in SQLite
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL( "CREATE TABLE " +
                StudyGuideContract.QuestionsTable.table + " ( " +
                StudyGuideContract.QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StudyGuideContract.QuestionsTable.column + " TEXT, " +
                StudyGuideContract.QuestionsTable.option_column + " TEXT, " +
                StudyGuideContract.QuestionsTable.option_column2 + " TEXT, " +
                StudyGuideContract.QuestionsTable.option_column3 + " TEXT, " +
                StudyGuideContract.QuestionsTable.answer + " INTEGER" + ")");
        configureTable();
    }

    //method for dropping table if a new one is implemented (using the method above)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StudyGuideContract.QuestionsTable.table);
        onCreate(db);
    }

    //define and create question objects to be injected into the database
    private void configureTable() {
        String question = "What does XML stand for?";
        ArrayList<String> answers = new ArrayList<String>();
        answers.add("Extended Mega Language");
        answers.add("Extensible Markup Language");
        answers.add("Extended Multi Language");
        int correctIndex = 2;
        Question question1 = new Question(question,answers,correctIndex);
        addQuestion(question1);

        question = "Which directory contains app resources?";
        answers.clear();
        answers.add("/app");
        answers.add("/file");
        answers.add("/res");
        correctIndex = 3;
        Question question2 = new Question(question,answers,correctIndex);
        addQuestion(question2);

        question = "What keyword prefix does a class need to restrict it from being inherited?";
        answers.clear();
        answers.add("final");
        answers.add("abstract");
        answers.add("interface");
        correctIndex = 1;
        Question question3 = new Question(question,answers,correctIndex);
        addQuestion(question3);

        question = "Which package is the View class located in?";
        answers.clear();
        answers.add("android.widget");
        answers.add("android.view");
        answers.add("java.util");
        correctIndex = 2;
        Question question4 = new Question(question,answers,correctIndex);
        addQuestion(question4);

        question = "Which method is used to disable a button?";
        answers.clear();
        answers.add("disable()");
        answers.add("isEnabled()");
        answers.add("setEnabled()");
        correctIndex = 3;
        Question question5 = new Question(question,answers,correctIndex);
        addQuestion(question5);
    }

    //inserts a value into the table in the database
    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        String questionPhrase = question.getQuestion();
        ArrayList<String> answers = question.getAnswers();
        int index = question.getCorrectIndex();
        cv.put(StudyGuideContract.QuestionsTable.column, questionPhrase);
        cv.put(StudyGuideContract.QuestionsTable.option_column, answers.get(0));
        cv.put(StudyGuideContract.QuestionsTable.option_column2, answers.get(1));
        cv.put(StudyGuideContract.QuestionsTable.option_column3, answers.get(2));
        cv.put(StudyGuideContract.QuestionsTable.answer, index);
        db.insert(StudyGuideContract.QuestionsTable.table, null, cv);
    }

    //returns the questions existing in the database
    public List<Question> getQuestions() {
        List<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + StudyGuideContract.QuestionsTable.table, null);

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                ArrayList<String> answers = new ArrayList<String>();
                answers.add(cursor.getString(cursor.getColumnIndex(StudyGuideContract.QuestionsTable.option_column)));
                answers.add(cursor.getString(cursor.getColumnIndex(StudyGuideContract.QuestionsTable.option_column2)));
                answers.add(cursor.getString(cursor.getColumnIndex(StudyGuideContract.QuestionsTable.option_column3)));
                question.setQuestion(cursor.getString(cursor.getColumnIndex(StudyGuideContract.QuestionsTable.column)));
                question.setAnswers(answers);
                question.setCorrectIndex(cursor.getInt(cursor.getColumnIndex(StudyGuideContract.QuestionsTable.answer)));
                questionList.add(question);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return questionList;
    }
}
