package com.adafruit.bluefruit.le.connect.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.adafruit.bluefruit.le.connect.R;
import com.adafruit.bluefruit.le.connect.app.settings.PreferencesFragment;
import com.adafruit.bluefruit.le.connect.ble.BleManager;
import com.adafruit.bluefruit.le.connect.ble.BleUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class PadActivity extends UartInterfaceActivity {
    // Log
    private final static String TAG = PadActivity.class.getSimpleName();
    private final static boolean kIsImmersiveModeEnabled = false;

    //Data values
    private volatile ArrayList<UartDataChunk> mDataBuffer;
    public int raw_left;
    public int raw_right;
    public char left_holder_char = 'm';
    public char right_holder_char = 'M';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad);

        mBleManager = BleManager.getInstance(this);

        final TextView leftSliderText = (TextView)findViewById(R.id.leftSeekbarText);
        final TextView rightSliderText = (TextView)findViewById(R.id.rightSeekbarText);
        leftSliderText.setTextSize(30);
        rightSliderText.setTextSize(30);
        VerticalSeekBar leftSeekbar = (VerticalSeekBar)findViewById(R.id.leftSeekBar);
        VerticalSeekBar rightSeekbar = (VerticalSeekBar)findViewById(R.id.rightSeekBar);
        leftSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //display current value to user
                leftSliderText.setText(""+progress);
                raw_left = progress;
                char sentValue = convertLeftRawToAlpha(raw_left);
                //don't send duplicate values
                if(sentValue  != left_holder_char) {
                    sendTouchEvent(sentValue);
                }
                //assign updated value
                left_holder_char = sentValue;
            }
        });

        rightSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //display current value to user
                rightSliderText.setText(""+progress);
                raw_right = progress;
                char sentValue = convertRightRawToAlpha(raw_right);
                //don't send duplicate values
                if(sentValue  != right_holder_char) {
                    sendTouchEvent(sentValue);
                }
                //assign updated value
                right_holder_char = sentValue;
            }
        });
        // Start service
        onServicesDiscovered();
    }


    //Left side slider input
    public char convertLeftRawToAlpha(int left_raw_input){
        if((left_raw_input <= 255) && (left_raw_input > 245)) {
            return 'a';
        }
        else if((left_raw_input <= 245) && (left_raw_input > 235)) {
            return 'b';
        }
        else if((left_raw_input <= 235) && (left_raw_input > 225)) {
            return 'c';
        }
        else if((left_raw_input <= 225) && (left_raw_input > 215)) {
            return 'd';
        }
        else if((left_raw_input <= 215) && (left_raw_input > 205)) {
            return 'e';
        }
        else if((left_raw_input <= 205) && (left_raw_input > 195)) {
            return 'f';
        }
        else if((left_raw_input <= 195) && (left_raw_input > 185)) {
            return 'g';
        }
        else if((left_raw_input <= 185) && (left_raw_input > 175)) {
            return 'h';
        }
        else if((left_raw_input <= 175) && (left_raw_input > 165)) {
            return 'i';
        }
        else if((left_raw_input <= 165) && (left_raw_input > 155)) {
            return 'j';
        }
        else if((left_raw_input <= 155) && (left_raw_input > 145)) {
            return 'k';
        }
        else if((left_raw_input <= 145) && (left_raw_input > 135)) {
            return 'l';
        }

        //dead zone
        else if((left_raw_input <= 135) && (left_raw_input > 115)) {
            return 'm';
        }
        //end dead zone

        else if((left_raw_input <= 115) && (left_raw_input > 105)) {
            return 'n';
        }
        else if((left_raw_input <= 105) && (left_raw_input > 95)) {
            return 'o';
        }
        else if((left_raw_input <= 95) && (left_raw_input > 85)) {
            return 'p';
        }
        else if((left_raw_input <= 85) && (left_raw_input > 75)) {
            return 'q';
        }
        else if((left_raw_input <= 75) && (left_raw_input > 65)) {
            return 'r';
        }
        else if((left_raw_input <= 65) && (left_raw_input > 55)) {
            return 's';
        }
        else if((left_raw_input <= 55) && (left_raw_input > 45)) {
            return 't';
        }
        else if((left_raw_input <= 45) && (left_raw_input > 35)) {
            return 'u';
        }
        else if((left_raw_input <= 35) && (left_raw_input > 25)) {
            return 'v';
        }
        else if((left_raw_input <= 25) && (left_raw_input > 15)) {
            return 'w';
        }
        else if((left_raw_input <= 15) && (left_raw_input > 5)) {
            return 'x';
        }
        else if((left_raw_input <= 5) && (left_raw_input > 0)) {
            return 'y';
        }
        else if(left_raw_input == 0) {
            return 'z';
        }
        //stop if error
        else {
            return 'm';
        }
    }
    //end left side slider input

    //Right side slider input
    public char convertRightRawToAlpha(int right_raw_input){
        if((right_raw_input <= 255) && (right_raw_input > 245)) {
            return 'A';
        }
        else if((right_raw_input <= 245) && (right_raw_input > 235)) {
            return 'B';
        }
        else if((right_raw_input <= 235) && (right_raw_input > 225)) {
            return 'C';
        }
        else if((right_raw_input <= 225) && (right_raw_input > 215)) {
            return 'D';
        }
        else if((right_raw_input <= 215) && (right_raw_input > 205)) {
            return 'E';
        }
        else if((right_raw_input <= 205) && (right_raw_input > 195)) {
            return 'F';
        }
        else if((right_raw_input <= 195) && (right_raw_input > 185)) {
            return 'G';
        }
        else if((right_raw_input <= 185) && (right_raw_input > 175)) {
            return 'H';
        }
        else if((right_raw_input <= 175) && (right_raw_input > 165)) {
            return 'I';
        }
        else if((right_raw_input <= 165) && (right_raw_input > 155)) {
            return 'J';
        }
        else if((right_raw_input <= 155) && (right_raw_input > 145)) {
            return 'K';
        }
        else if((right_raw_input <= 145) && (right_raw_input > 135)) {
            return 'L';
        }

        //dead zone
        else if((right_raw_input <= 135) && (right_raw_input > 115)) {
            return 'M';
        }
        //end dead zone

        else if((right_raw_input <= 115) && (right_raw_input > 105)) {
            return 'N';
        }
        else if((right_raw_input <= 105) && (right_raw_input > 95)) {
            return 'O';
        }
        else if((right_raw_input <= 95) && (right_raw_input > 85)) {
            return 'P';
        }
        else if((right_raw_input <= 85) && (right_raw_input > 75)) {
            return 'Q';
        }
        else if((right_raw_input <= 75) && (right_raw_input > 65)) {
            return 'R';
        }
        else if((right_raw_input <= 65) && (right_raw_input > 55)) {
            return 'S';
        }
        else if((right_raw_input <= 55) && (right_raw_input > 45)) {
            return 'T';
        }
        else if((right_raw_input <= 45) && (right_raw_input > 35)) {
            return 'U';
        }
        else if((right_raw_input <= 35) && (right_raw_input > 25)) {
            return 'V';
        }
        else if((right_raw_input <= 25) && (right_raw_input > 15)) {
            return 'W';
        }
        else if((right_raw_input <= 15) && (right_raw_input > 5)) {
            return 'X';
        }
        else if((right_raw_input <= 5) && (right_raw_input > 0)) {
            return 'Y';
        }
        else if(right_raw_input == 0) {
            return 'Z';
        }
        //stop if error
        else {
            return 'M';
        }
    }
    //end right side slider input

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // (required for compile time, no functionality)
        getMenuInflater().inflate(com.adafruit.bluefruit.le.connect.R.menu.menu_pad, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendTouchEvent(char output) {
        // edited to only send a char
        // concatenating to a string is required since UartInterfaceActivity requires a String input
        String data = "" + output + "";
        // utilizes inherited parent method
        sendData(data);
    }

    @Override
    public void onServicesDiscovered() {
        super.onServicesDiscovered();
        enableRxNotifications();
    }

    @Override
    public void onDisconnected() {
        super.onDisconnected();
        Log.d(TAG, "Disconnected. Back to previous activity");
        setResult(-1);      // Unexpected Disconnect
        finish();
    }

    @Override
    public synchronized void onDataAvailable(BluetoothGattCharacteristic characteristic) {
        super.onDataAvailable(characteristic);
        // UART RX
        if (characteristic.getService().getUuid().toString().equalsIgnoreCase(UUID_SERVICE)) {
            if (characteristic.getUuid().toString().equalsIgnoreCase(UUID_RX)) {
                final byte[] bytes = characteristic.getValue();

                final UartDataChunk dataChunk = new UartDataChunk(System.currentTimeMillis(), UartDataChunk.TRANSFERMODE_RX, bytes);
                mDataBuffer.add(dataChunk);
            }
        }
    }

    // endregion

    // region DataFragment
    public static class DataFragment extends Fragment {
        private SpannableStringBuilder mTextSpanBuffer;
        private ArrayList<UartDataChunk> mDataBuffer;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }
    }
    // endregion
}
