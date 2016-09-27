package edu.orangecoastcollege.cs273.gbyers.flagquiz;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Handler;

/**
 * A placeholder fragment containing a simple view.
 */
public class QuizActivityFragment extends Fragment {

    private static final String TAG = "FlagQuiz Activity";
    private static final int FLAGS_IN_QUIZ = 10;

    private List<String> fileNameList; //flag file names
    private List<String> quizCountriesList; //countries in current quiz
    private Set<String> regionSet;
    private String correctAnswer;
    private int guessRows;
    private int totalGuesses;
    private int correctAnswers;
    private SecureRandom random;
    private Handler handler;

    private TextView questionNumberTextView;
    private ImageView flagImageView;
    private LinearLayout[] guessLinearLayout;
    private TextView answerTextView;
    public QuizActivityFragment() {
    }

    /**
     * configures the QuizActivity Fragment when its view is created
     * @param inflater  the layout inflater
     * @param container the ViewGroup where the fragment resides
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


      View view =  inflater.inflate(R.layout.fragment_quiz, container, false);
        fileNameList = new ArrayList<>();
        quizCountriesList = new ArrayList<>();
        random = new SecureRandom();
        handler = new Handler();

        //get ref to GUI componenets
        questionNumberTextView = (TextView) view.findViewById(R.id.questionNumberTextView);
        flagImageView = (ImageView) view.findViewById(R.id.flagImageView);
        guessLinearLayout = new LinearLayout[4];
        guessLinearLayout[0] = (LinearLayout) view.findViewById(R.id.rowOneLinearLayout);
        guessLinearLayout[1] = (LinearLayout) view.findViewById(R.id.rowTwoLinearLayout);
        guessLinearLayout[2] = (LinearLayout) view.findViewById(R.id.rowThreeLinearLayout);
       // guessLinearLayout[3] = (LinearLayout) view.findViewById(R.id.rowFourLinearLayout);
        answerTextView = (TextView) view.findViewById(R.id.answerTextView);
        for(LinearLayout row: guessLinearLayout)
        {
            for(int column = 0; column < row.getChildCount(); column++)
            {
                Button button = (Button) row.getChildAt(column);
                button.setOnClickListener(guessButtonListener);
            }
        }
        //set questionNumberTextViews Text
        questionNumberTextView.setText(getString(R.string.question, 1, FLAGS_IN_QUIZ));
        return view; // return fragments view for display
    }
}
