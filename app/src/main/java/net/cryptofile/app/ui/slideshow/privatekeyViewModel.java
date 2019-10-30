package net.cryptofile.app.ui.slideshow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class privatekeyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public privatekeyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is privatekey fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}