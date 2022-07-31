package com.example.vv.quicknote;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vv.quicknote.Port.Exporter;
import com.example.vv.quicknote.constants.Constants;
import com.example.vv.quicknote.util.DateUtil;
import com.example.vv.quicknote.util.MainActivityUtil;
import com.example.vv.quicknote.util.ToasterUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.vv.quicknote.constants.Constants.EMPTY_STR;
import static com.example.vv.quicknote.constants.Constants.KEY_QNOTE;
import static com.example.vv.quicknote.constants.Constants.KEY_MAJOR_SAVED;
import static com.example.vv.quicknote.constants.Constants.QUERY_DIALOG_BTN_OK;
import static com.example.vv.quicknote.constants.Constants.QUERY_DIALOG_CANCEL;
import static com.example.vv.quicknote.constants.Constants.QUERY_DIALOG_TITLE;
import static com.example.vv.quicknote.util.ToasterUtil.shortToaster;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final Logger LOGGER = LoggerFactory.getLogger(MainActivity.class);
    private final Context context = MainActivity.this;

    private EditText text_data;
    private Button button_modify_save;
    private Button button_backup;
    private Button button_search;
    private Button button_delete;

    private boolean firstSaveDoneForSession = false, automatedTextAdded = false;
    private String data;
    private SharedPreferences shared;

    /**
     * The modify button acts as the the save button also when the edittext is enabled, and as modify when not enabled
     */
    int modifySaveCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_delete = findViewById(R.id.button_delete);
        button_backup = findViewById(R.id.button_Backup);
        button_search = findViewById(R.id.button_search);
        button_modify_save = findViewById(R.id.button_modify);
        text_data = findViewById(R.id.text_data);

        button_delete.setOnClickListener(this);
        button_modify_save.setOnClickListener(this);
        button_search.setOnClickListener(this);
        button_backup.setOnClickListener(this);
        text_data.setEnabled(false);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        shared = context.getSharedPreferences(KEY_MAJOR_SAVED, Context.MODE_PRIVATE);

        if (!firstSaveDoneForSession) {
            data = shared.getString(KEY_QNOTE, EMPTY_STR);
            //String currentDateTime = DateUtil.getCurrentTimeStamp() + "\n";
            String currentDateTime = "->\n";
            automatedTextAdded = true;
            text_data.setText(String.format("%s\n\n%s", currentDateTime, data));
            text_data.setSelection((currentDateTime).length());
        }
    }

    @Override
    public void onClick(View view) {
        if (view == button_delete) {
            if (automatedTextAdded) {
                try {
                    String text = text_data.getText().toString();
                    text = text.substring(text.indexOf('\n') + 1).substring(1);
                    automatedTextAdded = false;
                    text_data.setText(text);
                    text_data.setSelection(0);
                } catch (Exception ignored) {
                }
            } else {
                shortToaster(context, "Can't delete, no auto date in 1st line!");
            }
        } else if (view == button_modify_save) {
            if (MainActivityUtil.isOdd(modifySaveCounter)) {
                text_data.setEnabled(false);
                button_modify_save.setText(R.string.button_modify_save_modify);

                firstSaveDoneForSession = true;
                SharedPreferences.Editor editor = shared.edit();
                data = text_data.getText().toString();
                if (BuildConfig.DEBUG) LOGGER.info("The data to be edited : " + data);
                editor.putString(KEY_QNOTE, data);
                editor.apply();
            } else {
                text_data.setEnabled(true);
                text_data.requestFocus();
                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                if (firstSaveDoneForSession) text_data.setSelection(0);
                button_modify_save.setText(R.string.button_modify_save_save);
            }
            modifySaveCounter++;
        } else if (view == button_search) {
            promptSearching();

        } else if (view == button_backup) {
            Exporter exporter = new Exporter();
            boolean isExportDone = exporter.export(text_data.getText().toString());
            String backupToast = "BACKUP done";
            if (!isExportDone) backupToast = "BACKUP failed";
            shortToaster(context, backupToast);
        }
    }


    private void promptSearching() {
        final View promptView = LayoutInflater.from(context)
                .inflate(R.layout.prompt,
                        findViewById(android.R.id.content),
                        false);
        EditText newValueCatchment = promptView.findViewById(R.id.text_newValue);
        AtomicReference<String> newValueInflow = new AtomicReference<>(EMPTY_STR);

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(QUERY_DIALOG_TITLE);
        alert.setView(promptView);
        alert.setPositiveButton(QUERY_DIALOG_BTN_OK, (dialog, which) -> {
            try {
                newValueInflow.set(String.valueOf(newValueCatchment.getText()).trim());
                int searchIndex = text_data.getText().toString().indexOf(newValueInflow.get());
                if (searchIndex == -1) shortToaster(context, String.format("%s' not found", newValueInflow.get()));
                else {
                    LOGGER.info("queried '{}' present at index: {}", newValueInflow.get(), searchIndex);
                    text_data.setEnabled(true);
                    text_data.requestFocus();
                    text_data.scrollTo(0, searchIndex);
                    text_data.setSelection(searchIndex);
                }
            } catch (Exception e) {
                LOGGER.error("Error: ", e);
            }
        });
        alert.setNeutralButton(QUERY_DIALOG_CANCEL, null);
        alert.show();
    }
}