package cn.halbert.xiaomitv.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    private static final int DEFAULT_INPUT = 23;
    private static final String LAST_INPUT_KEY = "lastInput";
    private static final String EXTERNAL_PLAY_ACTION = "com.xiaomi.mitv.tvplayer.EXTSRC_PLAY";
    private static final String EXTERNAL_PLAY_EXTRA_INPUT_KEY = "input";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchToLastInput();
        buildUI();
    }

    private void buildUI() {
        setContentView(R.layout.main);

        Map<Integer, Integer> hdmiButtonInputMap = new HashMap<>();
        hdmiButtonInputMap.put(R.id.btn_hdmi1, 23);
        hdmiButtonInputMap.put(R.id.btn_hdmi2, 24);
        hdmiButtonInputMap.put(R.id.btn_hdmi3, 25);

        for (Map.Entry<Integer, Integer> entry : hdmiButtonInputMap.entrySet()) {
            Integer id = entry.getKey();
            Integer input = entry.getValue();
            Button button = findViewById(id);
            button.setOnClickListener(v -> switchToInput(input));
        }

        Button settingsButton = findViewById(R.id.btn_settings);
        settingsButton.setOnClickListener(v -> openSettings());
    }

    private void switchToLastInput() {
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        int lastInput = prefs.getInt(LAST_INPUT_KEY, DEFAULT_INPUT);
        switchToInput(lastInput);
    }

    private void switchToInput(int input) {
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(LAST_INPUT_KEY, input);
        editor.apply();

        Intent intent = new Intent();
        intent.setAction(EXTERNAL_PLAY_ACTION);
        intent.putExtra(EXTERNAL_PLAY_EXTRA_INPUT_KEY, input);
        PackageManager packageManager = getPackageManager();
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(this, "Start to show input " + input, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void openSettings () {
        startActivity(new Intent(Settings.ACTION_SETTINGS));
    }
}