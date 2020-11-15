package com.example.rec_commend.ui.share;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.BoringLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.example.rec_commend.R;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

import java.util.HashMap;
import java.util.Map;

public class ShareFragment extends Fragment {

    private static final String ARG_R = "r";
    private static final String ARG_G = "g";
    private static final String ARG_B = "b";

    // UI elements
    private ImageView voiceColorView;
    private Button shareBtn;
    private PopupWindow sharePopup;
    private Button kakaoBtn;
    private Button facebookBtn;
    private Button instagramBtn;
    private Button twitterBtn;
    private Button popupCloseBtn;
    private ImageView popupBackOverlay;

    // User voice color
    private int r;
    private int g;
    private int b;

    public ShareFragment() {}

    public static ShareFragment newInstance(int r, int g, int b) {
        ShareFragment fragment = new ShareFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_R, r);
        args.putInt(ARG_G, g);
        args.putInt(ARG_B, b);
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_share, container, false);
        voiceColorView = root.findViewById(R.id.voice_color);
        voiceColorView.setColorFilter(Color.rgb(r, g, b));
        popupBackOverlay = root.findViewById(R.id.popup_back_overlay);
        popupBackOverlay.setVisibility(View.INVISIBLE);

        shareBtn = root.findViewById(R.id.share_btn);
        shareBtn.setOnClickListener((view)->{
            popupBackOverlay.setVisibility(View.VISIBLE);
            initPopup();
        });


        return root;
    }

    private void initPopup(){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.fragment_share_popup, null);
        sharePopup = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        sharePopup.showAtLocation(layout, Gravity.CENTER, 0,0);

        kakaoBtn = (Button) layout.findViewById(R.id.kakao_btn);
        kakaoBtn.setOnClickListener(this::setKakaoBtn);

        popupCloseBtn = (Button) layout.findViewById(R.id.popup_close_btn);
        popupCloseBtn.setOnClickListener((view)->{
            sharePopup.dismiss();
            popupBackOverlay.setVisibility(View.INVISIBLE);
        });

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