package com.example.chaitanya.helpchatinstagram;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.chaitanya.helpchatinstagram.Fragments.InstagramLogin;
import com.example.chaitanya.helpchatinstagram.Fragments.UserProfileFragment;

public class MainActivity extends AppCompatActivity {




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InstagramLogin instagramLogin = new InstagramLogin();
        switchContent(R.id.activity_main, instagramLogin);
    }


    public void switchContentHistory(int id, Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(id, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void switchContent(int id, Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(id, fragment, fragment.toString());
        fragmentTransaction.commit();
    }

    public void showUser(String accessToken){
        UserProfileFragment userProfileFragment = new UserProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("accessToken", accessToken);
        userProfileFragment.setArguments(bundle);
        FragmentTransaction fm = getFragmentManager().beginTransaction().replace(R.id.activity_main,userProfileFragment);
        fm.commit();

    }

}

