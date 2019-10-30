package net.cryptofile.app.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PublicKeyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PublicKeyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is publickey fragment");
        
    }

    public LiveData<String> getText() {
        return mText;
    }
}