package project.chasemvp.Model.Events;

/**
 * Created by Mehdi on 11/06/2017.
 */

public class ErrorEvent
{
    String error;
    public ErrorEvent(String e)
    {
        error = e;
    }
    public String getError()
    {
        return error;
    }
}
