package sandhu.harman.singleproject.parent;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by PortDesk on 13-Sep-17.
 */

public class ToastClass {
    public static void ToastMessage(Context context, Object msg)

    {
        if (msg != null) {
            Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
