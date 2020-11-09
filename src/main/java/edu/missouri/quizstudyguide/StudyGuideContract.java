package edu.missouri.quizstudyguide;

import android.provider.BaseColumns;

//model for the database table
public final class StudyGuideContract {

    private StudyGuideContract() {}

    //implement basecolumns to apply an ID to the table,
    public static class QuestionsTable implements BaseColumns {
        //table definitions
        public static final String table = "questions";
        public static final String column = "question";
        public static final String option_column = "option1";
        public static final String option_column2 = "option2";
        public static final String option_column3 = "option3";
        public static final String answer = "answer";
    }
}
