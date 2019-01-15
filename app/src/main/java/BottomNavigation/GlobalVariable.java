package BottomNavigation;

import android.app.Application;

/**
 * Created by OK on 4/27/2018.
 */

public class GlobalVariable extends Application {
    private String cEmail;

    public String getcEmail() {
        return cEmail;
    }

    public void setcEmail(String cEmail) {
        this.cEmail = cEmail;
    }
}
