package com.example.rec_commend.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TsearchViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TsearchViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is tune search fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}