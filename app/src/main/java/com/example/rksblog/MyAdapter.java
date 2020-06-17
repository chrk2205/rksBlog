package com.example.rksblog;


import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingDeque;


class MyAdapter extends RecyclerView.Adapter<MyAdapter.viewHolder> {




    private Context context;
    private List<blogGet> list=new ArrayList<>();
    onclick monclick;
    private int count;

    class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView userImage;
        ImageView blogImage;
        TextView userName,Description;
        onclick onclick;

        public viewHolder(@NonNull View itemView,onclick onclick) {
            super(itemView);
            blogImage=itemView.findViewById(R.id.blogImage);
            userName=itemView.findViewById(R.id.UserName);
            Description=itemView.findViewById(R.id.desc);
            userImage=itemView.findViewById(R.id.imageView);
            this.onclick=onclick;


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                            onclick.onBlogClick(getAdapterPosition());
        }
    }
    public MyAdapter(){}
    public MyAdapter(Context context) {
        this.context=context;


    }
    public MyAdapter(Context context,List<blogGet>l,onclick monclick) {
        //this.list.clear();
        this.list.addAll(l);
        this.context=context;
        this.monclick=monclick;

    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(context).inflate(R.layout.blog,parent,false);
        return new viewHolder(v,monclick);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, int position) {

         Thread thread= new Thread(new putpictures(list.get(position).getBlogimage(),holder.blogImage));
         thread.run();
         Thread t=new Thread(new putpictures(list.get(position).getUserimage(),holder.userImage));
         t.run();

        holder.Description.setText(list.get(position).getCaption());


        //for username and user dp
        // databaseReference;
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("AU").child(list.get(position).getUserid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                unandimg k=(dataSnapshot.getValue(unandimg.class));
                if (k != null) {
                    Thread thread1=new Thread(new putpictures(k.getDp(),holder.userImage));
                    thread1.run();
                }
                if (k != null) {
                    holder.userName.setText(k.getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }


    public void datasetchanged(List<blogGet> list ){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }


    public interface onclick
    {
        void onBlogClick(int position);
    }




    public class putpictures implements Runnable {

        String url;
        ImageView blogImage;


        public putpictures(String url, ImageView blogImage) {

            this.url=url;
            this.blogImage=blogImage;

        }

        @Override
        public void run() {

            Picasso.get().load(url).into(blogImage);


        }
    }


}
