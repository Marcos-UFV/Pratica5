package br.ufv.dpi.inf311.pratica5.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.ufv.dpi.inf311.pratica5.R;
import br.ufv.dpi.inf311.pratica5.model.Local;

public class LocalAdapterBasic extends BaseAdapter {
    List<Local> locais;
    Context context;

    public LocalAdapterBasic(List<Local> locais, Context context) {
        this.locais = locais;
        this.context = context;
    }

    @Override
    public int getCount() {
        return locais != null?locais.size():0;
    }

    @Override
    public Object getItem(int i) {
        return locais.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_relatorio,viewGroup,false);
        }
        ((TextView) view.findViewById(R.id.nomeLocal)).setText(locais.get(i).getNome());
        ((TextView) view.findViewById(R.id.qtdVisitas)).setText(String.valueOf(locais.get(i).getQtdVisitas()));

        return view;
    }
}
