package com.test.prototype.internetresources;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

public class ShowLicense extends AppCompatActivity {

    private final static String LICENSE_URL = "https://www.gnu.org/licenses/gpl.txt";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.license_agreement_screen);

        LicenseDownloader licenseDl = new LicenseDownloader();
        licenseDl.execute(LICENSE_URL);
    }

    private void updateLicenseText(String licenseText) {
        ((TextView)findViewById(R.id.textLicense)).setText(licenseText);
    }

    public void close(View view) {
        setResult(RESULT_OK);
        finish();
    }

    private class LicenseDownloader extends AsyncTask<String, Void, String> {

        private Exception exception = null;

        @Override
        protected String doInBackground(String... params) {
            try {
                URL licenseUrl = new URL(params[0]);
                BufferedReader reader = new BufferedReader(new InputStreamReader(licenseUrl.openStream()));
                return reader.lines().parallel().collect(Collectors.joining("\n"));
            } catch (Exception e) {
                exception = e;
            }

            return "";
        }

        @Override
        protected void onPostExecute(String license) {
            if (exception != null) {
                exception.printStackTrace();
                return;
            }

            updateLicenseText(license);
        }
    }
}
