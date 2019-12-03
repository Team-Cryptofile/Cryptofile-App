package net.cryptofile.app.ui.help;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HelpViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HelpViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(" To use this app:  \n  1. Upload a file vie the floating action button to the bottom right. " +
                "\n\n 2. The file will be encrypted from others and only the receiver with a key will be able to see it. " +
                "\n\n 3. The key can be copied to clipboard when clicking on the file. " + "\n\n 4.The key can be shared elsewhere with the receiver." );
    }

    public LiveData<String> getText() {
        return mText;
    }
}