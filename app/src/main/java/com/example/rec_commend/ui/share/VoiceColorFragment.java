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
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Map;

public class VoiceColorFragment extends Fragment {

    private static final String ARG_R = "r";
    private static final String ARG_G = "g";
    private static final String ARG_B = "b";
    private static final String ARG_SEARCH_MODE = "searchMode";

    // UI elements
    private TextView shareDescription;
    private TextView voiceColorHex;
    private ImageView voiceColorView;
    private Button shareBtn;
    private PopupWindow sharePopup;
    private Button simBtn;
    private Button kakaoBtn;
    private Button facebookBtn;
    private Button instagramBtn;
    private Button twitterBtn;
    private Button roughBtn;
    private Button warmBtn;
    private Button sharpBtn;
    private Button brightBtn;
    private Button boomBtn;
    private Button depthBtn;
    private Button popupCloseBtn;

    // User voice color
    private int r;
    private int g;
    private int b;

    private String searchMode;

    public VoiceColorFragment() {}

    public static VoiceColorFragment newInstance(int _r, int _g, int _b, String _mode) {
        VoiceColorFragment fragment = new VoiceColorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_R, _r);
        args.putInt(ARG_G, _g);
        args.putInt(ARG_B, _b);
        args.putString(ARG_SEARCH_MODE, _mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            r = getArguments().getInt(ARG_R);
            g = getArguments().getInt(ARG_G);
            b = getArguments().getInt(ARG_B);
            searchMode = getArguments().getString(ARG_SEARCH_MODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_voice_color, container, false);

        shareDescription = root.findViewById(R.id.share_description);
        if(searchMode == "M") // Music search
            shareDescription.setText(R.string.share_description_m);
        else // searchMode == "T" // Tune search
            shareDescription.setText(R.string.share_description_t);

        voiceColorHex = root.findViewById(R.id.voice_color_hex);
        voiceColorHex.setText(String.format("#%02X%02X%02X", r, g, b));

        voiceColorView = root.findViewById(R.id.voice_color);
        voiceColorView.setColorFilter(Color.rgb(r, g, b));

        shareBtn = root.findViewById(R.id.share_btn);
        shareBtn.setOnClickListener((view)->{
            sharing();
        });
        simBtn=root.findViewById(R.id.sim_Btn);
        simBtn.setOnClickListener((view)->{
            initPopup();
        });
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
        }

        return root;
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

        sharePopup.setOnDismissListener(()->{
        });
/*
        kakaoBtn = (Button) layout.findViewById(R.id.kakao_btn);
        kakaoBtn.setOnClickListener(this::setKakaoBtn);
*/
        Toast toast=Toast.makeText(getActivity(), "Toast test",Toast.LENGTH_SHORT);
        roughBtn = (Button) layout.findViewById(R.id.rough_btn);
        roughBtn.setOnClickListener((view)->{
            toast.setText("Roughness="+(r/16));
            toast.show();
        });
        warmBtn = (Button) layout.findViewById(R.id.warm_btn);
        warmBtn.setOnClickListener((view)->{
            toast.setText("Warmth="+(r%16));
            toast.show();
        });
        sharpBtn = (Button) layout.findViewById(R.id.sharp_btn);
        sharpBtn.setOnClickListener((view)->{
            toast.setText("Sharpness="+(g/16));
            toast.show();
        });
        brightBtn = (Button) layout.findViewById(R.id.bright_btn);
        brightBtn.setOnClickListener((view)->{
            toast.setText("Brightness="+(g%16));
            toast.show();
        });
        boomBtn = (Button) layout.findViewById(R.id.boom_btn);
        boomBtn.setOnClickListener((view)->{
            toast.setText("Boominess="+(b/16));
            toast.show();
        });
        depthBtn = (Button) layout.findViewById(R.id.depth_btn);
        depthBtn.setOnClickListener((view)->{
            toast.setText("Depth="+(b%16));
            toast.show();
        });
        popupCloseBtn = (Button) layout.findViewById(R.id.popup_close_btn);
        popupCloseBtn.setOnClickListener((view)->{
            sharePopup.dismiss();
        });

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