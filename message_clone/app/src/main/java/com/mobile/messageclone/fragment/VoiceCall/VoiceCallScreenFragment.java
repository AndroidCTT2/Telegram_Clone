package com.mobile.messageclone.fragment.VoiceCall;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mobile.messageclone.R;
import com.mobile.messageclone.Activity.ChatActivity;


public class VoiceCallScreenFragment extends DialogFragment {
    private ImageButton btnEndCall;
    private TextView txtName;
    private String name;
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    public Long callDuration = 0L;

    public static VoiceCallScreenFragment newInstance() {
        VoiceCallScreenFragment fragment = new VoiceCallScreenFragment("");
        return fragment;
    }

    public Chronometer getChronometer() {
        return chronometer;
    }

    public VoiceCallScreenFragment(String name) {
        this.name = name;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,
                android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        this.callDuration = SystemClock.elapsedRealtime() - chronometer.getBase();
    }

    public Long getCallDuration() {
        return this.callDuration;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_voice_call_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.btnEndCall = (ImageButton)view.findViewById(R.id.btnEndCall);
        this.txtName = (TextView)view.findViewById(R.id.txtNameVoiceCallScreen);
        this.btnEndCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseChronometer();
                ((ChatActivity)getActivity()).endVoiceCall();
                dismiss();
            }
        });
        this.txtName.setText(name);
        chronometer = (Chronometer)view.findViewById(R.id.chronometer);
        chronometer.setFormat("%m:%s");
        chronometer.setBase(SystemClock.elapsedRealtime());
    }

    public void setName(String name) {
        this.name = name;
    }
    public void startChronometer() {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }
    public void pauseChronometer() {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    public void resetChronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    public void turnOffTheClock() {
        pauseChronometer();
        resetChronometer();
    }

    public void turnOnTheClock() {
        startChronometer();
    }

}