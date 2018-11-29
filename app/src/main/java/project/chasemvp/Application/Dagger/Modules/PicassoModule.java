package project.chasemvp.Application.Dagger.Modules;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.util.concurrent.Executors;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mehdi on 12/07/2017.
 */
@Module(includes = {AppModule.class})
public class PicassoModule
{
    @Provides
    @Singleton
    @Named("picasso")
    Picasso providesPicasso(Context context)
    {
        Picasso picasso = new Picasso.Builder(context)
                .executor(Executors.newSingleThreadExecutor())
                .indicatorsEnabled(true)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception e) {
                        Log.e("Picasso", "Failed to load image:" + uri);
                    }
                })
                .build();

        return picasso;
    }
}
