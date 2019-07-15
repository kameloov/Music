package com.orientalmusic.music.dialog;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;

import com.orientalmusic.music.DataStoreManager;
import com.orientalmusic.music.R;
import com.orientalmusic.music.Social;
import com.orientalmusic.music.activity.MainActivity;
import com.orientalmusic.music.entity.Contact;
import com.orientalmusic.music.entity.More;

/**
 * Created by kameloov on 12/14/2017.
 */

public class ContactDialog extends Dialog implements View.OnClickListener {
    private MainActivity mainActivity;
    private CardView crdPhone, crdEmail, crdFacebook, crdYoutube, crdTwitter, crdInstagram;
    private Contact contact;

    public ContactDialog(@NonNull MainActivity mainActivity) {
        super(mainActivity);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_contact);
        this.mainActivity = mainActivity;
        attachViews();
        attachEvents();
        viewData();
    }

    private void attachViews() {
        crdPhone = findViewById(R.id.crdPhone);
        crdEmail = findViewById(R.id.crdEmail);
        crdFacebook = findViewById(R.id.crdFacebook);
        crdYoutube = findViewById(R.id.crdYoutube);
        crdTwitter = findViewById(R.id.crdTwitter);
        crdInstagram = findViewById(R.id.crdInstagram);
    }

    private void attachEvents() {
        crdInstagram.setOnClickListener(this);
        crdTwitter.setOnClickListener(this);
        crdYoutube.setOnClickListener(this);
        crdFacebook.setOnClickListener(this);
        crdPhone.setOnClickListener(this);
        crdEmail.setOnClickListener(this);
    }

    private void viewData() {
        contact = DataStoreManager.getContactData(getContext());
        crdEmail.setEnabled(contact.getEmail().length() > 0);
        crdPhone.setEnabled(contact.getPhone().length() > 0);
        crdFacebook.setEnabled(contact.getFacebook().length() > 0);
        crdTwitter.setEnabled(contact.getTwitter().length() > 0);
        crdInstagram.setEnabled(contact.getInstagram().length() > 0);
        crdYoutube.setEnabled(contact.getYoutube().length() > 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.crdEmail:
                if (contact != null) {
                    Social.sendEmail(getContext(), contact.getEmail(), "", "");
                    dismiss();
                }
                break;
            case R.id.crdPhone:
                if (contact != null) {
                    Social.call(getContext(), contact.getPhone());
                    dismiss();
                }
                break;
            case R.id.crdFacebook:
                if (contact != null) {
                    Social.facebook(getContext(), contact.getFacebook());
                    dismiss();
                }
                break;
            case R.id.crdYoutube:
                if (contact != null) {
                    Social.youtube(getContext(), contact.getYoutube());
                    dismiss();
                }
                break;
            case R.id.crdInstagram:
                if (contact != null) {
                    Social.browser(getContext(),contact.getInstagram());
                    dismiss();
                }

                break;
            case R.id.crdTwitter:
                if (contact != null) {
                    Social.browser(getContext(),contact.getTwitter());
                    dismiss();
                }
                break;
        }
    }
}
