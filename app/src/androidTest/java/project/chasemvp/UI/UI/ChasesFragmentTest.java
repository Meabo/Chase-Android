package project.chasemvp.UI.UI;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.FragmentTransaction;

import org.greenrobot.eventbus.EventBus;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import project.chasemvp.Model.Events.ChasesEvent;
import project.chasemvp.Model.Events.ErrorEvent;
import project.chasemvp.Model.POJO.Chase;
import project.chasemvp.R;
import project.chasemvp.UI.Activities.MainActivity;
import project.chasemvp.UI.Fragments.ChasesFragment;
import project.chasemvp.UI.Utils.RecyclerViewItemCountAssertion;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Created by Mehdi on 12/06/2017.
 */
@RunWith(JUnit4.class)
@LargeTest
public class ChasesFragmentTest
{
    @Rule
    public ActivityTestRule activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void fragment_can_be_instantiated()
    {

        activityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ChasesFragment voiceFragment = startChases();
            }
        });

    }

    @Test
    public void testShouldShowRecyclerViewOnNewPosts()
    {
        List<Chase> chases = new ArrayList<>();
        chases.add(new Chase("1", "Lol", 10));
        chases.add(new Chase("2", "Lol1", 100));
        chases.add(new Chase("3", "Lol2", 1000));
        chases.add(new Chase("4", "Lol3", 1000));
        chases.add(new Chase("6", "Lol4", 10000));

        EventBus.getDefault().post(new ChasesEvent(chases));
        onView(withId(R.id.chases_fragment_recyclerview)).check(matches(isDisplayed()));
        onView(withId(R.id.chases_fragment_recyclerview)).check(new RecyclerViewItemCountAssertion(chases.size()));
        onView(withId(R.id.chases_fragment_errorview)).check(matches(not(isDisplayed())));

    }

    @Test
    public void testShouldShowErrorViewOnNetworkError()
    {

        String message_expected = "No Data to display";
        EventBus.getDefault().post(new ErrorEvent(message_expected));

        onView(withId(R.id.chases_fragment_errorview)).check(matches(withText(message_expected)));
        onView(withId(R.id.chases_fragment_recyclerview)).check(matches(not(isDisplayed())));

    }



    private ChasesFragment startChases() {
        MainActivity activity = ( MainActivity) activityRule.getActivity();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        ChasesFragment chasesFragment = new ChasesFragment();
        transaction.add(chasesFragment , "chasesFragment ");
        transaction.commit();
        return chasesFragment ;
    }


}


