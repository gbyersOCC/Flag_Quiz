package edu.orangecoastcollege.cs273.gbyers.flagquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Set;

public class QuizActivity extends AppCompatActivity {

    public static final String CHOICES = "pref_numberOfChoices";
    public static final String REGIONS = "pref_regions";

    private boolean phoneDevice = true;
    private boolean preferencesChanged = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(preferencesChangeListener);

        //determine screen size
        int screenSize = getResources()
                .getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        //if device is a tablet, set phoneDevice to false
        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE || screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE)
            phoneDevice = false;

        //if runnnig on phone sized device, allow only portrait orientation
        if (phoneDevice)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    /**
     * onStart is called after onCreate completes execution
     * methof updates the number of guess rows to display and the regiond to choose flags from,
     * then resets the quiz with new preferences
     */
    @Override
    protected void onStart(){
        super.onStart();

        if(preferencesChanged)
        {
            //now that the default preferences have been set
            //initialize quizActivity and start the quiz
            QuizActivityFragment quizFragment = (QuizActivityFragment)
                    getSupportFragmentManager().findFragmentById(R.id.quizFragment);
            quizFragment.updateGuessRows(PreferenceManager.getDefaultSharedPreferences(this));
            quizFragment.updateRegions(PreferenceManager.getDefaultSharedPreferences(this));
            quizFragment.resetQuiz();
            preferencesChanged = false;
        }
    }

    /**
     * Shows Settings Menu ONLY if used by Phone or Portrait-orientation tablet
     * @param menu  the settings menu
     * @return true if Settings View was inflated, false otherwise
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    //get devices current orientation
        int orientation = getResources().getConfiguration().orientation;

        //display apps setngs menu only in portrait orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT){
            //inflate the menu
        getMenuInflater().inflate(R.menu.menu_quiz,menu);
        return true;
        }
        else
            return false;
    }

    /**
     * displays the SettingsActivity when running on phone or Portrait-oriented tablet.
     * Starts the activity by use of Intent(Noo data passed, because the shared preferences, preferences.xml,has all data necessary)
     * @param item the menu item
     * @return  true, if an optionItem was selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent preferenceIntent = new Intent(this, SettingsActivity.class);

        startActivity(preferenceIntent);

        return super.onOptionsItemSelected(item);
    }

    /**
     * Listener to handlle the Preference changes in the apps shared preferences (preferences.xml)
     * If either regions or guess (options) have changed the quiz will restart with those new set settings
     */
    private SharedPreferences.OnSharedPreferenceChangeListener preferencesChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener()
            //called when user changes apps preferences
    {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPeezees, String key) {
            preferencesChanged = true;

            QuizActivityFragment quizFrag = (QuizActivityFragment) getSupportFragmentManager().findFragmentById(R.id.quizFragment);

            if (key.equals(CHOICES))//# of coices to display has  to change
            {
                quizFrag.updateGuessRows(sharedPeezees);
                quizFrag.resetQuiz();
            } else if (key.equals(REGIONS))//Regions setting changed
            {
                Set<String> region = sharedPeezees.getStringSet(REGIONS, null);

                if (region != null && region.size() > 0) {
                    quizFrag.updateRegion(sharedPeezees);
                    quizFrag.resetQuiz();
                }
                else    //must select one region (select North America by default)
                {
                    SharedPreferences.Editor editor = sharedPeezees.edit();
                    region.add(getString(R.string.default_region));
                    editor.putStringSet(REGIONS, region);
                    editor.apply();

                    Toast.makeText(QuizActivity.this,R.string.default_region_message, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        Toast.makeText(QuizActivity.this,R.string.restarting_quiz,Toast.LENGTH_SHORT)
                .show();
        }


    };
}


