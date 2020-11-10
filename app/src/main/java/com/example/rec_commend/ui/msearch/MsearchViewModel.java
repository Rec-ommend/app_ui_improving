package com.example.rec_commend.ui.msearch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MsearchViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MsearchViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is music search fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}