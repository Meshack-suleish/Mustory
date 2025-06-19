//package com.mustory;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.app.ProgressDialog;
//import android.content.ContentResolver;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.media.MediaPlayer;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.provider.MediaStore;
//import android.renderscript.ScriptGroup;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//public class AdminActivity extends AppCompatActivity implements MusicAdapter.OnDownloadClickListener{
//
//    private static final int PICK_MUSIC_REQUEST = 2;
//    private static final int REQUEST_PERMISSION_CODE = 1;
//
//    private ListView listView;
//    private Button addMusicButton;
//    private DatabaseReference databaseReference;
//    ProgressDialog progressdialog;
//    private ProgressBar progressBar;
//    ValueEventListener valueEventListener;
//
//    private ArrayList<String> songNames;  // List for song names
//    private ArrayList<String> songPathMap;// Map for storing song paths with song names
//    private ArrayList<Integer>songNumbers;
//    private ArrayList<String> songDescriptionMap;  // Stores song description
//    private ArrayList<String> artistNames;
//    private MusicAdapter simpleMusicAdapter;
//
//
//    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_admin);
//
//        listView = findViewById(R.id.playlist);
//         addMusicButton = findViewById(R.id.add_music_button);
//        progressBar = findViewById(R.id.progressBar);
//
//
//        songNames = new ArrayList<>();
//        songPathMap = new ArrayList<>();
//        songDescriptionMap = new ArrayList<>();
//        artistNames = new ArrayList<>();
//        songNumbers = new ArrayList<>();
//        simpleMusicAdapter = new MusicAdapter(this, songNames,songDescriptionMap,songPathMap,artistNames,songNumbers,this);
//        listView.setAdapter(simpleMusicAdapter);
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("songs");
//        //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//
//
//        if(isOnline()) {
//            showProgressBar();
//            loadSongFromFirebase();
//
//        }
//        else{
//            loadSongsFromMediaStore();
//            Toast.makeText(AdminActivity.this,"YOU ARE OFFLINE",Toast.LENGTH_SHORT).show();
//        }
//
//
//
//        // Check permissions
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
//        }
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
//        }
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            // Check if the READ_MEDIA_AUDIO permission is granted
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO)
//                    != PackageManager.PERMISSION_GRANTED) {
//                // If permission is not granted, request it
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.READ_MEDIA_AUDIO},
//                        REQUEST_PERMISSION_CODE);
//            }
//        }
//
//
//
//
//
//            addMusicButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openFilePicker();
//            }
//        });
//
//
//
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String selectedSongName = songNames.get(position);
//                String selectedSongPath = songPathMap.get(position);
//                String selectedSongDescription = songDescriptionMap.get(position);  // Get the description
//                String selectedArtistName = artistNames.get(position);
//                showProgressBar();
//
//                // Start PlayerActivity and pass the song path
//                File songFile = new File(selectedSongPath);
//                Intent intent = new Intent(AdminActivity.this, PlayerActivity.class);
//                intent.putStringArrayListExtra("songNames",songNames);
//                intent.putStringArrayListExtra("songPaths",songPathMap);
//                intent.putStringArrayListExtra("songDescriptions",songDescriptionMap);
//                intent.putStringArrayListExtra("artistNames",artistNames);
//                intent.putExtra("position",position);
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        hideProgressBar();
//                        startActivity(intent);
//                    }
//                }, 500);
//
//
//            }
//        });
//
//    }
//
//    private void openFilePicker() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, PICK_MUSIC_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_MUSIC_REQUEST && resultCode == RESULT_OK) {
//            Uri songUri = data.getData();
//            String songPath = getRealPathFromURI(songUri);
//            String songName = getSongNameFromURI(songUri);
//
//            if (songUri != null && songName != null) {
//                // Show a dialog to get description from the user
//                showAddSongDialog(songName, songUri);
//            } else {
//                Toast.makeText(this, "Error retrieving song", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void showAddSongDialog(String songName, Uri songUri) {
//        // Create an AlertDialog to input a description
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View dialogView = inflater.inflate(R.layout.alert_dialog,null);
//
//        EditText song_description = dialogView.findViewById(R.id.song_description);
//        EditText artist_name = dialogView.findViewById(R.id.artist_name);
//        EditText songNumberInput = dialogView.findViewById(R.id.song_Number);
//
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Add a description");
//        builder.setView(dialogView);
//
//
//        // Set up the buttons
//        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String description = song_description.getText().toString().trim();
//                String artistName = artist_name.getText().toString().trim();
//                String song_number = songNumberInput.getText().toString().trim();
//                int songNumber;
//                try {
//                    songNumber = Integer.parseInt(song_number);
//                } catch (NumberFormatException e) {
//                    Toast.makeText(getApplicationContext(), "Song number must be a valid integer", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // Store song name, path, and description
//                uploadSongToFirebaseStorage(songUri,songName, description,artistName,songNumber);
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        builder.create().show();
//    }
//
//
//    private void uploadSongToFirebaseStorage(Uri songUri, String songName, String description,String artistName,int songNumber) {
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference().child("songs/" + songName);
//        progressdialog = new ProgressDialog(this);
//        progressdialog.setTitle("Uploading...");
//        progressdialog.show();
//        storageRef.putFile(songUri)
//                .addOnSuccessListener(taskSnapshot -> {
//                    // Retrieve the download URL
//                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                        String downloadUrl =uri.toString();
//                        // Now add the song info to Firebase Realtime Database
//                        addSongToList(songName, downloadUrl, description,artistName,songNumber);
//                        if(progressdialog.isShowing()) {
//                            progressdialog.dismiss();
//                        }
//                    });
//                })
//                .addOnFailureListener(e -> {
//                    if(progressdialog.isShowing()){
//                        progressdialog.dismiss();
//                    }
//                    Toast.makeText(AdminActivity.this, "Failed to upload song to Firebase Storage", Toast.LENGTH_SHORT).show();
//                });
//    }
//
//
//    private void addSongToList(String songName, String downloadUrl, String description,String artistName,int songNumber) {
//
//        Songs newSong = new Songs(songName,downloadUrl,description,artistName,songNumber);
//        databaseReference.push().setValue(newSong)
//                .addOnCompleteListener(task -> {
//                    if(task.isSuccessful()){
//                        if(progressdialog.isShowing()){
//                            progressdialog.dismiss();
//                        }
//                        Toast.makeText(AdminActivity.this,"NewSong Added",Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        if(progressdialog.isShowing()){
//                            progressdialog.dismiss();
//                        }
//                        Toast.makeText(AdminActivity.this,"Failed to Add a Song",Toast.LENGTH_SHORT).show();
//                    }
//                });
//     /* databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//        Songs song;
//    @Override
//    public void onDataChange(@NonNull DataSnapshot snapshot) {
//      if(!snapshot.exists()){
//          Toast.makeText(MainActivity.this,"NO song found",Toast.LENGTH_SHORT).show();
//      }
//      else{
//          for(DataSnapshot data:snapshot.getChildren()){
//              song = data.getValue(Songs.class);
//              // Add song name and path with description to the list and map
//              assert song != null;
//              songNames.add(song.getSongName());
//              songPathMap.add(song.getsongUri());
//              songDescriptionMap.add(song.getDescription());  // Store the description in a map
//              adapter.notifyDataSetChanged();   // Update ListView
//          }
//      }
//    }
//
//    @Override
//    public void onCancelled(@NonNull DatabaseError error) {
//
//    }
//   });*/
//
//    }
//
//    private void showProgressBar() {
//        progressBar.setVisibility(View.VISIBLE);
//    }
//
//    // Method to hide ProgressBar
//    private void hideProgressBar() {
//        progressBar.setVisibility(View.GONE);
//    }
//
//
//    private String getRealPathFromURI(Uri contentUri) {
//        String[] projection = {MediaStore.Audio.Media.DATA};
//        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
//        if (cursor != null) {
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
//            cursor.moveToFirst();
//            String result = cursor.getString(column_index);
//            cursor.close();
//            return result;
//        }
//        return null;
//    }
//
//    private String getSongNameFromURI(Uri contentUri) {
//        String[] projection = {MediaStore.Audio.Media.TITLE};  // Get the song title
//        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
//        if (cursor != null) {
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
//            cursor.moveToFirst();
//            String songName = cursor.getString(column_index);
//            cursor.close();
//            return songName;
//        }
//        return null;
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_PERMISSION_CODE) {
//            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    public void loadSongFromFirebase(){
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener(){
//            Songs song;
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                hideProgressBar();
//                songNames.clear();
//                songDescriptionMap.clear();
//                songPathMap.clear();
//                artistNames.clear();
//                songNumbers.clear();
//                if(!snapshot.exists()){
//                    Toast.makeText(AdminActivity.this,"NO song found",Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    for(DataSnapshot data:snapshot.getChildren()){
//                        song = data.getValue(Songs.class);
//                        assert song != null;
//                        songNames.add(song.getSongName());
//                        songPathMap.add(song.getsongUri());
//                        songDescriptionMap.add(song.getDescription());
//                        artistNames.add(song.getArtistName());
//                        songNumbers.add(song.getSongNumber());
//
//                    }
//                    simpleMusicAdapter.notifyDataSetChanged();
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(AdminActivity.this,"Failed to Load Songs",Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//
//    @Override
//    public void onDownloadClick(String songUrl, String songName, String description,String artistName,int songNumber) {
//
//        downloadSongFromFirebase(songUrl, songName, description,artistName,songNumber);
//    }
//
//
//    private void downloadSongFromFirebase(String firebaseUrl, String songName, String description,String artistName,int songNumber) {
//
//        if (isSongAlreadyDownloaded(songName)) {
//            Toast.makeText(AdminActivity.this, "Song already downloaded!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReferenceFromUrl(firebaseUrl);
//
//        // Define a local path where the song will be saved
//        File localFile = new File(getExternalFilesDir(null), songName);
//
//        progressdialog = new ProgressDialog(this);
//        progressdialog.setTitle("Downloading...");
//        progressdialog.show();
//
//        storageRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
//            if (progressdialog.isShowing()) {
//                progressdialog.dismiss();
//            }
//
//            Toast.makeText(AdminActivity.this, "Song downloaded successfully", Toast.LENGTH_SHORT).show();
//
//            // Store the local file path and song description for offline use
//            saveLocalSongInfoToMediaStore(songName, localFile.getAbsolutePath(), description,artistName,songNumber);
//            saveDownloadedSong(songName, localFile.getAbsolutePath());
//
//
//        }).addOnFailureListener(e -> {
//            if (progressdialog.isShowing()) {
//                progressdialog.dismiss();
//            }
//            Toast.makeText(AdminActivity.this, "Failed to download song", Toast.LENGTH_SHORT).show();
//        });
//    }
//
//
//    private void saveLocalSongInfoToMediaStore(String songName, String localPath, String description, String artistName,int songNumber) {
//        // Content values for MediaStore insertion
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(MediaStore.Audio.Media.DISPLAY_NAME, songName); // Song name
//        contentValues.put(MediaStore.Audio.Media.TITLE, songName);       // Title
//        contentValues.put(MediaStore.Audio.Media.ARTIST, artistName);
//        contentValues.put(MediaStore.Audio.Media.DISC_NUMBER, songNumber);
//        contentValues.put(MediaStore.Audio.Media.COMPOSER, description);
//        contentValues.put(MediaStore.Audio.Media.MIME_TYPE, "audio/mpeg"); // MIME type for MP3
//        contentValues.put(MediaStore.Audio.Media.RELATIVE_PATH, "Music/MyApp"); // Directory in Music folder
//
//        // Insert the song into MediaStore
//        ContentResolver resolver = getContentResolver();
//        Uri uri = resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues);
//
//        if (uri != null) {
//            try {
//                // Write the file into the MediaStore
//                FileInputStream inputStream = new FileInputStream(localPath);
//                OutputStream outputStream = resolver.openOutputStream(uri);
//                if (outputStream != null) {
//                    byte[] buffer = new byte[1024];
//                    int length;
//                    while ((length = inputStream.read(buffer)) > 0) {
//                        outputStream.write(buffer, 0, length);
//                    }
//                    inputStream.close();
//                    outputStream.close();
//                    Toast.makeText(this, "Song saved to MediaStore", Toast.LENGTH_SHORT).show();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                Toast.makeText(this, "Error saving to MediaStore", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(this, "Error inserting song into MediaStore", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//
//
//    // Save song info for offline use
//    private void saveLocalSongInfo(String songName, String localPath, String description, String artistName) {
//        DatabaseHelper dbHelper = new DatabaseHelper(this);
//        dbHelper.insertSong(songName, localPath, description,artistName);
//    }
//
//    private boolean isOnline() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        return networkInfo != null && networkInfo.isConnected();
//    }
//
//
//    private void loadSongsFromMediaStore() {
//        // Define the columns to retrieve
//        String[] projection = {
//                MediaStore.Audio.Media._ID,           // Unique ID for the audio file
//                MediaStore.Audio.Media.DISPLAY_NAME, // File name
//                MediaStore.Audio.Media.ARTIST,       // Artist name
//                MediaStore.Audio.Media.DATA,// Full file path
//                MediaStore.Audio.Media.DISC_NUMBER
//        };
//
//        // Filter to match only audio files in the "Music/MyApp" folder
//        String selection = MediaStore.Audio.Media.DATA + " LIKE ?";
//        String[] selectionArgs = new String[]{"%Music/MyApp%"};
//
//        // Order results by date added (most recent first)
//        String sortOrder = MediaStore.Audio.Media.DATE_ADDED + " DESC";
//
//        // Query the MediaStore
//        Cursor cursor = getContentResolver().query(
//                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, // URI for external audio files
//                projection,
//                selection,
//                selectionArgs,
//                sortOrder
//        );
//
//        if (cursor != null) {
//            // Clear previous song data
//            songNames.clear();
//            songPathMap.clear();
//            songDescriptionMap.clear();
//            artistNames.clear();
//            songNumbers.clear();
//
//            // Process the query results
//            while (cursor.moveToNext()) {
//                String songName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
//                String artistName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
//                String songPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
//                //String description = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.COMPOSER));
//                int songNo = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISC_NUMBER));
//                // Add data to the lists
//                songNames.add(songName);
//                artistNames.add(artistName);
//                songPathMap.add(songPath);
//                songNumbers.add(songNo);
//                songDescriptionMap.add("TUNE iN!"); // Placeholder for description
//            }
//            cursor.close();
//
//            // Notify the adapter about the updated data
//            simpleMusicAdapter.notifyDataSetChanged();
//        } else {
//            Toast.makeText(this, "No songs found in the specified folder", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//
//    public void saveDownloadedSong(String songName, String songPath) {
//        SharedPreferences sharedPreferences = getSharedPreferences("DownloadedSongs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(songName, songPath);  // Save the song name and its file path
//        editor.apply();  // Commit the changes
//    }
//
//
//    public boolean isSongAlreadyDownloaded(String songName) {
//        SharedPreferences sharedPreferences = getSharedPreferences("DownloadedSongs", MODE_PRIVATE);
//        String songPath = sharedPreferences.getString(songName, null);  // Retrieve the stored path
//        return songPath != null;  // If song path exists, the song is already downloaded
//    }
//
//
//
//   /* private void loadSongFromLocalStorage() {
//        try {
//            DatabaseHelper dbHelper = new DatabaseHelper(this);
//            List<Songs> downloadedSongs = dbHelper.getAllDownloadedSongs();
//
//            if (downloadedSongs.isEmpty()) {
//                Toast.makeText(AdminActivity.this, "NO song found", Toast.LENGTH_SHORT).show();
//            }
//
//            songNames.clear();
//            songPathMap.clear();
//            songDescriptionMap.clear();
//            artistNames.clear();
//
//            for (Songs song : downloadedSongs) {
//                hideProgressBar();
//                songNames.add(song.getSongName());
//                songPathMap.add(song.getsongUri());
//                songDescriptionMap.add(song.getDescription());
//                artistNames.add(song.getArtistName());
//
//            }
//
//            simpleMusicAdapter.notifyDataSetChanged();
//        }
//        catch (Exception e){
//            Toast.makeText(AdminActivity.this,"Error",Toast.LENGTH_SHORT).show();
//        }
//    }*/
//
//}
