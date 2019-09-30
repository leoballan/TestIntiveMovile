package com.vila.testmobileintive.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.vila.testmobileintive.R;

import com.vila.testmobileintive.viewmodel.MainViewModel;


public class MainActivity extends AppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Window w = getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        setContentView(R.layout.activity_main);
        setInmwersiveMode();

        init(savedInstanceState);
    }



    private void init(Bundle savedInstanceState)
    {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getPersonLD();


        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.mainActivity_contenedor,ListFragment.newInstance(),"ListFragment")
                    .commitNow();
        }



    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
        {
            setInmwersiveMode();
        }
    }


    private void setInmwersiveMode()
    {
        View decorview = getWindow().getDecorView();

        decorview.setOnSystemUiVisibilityChangeListener((int visibility)->
        {
            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN)==0)
            {
                decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        });
    }
}
