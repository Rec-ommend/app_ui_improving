package com.example.rec_commend.ui;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.example.rec_commend.R;

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
    private Button recBtn;
    private ImageView recOverlay;
    private Button jsonTestBtn;
    private TextView timerText;
    private WaveFormView waveFormView;
    private ProgressBar progressBar;

    //abstract functions
    abstract protected void setDescription();
    abstract protected void setPostURL();
    abstract protected List<String> setPostDataAndRequest(String filePath) throws IOException;

    protected String searchMode;

    //variable for recording
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
    private final String [] permissions = {Manifest.permission.RECORD_AUDIO};

    //variable for http post
    private Thread postThread;
    protected static String urlString;
    private static final String testJson = "{\"origin\":{\"boominess\":17.623630224713473,\"brightness\":69.78387273942865,\"depth\":35.3642014943956,\"hardness\":66.01813382600065,\"reverb\":0,\"roughness\":61.714248729850794,\"sharpness\":58.738026616840145,\"warmth\":34.881154828579945},\"song\":\"[{\\\"id\\\":\\\"R3Fwdnij49o\\\",\\\"title\\\":\\\"\\\\uc2dc\\\\uac04\\\\uc758 \\\\ubc14\\\\uae65\\\",\\\"singer\\\":\\\"\\\\uc544\\\\uc774\\\\uc720\\\",\\\"genre\\\":\\\"\\\\ub85d\\\\/\\\\uba54\\\\ud0c8\\\",\\\"release\\\":\\\"2019.11.01\\\",\\\"timbre_similarity\\\":0},{\\\"id\\\":\\\"qEYOyZVWlzs\\\",\\\"title\\\":\\\"\\\\uc0ac\\\\ubfd0\\\\uc0ac\\\\ubfd0\\\",\\\"singer\\\":\\\"AOA\\\",\\\"genre\\\":\\\"\\\\ub304\\\\uc2a4\\\",\\\"release\\\":\\\"2014.11.11\\\",\\\"timbre_similarity\\\":0},{\\\"id\\\":\\\"LHUAmHYOXFM\\\",\\\"title\\\":\\\"\\\\uc544\\\\ub9c8\\\\ub450 (feat.\\\\uc6b0\\\\uc6d0\\\\uc7ac, \\\\uae40\\\\ud6a8\\\\uc740, \\\\ub109\\\\uc0b4, Huckleberry P)\\\",\\\"singer\\\":\\\"\\\\uc5fc\\\\ub530, \\\\ub525\\\\ud50c\\\\ub85c\\\\uc6b0, \\\\ud314\\\\ub85c\\\\uc54c\\\\ud1a0 (Paloalto), The Quiett, \\\\uc0ac\\\\uc774\\\\uba3c \\\\ub3c4\\\\ubbf8\\\\ub2c9\\\",\\\"genre\\\":\\\"\\\\ub7a9\\\\/\\\\ud799\\\\ud569\\\",\\\"release\\\":\\\"2019.12.03\\\",\\\"timbre_similarity\\\":0},{\\\"id\\\":\\\"dotD8om8HPs\\\",\\\"title\\\":\\\"\\\\uc560\\\\uc1a1\\\\uc774 (CF - \\\\uc368\\\\ub2c8\\\\ud150)\\\",\\\"singer\\\":\\\"\\\\ub809\\\\uc2dc\\\",\\\"genre\\\":\\\"\\\\ub7a9\\\\/\\\\ud799\\\\ud569\\\",\\\"release\\\":\\\"2003.10.07\\\",\\\"timbre_similarity\\\":0},{\\\"id\\\":\\\"AHbSzXM1e_s\\\",\\\"title\\\":\\\"\\\\uc778\\\\uae30 (Feat. \\\\uc1a1\\\\uac00\\\\uc778, \\\\ucc48\\\\uc2ac\\\\ub7ec)\\\",\\\"singer\\\":\\\"MC\\\\ubabd\\\",\\\"genre\\\":\\\"\\\\ub7a9\\\\/\\\\ud799\\\\ud569\\\",\\\"release\\\":\\\"2019.10.25\\\",\\\"timbre_similarity\\\":0},{\\\"id\\\":\\\"ZlVdJcS1Tms\\\",\\\"title\\\":\\\"To My Love (Korean Ver.)\\\",\\\"singer\\\":\\\"\\\\uc724\\\\ubbf8\\\\ub798\\\",\\\"genre\\\":\\\"R&B\\\\/Soul\\\",\\\"release\\\":\\\"2002.12.01\\\",\\\"timbre_similarity\\\":0},{\\\"id\\\":\\\"W4SXOQ7xFoo\\\",\\\"title\\\":\\\"\\\\uc548\\\\uc544\\\\uc918\\\",\\\"singer\\\":\\\"\\\\uc815\\\\uc900\\\\uc77c\\\",\\\"genre\\\":\\\"\\\\uc778\\\\ub514\\\\uc74c\\\\uc545, \\\\ub85d\\\\/\\\\uba54\\\\ud0c8\\\",\\\"release\\\":\\\"2011.11.23\\\",\\\"timbre_similarity\\\":0},{\\\"id\\\":\\\"mcjR94Q1qSI\\\",\\\"title\\\":\\\"The Time Goes On\\\",\\\"singer\\\":\\\"BewhY (\\\\ube44\\\\uc640\\\\uc774)\\\",\\\"genre\\\":\\\"\\\\ub7a9\\\\/\\\\ud799\\\\ud569\\\",\\\"release\\\":\\\"2015.03.10\\\",\\\"timbre_similarity\\\":0},{\\\"id\\\":\\\"ZPhttQ3kK2o\\\",\\\"title\\\":\\\"Music\\\",\\\"singer\\\":\\\"\\\\ubc14\\\\ub2e4 (BADA)\\\",\\\"genre\\\":\\\"\\\\ub304\\\\uc2a4, \\\\ubc1c\\\\ub77c\\\\ub4dc\\\",\\\"release\\\":\\\"2003.10.22\\\",\\\"timbre_similarity\\\":0},{\\\"id\\\":\\\"PDvtF8wuKjQ\\\",\\\"title\\\":\\\"\\\\uc694\\\\uc998 \\\\ubc14\\\\uc05c\\\\uac00\\\\ubd10\\\",\\\"singer\\\":\\\"2BIC(\\\\ud22c\\\\ube45)\\\",\\\"genre\\\":\\\"R&B\\\\/Soul\\\",\\\"release\\\":\\\"2014.04.10\\\",\\\"timbre_similarity\\\":0}]\",\"timbre\":{\"boominess\":0.3933148122086359,\"brightness\":0.916696793346051,\"depth\":0.3244571326418235,\"hardness\":1.3787054791923,\"reverb\":0,\"roughness\":1.1120699737927724,\"sharpness\":0.9450311890492772,\"warmth\":0.07938602069283819}}";

    //wave form view thread
    private Thread waveThread;

    //app preference
    protected SharedPreferences preferences;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //setting UI elements
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        descriptionText = root.findViewById(R.id.search_description);
        setDescription();

        recBtn = root.findViewById(R.id.rec_btn);

        recOverlay = root.findViewById(R.id.rec_overlay);
        recOverlay.setVisibility(View.INVISIBLE);

        timerText = root.findViewById(R.id.timer);
        initTimerView("녹음 버튼을 누르세요");

        waveFormView = root.findViewById(R.id.siriView);
        initWaveFormView();

        jsonTestBtn = root.findViewById(R.id.json_test_btn);

        progressBar = root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        //recording setting
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        String commonPath = Paths.get(getContext().getDataDir().getPath(), RECORDED_FILE_NAME).toString();
        MP4FilePath = commonPath + ".mp4";
        MP3FilePath = commonPath + ".mp3";
        MP3TestFilePath = Paths.get(getContext().getDataDir().getPath(), "TestVoice.mp3").toString();
        isRecording = false;
        recBtn.setSelected(false);
        recBtn.setOnClickListener((view)->{
            if(!isRecording){
                isRecording = true;
                startRecording(MP4FilePath);
                recBtn.setSelected(true);
            }else{
                isRecording = false;
                stopRecording(view);
            }
        });

        jsonTestBtn.setOnClickListener((view)->{
            Bundle bundle = new Bundle();
            bundle.putString("jsonData", testJson);
            bundle.putString("searchMode", "M");
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
        if(waveThread != null) waveThread.interrupt();
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
        recOverlay.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        timerViewStart();

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

        activateWaveFormView();
    }

    private void stopRecording(View view) {
        recBtn.setVisibility(View.INVISIBLE);
        recOverlay.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        initTimerView("잠시만 기다리세요...");
        initWaveFormView();

        recorder.stop();
        recorder.reset();
        recorder.release();
        recorder = null;
        convertMP4ToMP3AndPost(view, MP4FilePath, MP3FilePath);
    }

    private void convertMP4ToMP3AndPost(View view, String inputFile, String outputFile){
        //convert mp4 to mp3
        String command = "-y -i " + inputFile + " -vn " + outputFile;
        long executionId = FFmpeg.executeAsync(command,
            (executionId1, returnCode) -> {
                if (returnCode == RETURN_CODE_SUCCESS) {
                    Log.i(Config.TAG, "Async command execution completed successfully.");
                    //Post Request
                    //TODO: change to RELEASE code
//                    postRequest(view, outputFile); //RELEASE
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

                    List<String> response = setPostDataAndRequest(filePath);

                    System.out.println("SERVER REPLIED:");

                    StringBuilder fullResponse = new StringBuilder();
                    for (String line : response) { fullResponse.append(line); }
                    System.out.println(fullResponse);

                    // Sending data to result page.
                    Bundle bundle = new Bundle();
                    bundle.putString("jsonData", fullResponse.toString());
                    bundle.putString("searchMode", searchMode);
                    if(getActivity() != null)
                        getActivity().runOnUiThread(() -> Navigation.findNavController(view).navigate(R.id.navigation_song_list, bundle));

                } catch (IOException | NullPointerException e) {
                    e.printStackTrace();
                    if(getActivity() != null)
                        getActivity().runOnUiThread(() -> Navigation.findNavController(view).navigate(R.id.navigation_home));
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

    private void initTimerView(String msg){
        if(ti != null) ti.cancel();
        timerText.setText(msg);
    }

    private void activateWaveFormView(){
        waveFormView.setVisibility(View.VISIBLE);
        AnimatorSet waveformCreationAnim = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.wave_view_anim);
        waveformCreationAnim.setTarget(waveFormView);
        waveformCreationAnim.start();

        if(waveThread != null) waveThread.interrupt();
        waveThread = null;
        waveThread = new Thread(){
            @Override
            public void run(){
                try {
                    waveFormView.updateSpeaking(true);
                    while (recorder!=null){
                        int amp = recorder.getMaxAmplitude();
                        double logamp = Math.log10(amp);
                        //TODO: change to RELEASE code
                        waveFormView.updateAmplitude((float) (Math.pow(logamp, 3)/100.0f)); //RELEASE
//                        waveFormView.updateAmplitude(0.1f); //DEBUG
                        sleep(50);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        waveThread.start();
    }
    private void initWaveFormView(){
        waveFormView.setVisibility(View.INVISIBLE);
        waveFormView.updateSpeaking(false);
        if(waveThread != null) waveThread.interrupt();
    }
}