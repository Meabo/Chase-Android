package project.chasemvp.Application.Dagger.Modules;

import android.content.Context;

import com.squareup.picasso.Picasso;

import java.net.URISyntaxException;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.socket.client.IO;
import io.socket.client.Socket;
import project.chasemvp.Model.SocketModel;
import project.chasemvp.Utils.Constants;

/**
 * Created by Mehdi on 14/06/2017.
 */
@Module(includes = {PicassoModule.class, AppModule.class})
public class SocketModule
{

    @Provides
    @Named("socketUrl")
    String provideServerUrl() {
        return Constants.url;
    }

    @Provides
    @Singleton
    @Named("socket")
    Socket providesSocket(@Named("socketUrl") String socketUrl)
    {
        try {
            return IO.socket(socketUrl);
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Provides
    @Singleton
    SocketModel providesSocketModel(@Named("socket") Socket socket, @Named("picasso") Picasso picasso, Context context)
    {
        return new SocketModel(socket, picasso, context);
    }

}
