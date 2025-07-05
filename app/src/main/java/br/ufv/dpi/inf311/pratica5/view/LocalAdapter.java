package br.ufv.dpi.inf311.pratica5.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.ufv.dpi.inf311.pratica5.R;
import br.ufv.dpi.inf311.pratica5.model.Local;

public class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.LocalViewHolder> {
    protected static final String TAG = "Pratica5";
    private final List<Local> locais;
    private final Context context;
    private LocalOnClickListener localOnClickListener;

    public LocalAdapter(List<Local> locais, Context context, LocalOnClickListener localOnClickListener) {
        this.locais = locais;
        this.context = context;
        this.localOnClickListener = localOnClickListener;
    }

    public interface LocalOnClickListener{
        public void onClickLocal(View view,int idx);
    }
    @NonNull
    @Override
    public LocalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_local, parent, false);
        return new LocalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalViewHolder holder,int position) {
        Local local = locais.get(position);
        holder.tNome.setText(local.getNome());
        if(localOnClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    localOnClickListener.onClickLocal(holder.itemView,holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return locais!= null?locais.size():0;
    }

    public static class LocalViewHolder extends RecyclerView.ViewHolder{
        public TextView tNome;
        ImageView img;
        public LocalViewHolder(@NonNull View itemView) {
            super(itemView);
            tNome = itemView.findViewById(R.id.nome);
        }
    }
}
