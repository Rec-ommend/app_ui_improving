package com.example.rec_commend.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.example.rec_commend.MultipartUtility;
import com.example.rec_commend.R;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

@RequiresApi(api = Build.VERSION_CODES.O)
public abstract class SearchFragment extends Fragment {

    //UI elements
    protected TextView descriptionText;
    private ImageButton recBtn;
    private ImageView recOverlay;
    private Button jsonTestBtn;
    private TextView timerText;

    private boolean isRecording;
    final private static String RECORDED_FILE_NAME = "record";
    private String MP4FilePath;
    private String MP3FilePath;
    private String MP3TestFilePath;
    private MediaRecorder recorder;

    //recording timer thread
    private TimerTask ti;

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    //variable for http post
    private Thread postThread;
    protected static String urlString;
    private static final String testJson = "[{\"id\":\"tmFmFNn5-OM\",\"title\":\"Puss (Prod.by \\ub77c\\uc774\\uba38)\",\"singer\":\"\\uc9c0\\ubbfc (AOA), \\uc544\\uc774\\uc5b8\",\"genre\":\"\\ub7a9\\/\\ud799\\ud569\",\"release\":\"2015.03.20\",\"timbre_similarity\":98.7300118817},{\"id\":\"LmApDbvNCXg\",\"title\":\"Outro : Ego\",\"singer\":\"\\ubc29\\ud0c4\\uc18c\\ub144\\ub2e8\",\"genre\":\"\\ub7a9\\/\\ud799\\ud569\",\"release\":\"2020.02.21\",\"timbre_similarity\":139.5603603933},{\"id\":\"dj-OEG32OR4\",\"title\":\"Forever (Prod. By GRAY)\",\"singer\":\"BewhY (\\ube44\\uc640\\uc774)\",\"genre\":\"\\ub7a9\\/\\ud799\\ud569\",\"release\":\"2016.07.02\",\"timbre_similarity\":140.948202667},{\"id\":\"E-HXf-Dih3c\",\"title\":\"\\uc0ac\\uc774\\uba3c \\ub3c4\\ubbf8\\ub2c9\",\"singer\":\"\\uc0ac\\uc774\\uba3c \\ub3c4\\ubbf8\\ub2c9\",\"genre\":\"\\ub7a9\\/\\ud799\\ud569\",\"release\":\"2015.08.21\",\"timbre_similarity\":143.5430228883},{\"id\":\"aFsS59DkFhc\",\"title\":\"Okey Dokey\",\"singer\":\"MINO (\\uc1a1\\ubbfc\\ud638), \\uc9c0\\ucf54 (ZICO)\",\"genre\":\"\\ub7a9\\/\\ud799\\ud569\",\"release\":\"2015.08.29\",\"timbre_similarity\":153.3020229043},{\"id\":\"vRXZj0DzXIA\",\"title\":\"Ice Cream (with Selena Gomez)\",\"singer\":\"BLACKPINK\",\"genre\":\"\\ub304\\uc2a4\",\"release\":\"2020.08.28\",\"timbre_similarity\":161.693719075},{\"id\":\"VTASffPQGhY\",\"title\":\"\\ub204\\ub09c \\ub108\\ubb34 \\uc608\\ubed0 (Replay)\",\"singer\":\"SHINee (\\uc0e4\\uc774\\ub2c8)\",\"genre\":\"\\ub304\\uc2a4\",\"release\":\"2008.05.22\",\"timbre_similarity\":162.2474205873},{\"id\":\"of2GzuZGxo0\",\"title\":\"STILL ALIVE\",\"singer\":\"BIGBANG\",\"genre\":\"\\ub304\\uc2a4\",\"release\":\"2012.06.03\",\"timbre_similarity\":166.8970531035},{\"id\":\"jTYtNlSnn7g\",\"title\":\"I` ll Be There\",\"singer\":\"\\uc774\\ube0c\",\"genre\":\"\\ub85d\\/\\uba54\\ud0c8\",\"release\":\"2001.04.11\",\"timbre_similarity\":168.3398402292},{\"id\":\"wd2qbIh6wYQ\",\"title\":\"Moon\",\"singer\":\"\\ubc29\\ud0c4\\uc18c\\ub144\\ub2e8\",\"genre\":\"\\ub7a9\\/\\ud799\\ud569\",\"release\":\"2020.02.21\",\"timbre_similarity\":170.050908522}]\n";

