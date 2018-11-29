package project.chasemvp.UI.UI;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import project.chasemvp.R;
import project.chasemvp.UI.Activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

/**
 * Created by Mehdi on 11/06/2017.
 */

@RunWith(JUnit4.class)
@LargeTest
public class MainActivityTest
{
    @Rule
    public final ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void CheckToolbar()
    {
        onView(withId(R.id.chases_fragment_recyclerview)).check(matches(not(isDisplayed())));
    }

}
