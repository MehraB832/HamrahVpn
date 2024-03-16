package sp.inetvpn.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sp.inetvpn.data.ApiData;
import sp.inetvpn.handler.VolleySingleton;

/**
 * by MehrabSp
 */
public class GetAllOpenVpn {

    public interface OpenVCallback {
        void onOpenVResult(Boolean retOpenV, String message);
    }

    private static Boolean checkStatus = false;
    private static JSONObject resFetch = null;
    private static String message = null;
    private static int OSID = -666;

    public static void getAllServers(Context context, OpenVCallback callback) {
        if (ApiData.UserToken == null) {
            callback.onOpenVResult(checkStatus, message);
            return;
        }

        VolleySingleton volleySingleton = new VolleySingleton(context);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("LanguageID", "");
            jsonBody.put("Query", "");
            jsonBody.put("Operand", "");
            jsonBody.put("PageNo", "");
            jsonBody.put("RowPerPage", "");
            jsonBody.put("SortIndex", "");
        } catch (Exception e) {
            message = "[ERROR] A89P";
            callback.onOpenVResult(checkStatus, message);
            return;
        }

        // Get OSID
        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, ApiData.ApiGetOSAddress, jsonBody,
                response -> {
                    Log.d("RES FROM OPEN", String.valueOf(response));

                    try {
                        if (response != null) {
                            resFetch = response;
                            OSID = extractOSID();

                            if (OSID != 666) {
                                checkInformation(context, callback);
                            }

                        } else {
                            callback.onOpenVResult(checkStatus, message);
                        }
                    } catch (Exception ignore) {
                        callback.onOpenVResult(checkStatus, message);
                    }
//                    finally {
//                        callback.onOpenVResult(checkStatus, message);
//                    }
                },
                error -> {
                    // Handle error
                    message = "[Error] from api";
                    callback.onOpenVResult(checkStatus, message);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + ApiData.UserToken);
                // Add other headers if needed
                return headers;
            }
        };
        volleySingleton.addToRequestQueue(sr);
    }

    private static int extractOSID() {
        int osID = -666;
        try {
            // آرایه‌ی Data را از JSON بدست می‌آوریم
            JSONArray dataArray = resFetch.getJSONArray("Data");

            // برای هر آیتم در آرایه Data
            for (int i = 0; i < dataArray.length(); i++) {
                // درآیتم فعلی
                JSONObject item = dataArray.getJSONObject(i);
                // اگر مقدار Name آن Android باشد
                if (item.getString("Name").equals("Android")) {
                    osID = item.getInt("OSID");
                    checkStatus = true;
                    break;
                }
            }
        } catch (JSONException e) {
            message = "[ERROR] json OSID";
        }

        return osID;
    }


    private static void checkInformation(Context context, OpenVCallback callback) {
        try {
            VolleySingleton volleySingleton = new VolleySingleton(context);

            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("LanguageID", "");
                jsonBody.put("Query", "");
                jsonBody.put("OSID", String.valueOf(OSID));
                jsonBody.put("Operand", "");
                jsonBody.put("PageNo", "");
                jsonBody.put("RowPerPage", "");
                jsonBody.put("SortIndex", "");
            } catch (Exception e) {
                message = "[ERROR] A89P";
                callback.onOpenVResult(checkStatus, message);
                return;
            }

            JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, ApiData.ApiGetServersAddress, jsonBody,
                    response -> {
                        Log.d("RES os sr", String.valueOf(response));
//                        checkLogin = saveInformation(response, token);
//                            callback.onLoginResult(checkLogin, message);
                    },
                    error -> {
                        // Handle error
                        //message = "[Error] from api";
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + ApiData.UserToken);
                    // Add other headers if needed
                    return headers;
                }
            };
            volleySingleton.addToRequestQueue(sr);

        } catch (Exception e) {
            message = "[Error] K85S";
        }
    }

//    private static boolean saveInformation(JSONObject resCheck, String token) {
//        boolean res = false;
//        if (resCheck != null && checkLogin) {
//            try {
//                // مقدار Status را دریافت می‌کنیم
//                int status = resCheck.getInt("Status");
//                if (status == 0) {
//                    //message = "";
//                    res = true;
//
//                    GlobalData.appValStorage.putString("basic_info", "basic_info");
//                    GlobalData.appValStorage.putString("first_login", "first_login"); //
//                    GlobalData.appValStorage.putString("first_connection", "first_connection"); //
//                    GlobalData.appValStorage.putString("nearest_exp_date", "nearest_exp_date"); //
//                    GlobalData.appValStorage.putString("user_id", "user_id");
//                    GlobalData.appValStorage.putString("expiration", "expiration");
//                    GlobalData.appValStorage.putString("token_user", token);
//                    GlobalData.appValStorage.putInt("days", 30);
//                    Log.d("R", "TRUE");
//                } else {
//                    Log.d("R", "status false");
//                }
//                // مقدار CreationTime را دریافت می‌کنیم
//                String creationTime = resCheck.getJSONArray("Data").getJSONObject(0).getString("CreationTime");
//                // مقدار ExternalUser را دریافت می‌کنیم
//                String externalUser = resCheck.getJSONArray("Data").getJSONObject(0).getString("ExternalUser");
//                Log.d("CT", creationTime);
//                Log.d("EU", externalUser);
//
//            } catch (JSONException e) {
//                //message = "[Error] from smart check";
//            }
//        } else {
//            Log.d("R", "false");
//        }
//        return res;
//    }

}