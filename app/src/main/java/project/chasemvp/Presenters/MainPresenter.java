package project.chasemvp.Presenters;

import javax.inject.Inject;

import project.chasemvp.UI.Interfaces.View.MainScreen;

/**
 * Created by Mehdi on 11/06/2017.
 */

public class MainPresenter
{
    @Inject
    public MainPresenter()
    {
    }

    public void OnShowPostsButtonClick(MainScreen mainScreen)
    {
        mainScreen.launchPostsActivity();
    }
}
