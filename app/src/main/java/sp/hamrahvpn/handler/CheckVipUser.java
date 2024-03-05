package sp.hamrahvpn.handler;

import android.content.Intent;
import android.widget.Toast;

import sp.hamrahvpn.R;
import sp.hamrahvpn.ui.LoginActivity;
import sp.hamrahvpn.ui.MainActivity;
import sp.hamrahvpn.util.Data;

public class CheckVipUser {

    public static void checkInformationUser(MainActivity context) {
        String uL = Data.appValStorage.getString("usernameLogin", null);
        String uU = Data.appValStorage.getString("usernamePassword", null);

        CheckLoginFromApi.checkIsLogin(
                context,
                uL,
                uU,
                (getApi, v) -> {
                    try {
                        if (!getApi) {
                            Toast.makeText(context, "اشتراک شما به پایان رسیده است", Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context, LoginActivity.class));
                            context.overridePendingTransition(R.anim.fade_in_1000, R.anim.fade_out_500);
                            context.finish();
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "مشکلی در بررسی وجود دارد", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
