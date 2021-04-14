package com.example.notifyuser;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.button.MaterialButton;

import static com.example.notifyuser.R.drawable.team;

public class MainActivity extends AppCompatActivity {
    private MaterialButton mToastBtn, mAlertBtn, mNotificationBtn;
    private EditText mEditText1;
    private AlertDialog.Builder mBuilder, mBuilder2;
    private AlertDialog mDialog, mDialog2;
    private View mView;
    private TextView mText;
    private static final String CHANNEL_ID = "11011";
    NotificationCompat.Builder mNotificationBuilderBuilder;
    private int NOTIFICATON_ID = 1;
    private Bitmap mLargeIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToastBtn = findViewById(R.id.mToastBtn);
        mAlertBtn = findViewById(R.id.mAlertBoxBtn);
        mNotificationBtn = findViewById(R.id.mNotificationBtn);
        mEditText1 = findViewById(R.id.editText);
        mBuilder = new AlertDialog.Builder(this);
        mBuilder2 = new AlertDialog.Builder(this);
        mView = getLayoutInflater().inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.mToasLayout));

        mText = mView.findViewById(R.id.custom_toast_message);
        mLargeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_baseline_pan_tool_24);
        hideSoftKeyBoard();


    }

    private void hideSoftKeyBoard() {
        hideKeyboard(MainActivity.this);
        setupUi(findViewById(R.id.parentLayout));
    }

    private void setupUi(View mView) {
        if (!(mView instanceof EditText)){
            mView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    hideKeyboard(MainActivity.this);
                    return false;
                }
            });
        }
        if (mView instanceof ViewGroup){

            for (int i= 0;i<((ViewGroup) mView).getChildCount();i++){
                View mInnerView= ((ViewGroup) mView).getChildAt(i);
                setupUi(mInnerView);
            }

        }

    }

    private void hideKeyboard(MainActivity mainActivity) {
        InputMethodManager iManager = (InputMethodManager) mainActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (iManager.isAcceptingText()){

            iManager.hideSoftInputFromWindow(mainActivity.getCurrentFocus().getWindowToken(),0);

        }
    }

    public void printToast(View view) {

        PopupMenu mToastMenu = new PopupMenu(this, mToastBtn);
        mToastMenu.getMenuInflater().inflate(R.menu.toastmenu, mToastMenu.getMenu());
        mToastMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.mSimpleToast:
                    Toast.makeText(MainActivity.this, "Your Message : " + mEditText1.getText().toString(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.mCustomToast:
                    mText.setText(mEditText1.getText().toString());
                    Toast mToast = new Toast(this);
                    mToast.setView(mView);
                    mToast.setGravity(Gravity.CENTER, 0, 0);
                    mToast.setDuration(Toast.LENGTH_LONG);
                    mToast.show();
                    break;
                default:
                    return false;
            }
            return true;
        });
        mToastMenu.show();
    }

    public void openAleartBox(View view) {
        PopupMenu mAlertBoxmenu = new PopupMenu(this, mToastBtn);
        mAlertBoxmenu.getMenuInflater().inflate(R.menu.alertmenu, mAlertBoxmenu.getMenu());
        mAlertBoxmenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.mSimpleAlert:
                    mBuilder.setTitle("Want to exit?");
                    mBuilder.setMessage("Are You Sure");
                    mBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "Okay", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    mBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "Welcome Back Sir", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();

                        }
                    });
                    mDialog = mBuilder.create();
                    mDialog.show();
                    break;
                case R.id.mCustomAlert:
                    Toast.makeText(MainActivity.this, "Custom Alert Here", Toast.LENGTH_SHORT).show();
                    mBuilder2.setTitle("Enter Your Text Here");
                    final View customDialog = getLayoutInflater().inflate(R.layout.custom_alert_box, null);
                    mBuilder2.setView(customDialog);
                    mBuilder2.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EditText mEditText = customDialog.findViewById(R.id.mEditText2);
                            Toast.makeText(MainActivity.this, mEditText.getText().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    mDialog2 = mBuilder2.create();
                    mDialog2.show();
                    break;
                default:
                    return false;
            }
            return true;
        });
        mAlertBoxmenu.show();
    }

    public void pushNotification(View view) {

        mNotificationBuilderBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_pan_tool_24)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), team))
                .setContentTitle("New Notification")
                .setContentText(mEditText1.getText().toString())

                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        createNotificationChannel();
        NOTIFICATON_ID++;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence mName = "Vishal";
            String mDescription = "This is the description of the Notificaton Channel";
            int imp = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mCannel = new NotificationChannel(CHANNEL_ID, mName, imp);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mCannel);
            NotificationManagerCompat nManagerCompat = NotificationManagerCompat.from(this);
            nManagerCompat.notify(NOTIFICATON_ID, mNotificationBuilderBuilder.build());
        }
    }

}