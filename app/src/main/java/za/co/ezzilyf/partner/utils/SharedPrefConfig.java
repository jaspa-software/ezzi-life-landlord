package za.co.ezzilyf.partner.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefConfig {
    private SharedPreferences sharedPreferences;

    private Context context;

    public SharedPrefConfig(Context context) {

        this.context = context;

        sharedPreferences = context.getSharedPreferences("za.co.ezzilyf.partner",
                Context.MODE_PRIVATE);
    }

    public void writeUserProfile(String uid, String displayName, String type, String  emailAddress, String mobileNumber ){

        SharedPreferences.Editor  editor = sharedPreferences.edit();

        editor.putString("uid",uid);

        editor.putString("displayName",displayName);

        editor.putString("type",type);

        editor.putString("emailAddress",emailAddress);

        editor.putString("mobileNumber",mobileNumber);

        editor.putString("uid",uid);

        editor.apply();
    }

    public String readUid(){

        String uid = "";

        uid = sharedPreferences.getString("uid","GUEST");

        return uid;
    }

    public String readDisplayName(){

        String displayName = "";

        displayName = sharedPreferences.getString("uid","GUEST");

        return displayName;
    }

}
