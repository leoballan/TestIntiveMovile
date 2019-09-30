package com.vila.testmobileintive.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;

import com.vila.testmobileintive.R;


public class DialogoSimple extends AppCompatDialogFragment
{
    private TextView titulo;
    private TextView mensaje;
    private Button btRetry, btOk ;
    private OnDiagoloSimpleListener listener;

    private final static String ARGS_TITULO = "TITULO";
    private final static String ARGS_MENSAJE = "MENSAJE";


    public interface OnDiagoloSimpleListener
    {
        void clickOK();
    }

    public void setOnDiagoloSimpleListener(OnDiagoloSimpleListener listener)
    {this.listener = listener;}


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Dialog dialogo = super.onCreateDialog(savedInstanceState);

        if (dialogo.getWindow()!= null)
        {
            dialogo.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialogo.setCancelable(false);
            dialogo.setCanceledOnTouchOutside(false);
            dialogo.getWindow().getAttributes().windowAnimations = R.style.Dialog_Animation;
            if (getActivity() != null)
                dialogo.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.fondo_dialogo_simple));
        }

        dialogo.setOnKeyListener((dialogInterface, keyCode, keyEvent) ->
        {
            if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP)
            {
                Toast.makeText(getActivity(),getString(R.string.dialogo_simple_message_backpress)
                        ,Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        dialogo.setOnCancelListener(dialogInterface ->
                Toast.makeText(getActivity(),"Por favor presione OK"
                        ,Toast.LENGTH_SHORT).show());
        return dialogo;

    }


    public static DialogoSimple newInstance(String titulo, String mensaje)
    {
        DialogoSimple dialogo = new DialogoSimple();
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_TITULO,titulo);
        bundle.putString(ARGS_MENSAJE,mensaje);
        dialogo.setArguments(bundle);
        return dialogo;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.dialogo_simple,container,false);
        btRetry = view.findViewById(R.id.dialogo_simple_bt_retry);
        titulo = view.findViewById(R.id.dialogo_simple_titulo);
        mensaje = view.findViewById(R.id.dialogo_simple_mensaje);
        btOk = view.findViewById(R.id.dialogo_simple_bt_Ok);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments()!=null)
        {
            titulo.setText(getArguments().getString(ARGS_TITULO));
            mensaje.setText(getArguments().getString(ARGS_MENSAJE));
        }

        mensaje.setTextColor(Color.BLACK);
        titulo.setTextColor(Color.BLACK);

        btRetry.setOnClickListener(view1 ->
        {
            if (listener != null)
            {
                listener.clickOK();
            }
            dismiss();
        });

        btOk.setOnClickListener(view2 ->   dismiss());
    }


}
