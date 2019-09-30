package com.vila.testmobileintive.logic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vila.testmobileintive.R;
import com.vila.testmobileintive.model.Person;

import java.util.List;


public class PersonPagedAdapter extends PagedListAdapter<Person,PersonPagedAdapter.Holder>
{
    private Context contexto;
    private OnPersonPagedAdapterListener listener;


    public interface OnPersonPagedAdapterListener
    {
        void personSelected(int Position, View view);
    }


    public void setOnPersonPagedAdapterListener(OnPersonPagedAdapterListener listener )
    {
        this.listener = listener;
    }



    public PersonPagedAdapter(Context contexto)
    {
        super(DIFF_CALLBACK);
        this.contexto = contexto;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person,parent,false);

        return new Holder(view,listener);
    }


    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position)
    {
        Person person = getItem(position);

        if (person != null && person.getPicture() != null)
        {
            Glide.with(contexto)
                    .load(person.getPicture().getThumbnail())
                    .crossFade()
                    .centerCrop()
                    .into(holder.imageView);
            holder.imageView.setTransitionName(person.getEmail());
        }
    }


    private static DiffUtil.ItemCallback<Person> DIFF_CALLBACK = new DiffUtil.ItemCallback<Person>()
    {
        @Override
        public boolean areItemsTheSame(@NonNull Person oldItem, @NonNull Person newItem)
        {
            return oldItem.getLogin().getUsername().equals(newItem.getLogin().getUsername());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Person oldItem, @NonNull Person newItem)
        {
            return oldItem.equals(newItem);
        }
    };

    public void setPersonList(PagedList<Person> listP)
    {
        this.submitList(listP);
        this.notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder
    {
        private ImageView imageView;

         Holder(@NonNull View itemView,OnPersonPagedAdapterListener listener)
        {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_person_imageview);

            imageView.setOnClickListener(v ->
            {
                if (listener!=null)
                    listener.personSelected(getAdapterPosition(),imageView);
            });
        }
    }
}
