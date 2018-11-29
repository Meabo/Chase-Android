package project.chasemvp.Presenters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import project.chasemvp.Model.ChasesAPI;
import project.chasemvp.Model.POJO.Chase;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Mehdi on 12/06/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Observable.class, AndroidSchedulers.class})
public class ChasesPresenterTest
{
    private ChasesPresenter chasesPresenter;
    Observable<List<Chase>> chasesObservable;
    ChasesAPI.ChaseService chaseService;

    @Before
    public void setUp() throws Exception
    {
        chasesPresenter = spy(new ChasesPresenter(mock(ChasesAPI.class)));
        chasesObservable = (Observable<List<Chase>>) mock(Observable.class);
    }


    @Test
    public void testShouldScheduleChasesLoadFromAPIOnBackgroundThread()
    {

        //create mocks
       //  chasesObservable = (Observable<List<Chase>>) mock(Observable.class);
        //define return values
       // when(chasesPresenter.mChasesAPI.getChaseService().getChasesList()).thenReturn(chasesObservable);
        Chase c = new Chase("10", "Momo", 10);
        Chase a = new Chase("10", "Momo", 10);
        List<Chase> l = new ArrayList<>();
        l.add(c);
        l.add(a);
        chasesObservable = Observable.just(l);

        when(chaseService.getChasesList()).thenReturn(chasesObservable);
        when(chasesObservable.subscribeOn(Schedulers.io())).thenReturn(chasesObservable);
//        when(chasesObservable.observeOn(AndroidSchedulers.mainThread())).thenReturn(chasesObservable);

        //call test method
       // chasesPresenter.loadChasesFromAPI();

        //verify if all methods in the chain are called with correct arguments
      //  verify(chasesPresenter.getChasesAPI().getChaseService()).getChasesList();
        verify(chasesObservable).subscribeOn(Schedulers.io());
        verify(chasesObservable).observeOn(AndroidSchedulers.mainThread());
    }
    @Test
    public void testIntegrityOfChasesAPI()
    {

        //TestSubscriber<Chase> testSubscriber = new TestSubscriber<>();
        TestObserver<List<Chase>> testSubscribe =  chasesObservable.test(); //subscribe(testSubscriber);
        testSubscribe.assertSubscribed();
        testSubscribe.assertNoErrors();
        testSubscribe.onNext(Matchers.anyList());
        //testSubscriber.assertReceivedOnNext(Arrays.asList(1));

    }
}