    abstract protected void setDescription();
    abstract protected void setPostURL();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //setting UI elements
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        recBtn = root.findViewById(R.id.rec_btn);
        recOverlay = root.findViewById(R.id.rec_overlay);
        jsonTestBtn = root.findViewById(R.id.json_test_btn);
        timerText = root.findViewById(R.id.timer);
        descriptionText = root.findViewById(R.id.search_description);
        setDescription();

        recBtn.setImageResource(R.drawable.ic_baseline_fiber_manual_record_24);
        recOverlay.setVisibility(View.INVISIBLE);
        timerViewInit();

        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        String commonPath = Paths.get(getContext().getDataDir().getPath(), RECORDED_FILE_NAME).toString();
        MP4FilePath = commonPath + ".mp4";
        MP3FilePath = commonPath + ".mp3";
        MP3TestFilePath = Paths.get(getContext().getDataDir().getPath(), "TestVoice.mp3").toString();

        isRecording = false;
        recBtn.setOnClickListener((view)->{
            if(!isRecording){
                recBtn.setImageResource(R.drawable.ic_baseline_stop_24);
                recOverlay.setVisibility(View.VISIBLE);
                isRecording = true;
                startRecording(MP4FilePath);
                timerViewStart();
            }else{
                recBtn.setImageResource(R.drawable.ic_baseline_fiber_manual_record_24);
                recOverlay.setVisibility(View.INVISIBLE);
                isRecording = false;
                stopRecording(view);
                timerViewInit();
            }
        });

        jsonTestBtn.setOnClickListener((view)->{
            Bundle bundle = new Bundle();
            bundle.putString("jsonData", testJson);
            Navigation.findNavController(view).navigate(R.id.navigation_song_list, bundle);
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
        if(postThread != null) postThread.interrupt();
        if(ti != null) ti.cancel();
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

    private void stopRecording(View view) {
        recorder.stop();
        recorder.reset();
        recorder.release();
        recorder = null;
        convertMP4ToMP3AndPost(view, MP4FilePath, MP3FilePath);
        timerViewInit();
    }

    private void convertMP4ToMP3AndPost(View view, String inputFile, String outputFile){
        //convert mp4 to mp3
        String command = "-y -i " + inputFile + " -vn " + outputFile;
        long executionId = FFmpeg.executeAsync(command,
            (executionId1, returnCode) -> {
                if (returnCode == RETURN_CODE_SUCCESS) {
                    Log.i(Config.TAG, "Async command execution completed successfully.");
                    // Post Request
                    //PostRequest(outputFile); //RELEASE
                    postRequest(view, MP3TestFilePath); //DEBUG
                } else if (returnCode == RETURN_CODE_CANCEL) {
                    Log.i(Config.TAG, "Async command execution cancelled by user.");
                } else {
                    Log.i(Config.TAG, String.format("Async command execution failed with returnCode=%d.", returnCode));
                }
            }
        );
    }

    private void postRequest(View view, String filePath){
        if(postThread != null) postThread.interrupt();
        postThread = null;
        postThread = new Thread(){
            @Override
            public void run(){
                try {
                    setPostURL();
                    MultipartUtility multipart = new MultipartUtility(urlString, "UTF-8");

                    multipart.addFilePart("voice", new File(filePath));

                    List<String> response = multipart.finish();

                    System.out.println("SERVER REPLIED:");

                    String fullResponse = "";
                    for (String line : response) { fullResponse += line; }
                    System.out.println(fullResponse);

                    // Sending data to result page.
                    Bundle bundle = new Bundle();
                    bundle.putString("jsonData", fullResponse);
                    getActivity().runOnUiThread(() -> Navigation.findNavController(view).navigate(R.id.navigation_song_list, bundle));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        postThread.start();
    }

    private void timerViewStart(){
        final Timer timer = new Timer();
         ti = new TimerTask(){
            int second,minute,seconds=0;
            @SuppressLint("DefaultLocale")
            @Override
            public void run(){
                getActivity().runOnUiThread(() -> timerText.setText(String.format("%02d : %02d",minute,second)));
                seconds+=1;
                minute=seconds/60;
                second=seconds%60;
            }
        };
        timer.schedule(ti,0,1000);
    }

    private void timerViewInit(){
        if(ti != null) ti.cancel();
        timerText.setText("");
    }
}