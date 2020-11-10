package com.example.rec_commend.ui.msearch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rec_commend.R;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import kotlin.text.UStringsKt;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MsearchFragment extends Fragment {

    private MsearchViewModel msearchViewModel;
    private ImageButton m_rec_btn;
    private ImageView m_rec_overlay;

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private boolean isRecording = false;
    final private static String RECORDED_FILE = "record.mp4";
    private MediaPlayer player;
    private MediaRecorder recorder;
    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) getActivity().finish();

    }

    private void startRecording(String path) {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setOutputFile(path);
        Log.println(Log.DEBUG, "Save to ", path);
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed : " + e.getMessage());
        }
        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        msearchViewModel =
                new ViewModelProvider(this).get(MsearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_msearch, container, false);
        m_rec_btn = root.findViewById(R.id.m_rec_btn);
        m_rec_overlay = root.findViewById(R.id.m_rec_overlay);

        m_rec_btn.setImageResource(R.drawable.ic_baseline_fiber_manual_record_24);

        m_rec_overlay.setVisibility(View.INVISIBLE);

        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        m_rec_btn.setOnClickListener((view)->{
            if(!isRecording){
                m_rec_btn.setImageResource(R.drawable.ic_baseline_stop_24);
                m_rec_overlay.setVisibility(View.VISIBLE);
                isRecording = true;
                startRecording(Paths.get(getContext().getDataDir().getPath(), RECORDED_FILE).toString());
            }else{
                m_rec_btn.setImageResource(R.drawable.ic_baseline_fiber_manual_record_24);
                m_rec_overlay.setVisibility(View.INVISIBLE);
                isRecording = false;
                stopRecording();
            }
        });

        return root;
    }
    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
    }
}