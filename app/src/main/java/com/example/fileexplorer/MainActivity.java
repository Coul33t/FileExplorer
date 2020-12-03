package com.example.fileexplorer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {

    RecyclerViewAdapter adapter;
    ArrayList<File> fileList;
    ArrayList<String> dirList;
    String currentPath;
    private static final File ROOTDIR = Environment.getExternalStorageDirectory();

    // Tag for information log
    private static final String TAG = "MYTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentPath = "";
        getFileAndStrList(currentPath);

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.directoriesView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(this, dirList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void getFileAndStrList(String path) {
        fileList = getFileList(path);
        dirList = fileListToStringList(fileList);
    }

    private ArrayList<File> getFileList(String path) {
        ArrayList<File> fileList = new ArrayList<File>();

        File root;

        root = new File(ROOTDIR + path);

        File[] files = root.listFiles();

        if (files != null) {
            for (File inFile : files) {
                if (inFile.isDirectory()) {
                    fileList.add(inFile);
                }
            }
        }

        else {
            //Toast.makeText(this, "Folder " + path + " does not exist.", Toast.LENGTH_SHORT).show();
        }

        return fileList;
    }

    private ArrayList<String> fileListToStringList(ArrayList<File> fileList) {
        ArrayList<String> strList = new ArrayList<String>();

        if (!currentPath.isEmpty())
            strList.add("Return");

        for (File file: fileList) {
            strList.add(file.getName());
        }

        return strList;
    }

    @Override
    public void onItemClick(View view, int position) {
        if (adapter.getItem(position).equals("Return")) {
            try {
                currentPath = currentPath.substring(0, currentPath.lastIndexOf("/"));
            } catch (IndexOutOfBoundsException e) {
                currentPath = "";
            }
        }

        else
            currentPath += adapter.getItem(position);

        getFileAndStrList(currentPath);

        adapter.changeData(this.dirList);
        adapter.notifyDataSetChanged();
    }
}