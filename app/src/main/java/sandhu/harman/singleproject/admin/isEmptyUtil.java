package sandhu.harman.singleproject.admin;

/**
 * Created by PortDesk on 15-Sep-17.
 */

public class isEmptyUtil {
    public static boolean isEmpty(String input) {
        boolean b = false;
        if (!input.isEmpty() && input != null && input != "") {
            b = true;
        }

        return b;
    }
}
