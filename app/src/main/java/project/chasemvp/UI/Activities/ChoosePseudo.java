package project.chasemvp.UI.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;
import mehdi.sakout.fancybuttons.FancyButton;
import project.chasemvp.Application.ChaseApplication;
import project.chasemvp.Model.MySharedPreferences;
import project.chasemvp.Model.POJO.Avatar;
import project.chasemvp.Model.POJO.Chaser;
import project.chasemvp.Presenters.ChoosePseudoPresenter;
import project.chasemvp.R;
import project.chasemvp.Utils.Adapters.AvatarAdapter;

import static project.chasemvp.Utils.Constants.debug;

/**
 * Created by Mehdi on 22/06/2017.
 */

public class ChoosePseudo extends AppCompatActivity implements DiscreteScrollView.OnItemChangedListener
{
    @BindView(R.id.choose_pseudo_edit) EditText choose_pseudo;
    @BindView(R.id.choose_pseudo_next) FancyButton button_next;
    @BindView(R.id.avatar_picker) DiscreteScrollView avatar_picker;


    @Inject
    ChoosePseudoPresenter choosePseudoPresenter;

    @Inject
    MySharedPreferences pref;

    String pseudo;
    private InfiniteScrollAdapter infiniteAdapter;
    AvatarAdapter avatarAdapter;
    List<Avatar> data;
    int pos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_pseudo);
        ButterKnife.bind(this);
        ((ChaseApplication) getApplication()).getAppComponent().inject(this);
        FastPseudo();
        InitAvatarPicker();


    }
    private void InitAvatarPicker()
    {

        choosePseudoPresenter.getAvatars();
        avatarAdapter = new AvatarAdapter();
        infiniteAdapter = InfiniteScrollAdapter.wrap(avatarAdapter);
        avatar_picker.setAdapter(infiniteAdapter);
        //avatar_picker.setItemTransitionTimeMillis(DiscreteScrollViewOptions.getTransitionTime());
        avatar_picker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
        avatar_picker.addOnItemChangedListener(this);




    }
    private void FastPseudo()
    {
        if (pref.get("id") != null)
        {
            choosePseudoPresenter.getSocketModel().init(pref.get("pseudo"), pref.get("id"), pref.get("avatar_url"));
           // Discover();
            BeginMainActivity();
        }
    }

    @OnClick(R.id.choose_pseudo_next)
    public void validate_pseudo()
    {
        pseudo = choose_pseudo.getText().toString();
        if (pseudo.matches(""))
            choose_pseudo.setError("Pseudo is empty !");
        else
        {
            choosePseudoPresenter.checkpseudo(pseudo);
            button_next.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void BeginMainActivity()
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void Discover()
    {
        Intent i = new Intent(this, Discover.class);
        startActivity(i);
        finish();

    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(List<Avatar> l)
    {
        data = l;
        avatarAdapter.addAvatars(data);
        onItemChanged(data.get(0));
    }


    @Subscribe
    public void onEvent(Bitmap bt)
    {
        Log.d(debug, "OnEvent bitmap");
        try
        {
            Paper.book().write("avatar", bt);
            Discover();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Chaser c)
    {
       // Log.d(debug, sucessEvent.isSuccess() +"");
        if (c != null)
        {
            pref.put("id", c.getId());
            pref.put("pseudo", c.getPseudo());
            pref.put("level", c.getLevel());
            pref.put("xp", c.getXp());

            pref.put("avatar_id", data.get(pos).getId());
            pref.put("avatar_url", data.get(pos).getUrl_image());
            Log.d(debug, "Chaser is saved in Pref");
            choosePseudoPresenter.getSocketModel().init(pref.get("pseudo"), pref.get("id"), pref.get("avatar_url"));
            try
            {
                choosePseudoPresenter.saveImage(data.get(pos).getUrl_image(), this);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
           // Discover();
        }
        else
        {
            choose_pseudo.setError("Pseudo already taken");
            choose_pseudo.setText("");
            button_next.setVisibility(View.VISIBLE);

        }
    }

    private void onItemChanged(Avatar a)
    {
        /*currentItemName.setText(item.getName());
        currentItemPrice.setText(item.getPrice());
        changeRateButtonState(item);*/
    }


    @Override
    public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int position) {
        int positionInDataSet = infiniteAdapter.getRealPosition(position);
        onItemChanged(data.get(positionInDataSet));
        pos = positionInDataSet;
    }
}
