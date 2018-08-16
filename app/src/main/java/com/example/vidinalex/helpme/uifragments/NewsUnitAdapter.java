package com.example.vidinalex.helpme.uifragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vidinalex.helpme.R;
import com.example.vidinalex.helpme.datatypes.NewsDateFormat;
import com.example.vidinalex.helpme.managers.DatabaseManager;
import com.example.vidinalex.helpme.managers.PermissionManager;
import com.example.vidinalex.helpme.utils.GlobalVars;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

public class NewsUnitAdapter extends RecyclerView.Adapter<NewsUnitAdapter.ListItemViewHolder> {

    private List<NewsDateFormat> list;
    private StorageReference storageRef;

    public NewsUnitAdapter(List<NewsDateFormat> list)
    {
        super();
        if (list == null) {
            throw new IllegalArgumentException(
                    "list must not be null");
        }
        this.list = list;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListItemViewHolder holder, int position) {
        NewsDateFormat newsDateFormat = list.get(position);
        final NewsUnit newsUnit = new NewsUnit(newsDateFormat.getDate());



        if(newsDateFormat.getLoadFrom() == NewsUnit.POST_LOAD_FROM_CLOUD)
        {
            new DatabaseManager().assembleNewsUnit(newsUnit);
            GlobalVars.getContext().registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    putNewsUnitToHolder(holder,newsUnit);
                    storageRef = FirebaseStorage.getInstance().getReference().
                            child("newsImages/" + newsUnit.imagesArrayList.get(0));
                    GlobalVars.getContext().unregisterReceiver(this);
                }
            }, new IntentFilter(DatabaseManager.ACTION_NEWS_UNIT_DATA_READY + " "
                    + newsDateFormat.getDate()));

            //TODO закэшировать

            if(PermissionManager.checkReadAndWritePermission()) {
//                saveFile(Uri.fromFile(new File(getPreviewImagePath(newsUnit))).toString()+".png");
//                Log.d("NewsUnitAdapter", Thread.currentThread().getName() + ": saved file " + getPreviewImagePath(newsUnit));
//                Log.d("NewsUnitAdapter", Thread.currentThread().getName() + ": loading file " + Uri.fromFile(new File(getPreviewImagePath(newsUnit))));
//                holder.imageView.setImageURI(Uri.fromFile(new File(getPreviewImagePath(newsUnit)+".png")));
                Glide.with(GlobalVars.getContext())
                        .using(new FirebaseImageLoader())
                        .load(storageRef)
                        .into(holder.imageView);

            }
            else
            {
                Glide.with(GlobalVars.getContext())
                        .using(new FirebaseImageLoader())
                        .load(storageRef)
                        .into(holder.imageView);
                Log.d("Image", "No permission to write");
            }
        }
        else
        {
//            newsUnit = FileManager.assembleNewsUnit(newsDateFormat.getDate());

            holder.imageView.setImageURI(Uri.fromFile(new File(getPreviewImagePath(newsUnit))));
            Log.d("Image:Read from cache", getPreviewImagePath(newsUnit));
        }


    }


    private void putNewsUnitToHolder(ListItemViewHolder holder, NewsUnit newsUnit)
    {
        holder.head.setText(newsUnit.head);
        holder.date.setText(newsUnit.date);
        holder.body.setText(newsUnit.body);
        Log.d("NewsUnitAdaper", "head: " + newsUnit.head);
        Log.d("NewsUnitAdaper", "date: " + newsUnit.date);
        Log.d("NewsUnitAdaper", "body: " + newsUnit.body);
    }

    private String getPreviewImagePath(NewsUnit newsUnit)
    {
        return GlobalVars.getFileSavingPath() + newsUnit.imagesArrayList.get(0);
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.news_preview_element,
                        parent,
                        false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ListItemViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView date;
        TextView head;
        TextView body;

        public ListItemViewHolder(View item)
        {
            super(item);
            imageView = item.findViewById(R.id.image);
            date = item.findViewById(R.id.date);
            head = item.findViewById(R.id.head);
            body = item.findViewById(R.id.body);

        }
    }

//TODO допилить сейв файлов
    private void saveFile(String path)
    {
        final File file = new File(path);

        storageRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.d("File saved", file.getAbsolutePath());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("File Failed","");
            }
        });
    }

}
