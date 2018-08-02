package com.example.vidinalex.helpme.uifragments;

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
import com.example.vidinalex.helpme.utils.GlobalVars;
import com.example.vidinalex.helpme.utils.PermissionManager;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ListItemViewHolder> {

    private List<NewsPreviewElementView> list;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    public NewsRecyclerViewAdapter(List<NewsPreviewElementView> list)
    {
        if (list == null) {
            throw new IllegalArgumentException(
                    "list must not be null");
        }
        this.list = list;
    }

    @Override
    public void onBindViewHolder(@NonNull final ListItemViewHolder holder, int position) {
        NewsPreviewElementView newsPreviewElementView = list.get(position);
        holder.head.setText(newsPreviewElementView.head);
        holder.date.setText(newsPreviewElementView.date);
        holder.body.setText(newsPreviewElementView.body);

        ArrayList<String> arrayList = newsPreviewElementView.imagesArrayList;

        String fileName = arrayList.get(0);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("newsImages").child(fileName);
        String path = GlobalVars.getFileSavingPath() + /*"news"
                + File.separator + "images" +*/ File.separator + fileName;


        if(newsPreviewElementView.loadFrom == NewsPreviewElementView.POST_LOAD_FROM_CLOUD)
        {
            if(PermissionManager.checkReadAndWritePermission()) {
                saveFile(path);
                holder.imageView.setImageURI(Uri.fromFile(new File(path)));
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
            holder.imageView.setImageURI(Uri.fromFile(new File(path)));
            Log.d("Image:Read from cache", path);
        }

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


    private void saveFile(String path)
    {
        final File file = new File(path);
        storageRef.getFile(file).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                    Log.d("File saved", file.getAbsolutePath());
                else
                    Log.d("File Failed","");
            }
        });
    }

}
