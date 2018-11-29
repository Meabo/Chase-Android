package project.chasemvp.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.chasemvp.Application.ChaseApplication;
import project.chasemvp.Model.Events.ChasesEvent;
import project.chasemvp.Model.Events.ErrorEvent;
import project.chasemvp.Presenters.ChasesPresenter;
import project.chasemvp.R;
import project.chasemvp.Utils.Adapters.ChaseAdapter;
import project.chasemvp.Utils.Adapters.RecyclerItemClickListener;

import static project.chasemvp.Utils.Constants.debug;

/**
 * Created by Mehdi on 11/06/2017.
 */


public class ChasesFragment extends Fragment
{
    @BindView(R.id.chases_fragment_recyclerview) RecyclerView mRecyclerView;
    @BindView (R.id.chases_fragment_errorview) TextView errorView;
    @BindView (R.id.chases_swipe_container) SwipeRefreshLayout swipeLayout;
    @Inject ChasesPresenter chasesPresenter;
    ChaseAdapter chaseAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ((ChaseApplication) getActivity().getApplication()).getAppComponent().inject(this);
        //getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
        chasesPresenter.loadChasesFromAPI();
    }

    private void InitListeners()
    {
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), mRecyclerView , new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position)
                    {

                        DetailFragment df = new DetailFragment(chaseAdapter.getChases_list().get(position));
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_container, df, "detail")
                                .commit();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));

        swipeLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh()
                    {
                        if (!swipeLayout.isRefreshing()) swipeLayout.setRefreshing(true);

                        Log.d(debug, "OnRefreshRecyclerView");
                        chasesPresenter.loadChasesFromAPI();
                        //getActivity().getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, ChasesFragment.this);

                    }
                }
        );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.chases_fragment, container, false);
        ButterKnife.bind(this, view);
        InitRecyclerView();
        InitListeners();
        return view;
    }

    private void InitRecyclerView()
    {
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        chaseAdapter = new ChaseAdapter(getContext());
        mRecyclerView.setAdapter(chaseAdapter);
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

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ChasesEvent chasesEvent)
    {
        swipeLayout.setRefreshing(false);
        hideError();
        chaseAdapter.addChases(chasesEvent.getChases());
        //getActivity().getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, ChasesFragment.this);

    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ErrorEvent errorEvent)
    {
        showError(errorEvent.getError());
    }

    private void hideError()
    {
        mRecyclerView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    private void showError(String error)
    {
        mRecyclerView.setVisibility(View.GONE);
        errorView.setText(error);
        errorView.setVisibility(View.VISIBLE);
    }

}
