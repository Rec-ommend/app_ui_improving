package com.example.rec_commend.ui.share;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.rec_commend.MainActivity;
import com.example.rec_commend.R;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

import java.io.ByteArrayOutputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class VoiceColorFragment extends Fragment {

    private static final String ARG_SEARCH_MODE = "searchMode";
    private static final String ARG_TIMBRE = "timbre";
    private static final String ARG_TIMBRE_NORM = "timbreNorm";

    // UI elements
    private TextView shareDescription;
    private TextView voiceColorHex;
    private ImageView voiceColorView;
    private Button shareBtn;
    private PopupWindow sharePopup;
    private Button simBtn;
    private TextView timbreDataTextView;
    private Button popupCloseBtn;

    // User voice color
    private int r;
    private int g;
    private int b;

    private HashMap<String, Double> timbreNorm;
    private HashMap<String, Double> timbre;
    private String searchMode;

    public VoiceColorFragment() {}

    public static VoiceColorFragment newInstance(String _mode, HashMap<String, Float> _timbreNorm, HashMap<String, Float> _timbre) {
        VoiceColorFragment fragment = new VoiceColorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SEARCH_MODE, _mode);
        args.putSerializable(ARG_TIMBRE_NORM, _timbreNorm);
        args.putSerializable(ARG_TIMBRE, _timbre);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchMode = getArguments().getString(ARG_SEARCH_MODE);
            timbreNorm = (HashMap<String, Double>) getArguments().getSerializable(ARG_TIMBRE_NORM);
            timbre = (HashMap<String, Double>) getArguments().getSerializable(ARG_TIMBRE);
            colorMapping(timbreNorm);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_voice_color, container, false);

        shareDescription = root.findViewById(R.id.share_description);
        if(searchMode.equals("M")) // Music search
            shareDescription.setText(R.string.share_description_m);
        else // searchMode == "T" // Tune search
            shareDescription.setText(R.string.share_description_t);

        voiceColorHex = root.findViewById(R.id.voice_color_hex);
        voiceColorHex.setText(String.format("#%02X%02X%02X", r, g, b));

        voiceColorView = root.findViewById(R.id.voice_color);
        voiceColorView.setColorFilter(Color.rgb(r, g, b));

        shareBtn = root.findViewById(R.id.share_btn);
        shareBtn.setOnClickListener((view)-> sharing());
        simBtn=root.findViewById(R.id.sim_Btn);
        simBtn.setOnClickListener((view)-> initPopup());
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
        }

        return root;
    }

    private void colorMapping(Map<String, Double> _timbre){
        double depth = Math.max(Math.min(_timbre.get("depth"), 1), 0);
        double brightness = Math.max(Math.min(_timbre.get("brightness"), 1), 0);
        double roughness = Math.max(Math.min(_timbre.get("roughness"), 1), 0);
        double warmth = Math.max(Math.min(_timbre.get("warmth"), 1), 0);
        double sharpness = Math.max(Math.min(_timbre.get("sharpness"), 1), 0);
        double boominess = Math.max(Math.min(_timbre.get("boominess"), 1), 0);
        r = (int) (roughness * 225 + 15 + warmth * 15);
        g = (int) (sharpness * 225 + 15 + brightness * 15);
        b = (int) (boominess * 225 + 15 + depth * 15);
    }

    private void sharing(){
        LinearLayout con=(LinearLayout)getView().findViewById(R.id.conslayout);
        Bitmap bitmap = Bitmap.createBitmap(con.getWidth(), con.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        con.draw(canvas);
        Uri uri=getImageUri(getActivity(),bitmap);


        Intent shareintent=new Intent(Intent.ACTION_SEND);

        shareintent.putExtra(Intent.EXTRA_STREAM,uri);
        shareintent.setType("image/*");
        startActivity(Intent.createChooser(shareintent,"공유"));
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void initPopup(){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.fragment_share_popup, null);
        sharePopup = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        sharePopup.showAtLocation(layout, Gravity.CENTER, 0,0);

        // Set popup back dim
        View container = sharePopup.getContentView().getRootView();
        Context context = sharePopup.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.3f;
        wm.updateViewLayout(container, p);

/*
        kakaoBtn = (Button) layout.findViewById(R.id.kakao_btn);
        kakaoBtn.setOnClickListener(this::setKakaoBtn);
*/

        timbreDataTextView = layout.findViewById(R.id.timbre_data_text);
        StringBuilder timbreDataString = new StringBuilder();
        for (Map.Entry entry : timbre.entrySet()) {
            String key = (String) entry.getKey();
            Double value = (Double) entry.getValue();

            if (key.equals("reverb")) continue;

            timbreDataString.append(key);
            timbreDataString.append(" = ");
            timbreDataString.append(String.format(Locale.KOREA, "%.3f", value));
            timbreDataString.append("\n");
        }
        timbreDataTextView.setText(timbreDataString);

        popupCloseBtn = (Button) layout.findViewById(R.id.popup_close_btn);
        popupCloseBtn.setOnClickListener((view)-> sharePopup.dismiss());

        // hide back button of action bar
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
        }
    }

    public void setKakaoBtn(View v) {
        System.out.println("Click kakao button");

        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("Rec.ommend",
                        "https://ifh.cc/g/uziq3S.jpg",
                        LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com").build())
                        .setDescrption("당신의 Voice Color는?")
                        .build())
                .build();

        Map<String, String> serverCallbackArgs = new HashMap<String, String>();
        serverCallbackArgs.put("user_id", "${current_user_id}");
        serverCallbackArgs.put("product_id", "${shared_product_id}");


        KakaoLinkService.getInstance().sendDefault(getContext(), params, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {}

            @Override
            public void onSuccess(KakaoLinkResponse result) {
            }

        });
    }
}