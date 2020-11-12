package com.example.rec_commend.ui.msearch;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
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

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.example.rec_commend.MultipartUtility;
import com.example.rec_commend.R;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MsearchFragment extends Fragment {

    private MsearchViewModel msearchViewModel;
    private ImageButton m_rec_btn;
    private ImageView m_rec_overlay;

    private boolean isRecording = false;
    final private static String RECORDED_FILE_NAME = "record";
    private String MP4FilePath;
    private String MP3FilePath;
    private MediaRecorder recorder;

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    //variable for http post
    private static final String urlString = "https://timbre-cx7hn2quva-uc.a.run.app/voice";

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

        String commonPath = Paths.get(getContext().getDataDir().getPath(), RECORDED_FILE_NAME).toString();
        MP4FilePath = commonPath + ".mp4";
        MP3FilePath = commonPath + ".mp3";
        m_rec_btn.setOnClickListener((view)->{
            if(!isRecording){
                m_rec_btn.setImageResource(R.drawable.ic_baseline_stop_24);
                m_rec_overlay.setVisibility(View.VISIBLE);
                isRecording = true;
                startRecording(MP4FilePath);
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
        recorder.setOutputFile(path);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
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
        recorder.reset();
        recorder.release();
        recorder = null;

        //convert mp4 to mp3 and post request
        long executionId = FFmpeg.executeAsync("-y -i " + MP4FilePath + " -vn " + MP3FilePath, new ExecuteCallback() {

            @Override
            public void apply(final long executionId, final int returnCode) {
                if (returnCode == RETURN_CODE_SUCCESS) {
                    Log.i(Config.TAG, "Async command execution completed successfully.");
                    PostRequest(MP3FilePath);
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    Log.i(Config.TAG, "Async command execution cancelled by user.");
                } else {
                    Log.i(Config.TAG, String.format("Async command execution failed with returnCode=%d.", returnCode));
                }
            }
        });

    }
    private void PostRequest(String filePath){
        new Thread(){
            @Override
            public void run(){
                try {
                    MultipartUtility multipart = new MultipartUtility(urlString, "UTF-8");

                    multipart.addFilePart("voice", new File(filePath));

                    List<String> response = multipart.finish();

                    System.out.println("SERVER REPLIED:");

                    for (String line : response) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}