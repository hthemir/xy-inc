package context;

import android.content.Context;

/**
 * Created by Hugo on 09/12/2016.
 */
public class Contexto extends android.app.Application {
    private static Contexto mApp = null;
    /* (non-Javadoc)
     * @see android.app.Application#onCreate()
     */
    @Override
    public void onCreate()
    {
        super.onCreate();
        mApp = this;
    }
    public static Context context()
    {
        return mApp.getApplicationContext();
    }
}
