package hedi.com.example.elarmor.unyielding;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    //Provides the entry point to Google Play services.
    protected GoogleApiClient mGoogleApiClient;

    //The list of geofences used in this sample.
    protected ArrayList<Geofence> mGeofenceList;

    //not sure what is tag for
    protected static final String TAG = "Main Errors";

    //Used to persist application state about whether geofences were added.
    private SharedPreferences mSharedPreferences;

    //Used to keep track of whether geofences were added.
    private boolean mGeofencesAdded;

    //Used when requesting to add or remove geofences.
    private PendingIntent mGeofencePendingIntent;

    protected LocationRequest mLocationRequest;

    protected TabHost tabMain;

    protected aLogsdbHandler adb;
    protected dbHandler andb;

    protected ArrayAdapter<String> arrayAdapter;
    protected ArrayAdapter<String> aaAttendance;

    protected ListView LvLogs, lvAnnouncement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabMain = (TabHost)findViewById(R.id.tabMain);
        tabMain.setup();

        TabHost.TabSpec tsGeneral = tabMain.newTabSpec("Announcements");
        tsGeneral.setContent(R.id.tabAnnouncement);
        tsGeneral.setIndicator("Announcement");
        tabMain.addTab(tsGeneral);

        tsGeneral = tabMain.newTabSpec("Attendance");
        tsGeneral.setContent(R.id.tabLogs);
        tsGeneral.setIndicator("Attendance");
        tabMain.addTab(tsGeneral);

        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<Geofence>();

        // Retrieve an instance of the SharedPreferences object.
        mSharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        // Get the value of mGeofencesAdded from SharedPreferences. Set to false as a default.
        mGeofencesAdded = mSharedPreferences.getBoolean(Constants.GEOFENCES_ADDED_KEY, false);

        populateGeofenceList();

        buildGoogleApiClient();

        adb = new aLogsdbHandler(this,null,null,1);
        andb = new dbHandler(this,null,null,1);

        LvLogs = (ListView)findViewById(R.id.lvLogs);

        lvAnnouncement = (ListView)findViewById(R.id.lvAnnouncement);
        // drop this database if already exists lvAnnouncement
        //adb.onUpgrade(adb.getWritableDatabase(), 1, 2);
        andb.onUpgrade(andb.getWritableDatabase(), 1, 2);

        andb.addAnnouncement(new Announcement("This is an Announcement", "Canopy", "You have reached Canopy and here are a few announcements for you."));
        andb.addAnnouncement(new Announcement("This is an Announcement", "Cafeteria New building", "You have reached Cafeteria New building and here are a few announcements for you."));
        andb.addAnnouncement(new Announcement("This is an Announcement", "Cafeteria old building", "You have reached Cafeteria old building and here are a few announcements for you."));
        andb.addAnnouncement(new Announcement("This is an Announcement", "Side Gate A", "You have reached Side Gate A and here are a few announcements for you."));

        //adb.addAttendance(new Attendance("2015-11-10 20:10:11", "Indah Villa", "Entered"));

        printDatabase();
        annprintDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        LocationServices.GeofencingApi.addGeofences(
                mGoogleApiClient,
                // The GeofenceRequest object.
                getGeofencingRequest(),
                // A pending intent that that is reused when calling removeGeofences(). This
                // pending intent is used to generate an intent when a matched geofence
                // transition is observed.
                getGeofencePendingIntent()
        ).setResultCallback(this); // Result processed in onResult().
        //txtStatus.setText("it should show something here");
        Log.i(TAG, "Connected to GoogleApiClient");

    }



    public void printDatabase(){
        List<Attendance> attendances = adb.dbtoList();
        ArrayList<String> listView = new ArrayList<String>();

        for (Attendance att : attendances) {
            String log = "You "+ att.get_content() +" "+att.get_location() + " on " + att.get_date();
            listView.add(log);
        }

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listView);

        LvLogs.setAdapter(arrayAdapter);
    }

    public void annprintDatabase(){
        List<Announcement> announcement = andb.dbtoList();
        ArrayList<String> listView = new ArrayList<String>();

        for (Announcement ann : announcement) {
            String log = ann.get_title() + "\n" + ann.get_content();
            listView.add(log);
        }

        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listView);

        lvAnnouncement.setAdapter(arrayAdapter);
    }



    @Override
    public void onConnectionSuspended(int i) {

        Log.i(TAG, "Connection suspended");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

    }

    public void populateGeofenceList() {
        for (Map.Entry<String, LatLng> entry : Constants.Sunway_University.entrySet()) {

            mGeofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(entry.getKey())

                            // Set the circular region of this geofence.
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            Constants.GeofenceRadius
                    )

                            // Set the expiration duration of the geofence. This geofence gets automatically
                            // removed after this period of time. since it's never expire, then it won't be removed
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)

                            // Set the transition types of interest. Alerts are only generated for these
                            // transition
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)

                            // Create the geofence.
                    .build());
        }
    }

    private void logSecurityException(SecurityException securityException) {
        Log.e(TAG, "Invalid location permission. " +
                "You need to use ACCESS_FINE_LOCATION with geofences", securityException);
    }


    private GeofencingRequest getGeofencingRequest() {
        /**
         * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
         * Also specifies how the geofence notifications are initially triggered.
         */
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofences(mGeofenceList);

        // Return a GeofencingRequest.
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onResult(Status status) {

        if (status.isSuccess()) {
            // Update state and save in shared preferences.
            mGeofencesAdded = !mGeofencesAdded;
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(Constants.GEOFENCES_ADDED_KEY, mGeofencesAdded);
            editor.commit();
        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    status.getStatusCode());
            Log.e(TAG, errorMessage);
        }

    }
}
