package br.ufv.dpi.inf311.pratica5.view;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import br.ufv.dpi.inf311.pratica5.BancoDados;
import br.ufv.dpi.inf311.pratica5.R;
import br.ufv.dpi.inf311.pratica5.model.Categoria;
import br.ufv.dpi.inf311.pratica5.model.Local;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocalFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private List<Local> locais;
    BancoDados bd;
    public LocalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocalFragment newInstance(String param1, String param2) {
        LocalFragment fragment = new LocalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = BancoDados.getInstance();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_local, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAnimation();
        recyclerView.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        carregaLocais();
        recyclerView.setAdapter(new LocalAdapter(locais,getContext(),onClickLocal()));
    }
    private void carregaLocais(){
        locais = new ArrayList<>();
        //,"qtdVisitadas","latitude","longitude"
        Cursor c = bd.buscar("Checkin",new String[]{"Local","qtdVisitas","cat","latitude","longitude"},"","");

        while(c.moveToNext()){
            int local = c.getColumnIndex("Local");
            int qtd = c.getColumnIndex("qtdVisitas");
            int latitude = c.getColumnIndex("latitude");
            int longitude = c.getColumnIndex("longitude");
            int cat = c.getColumnIndex("cat");
            Local l = new Local(c.getString(local), c.getInt(qtd), c.getString(latitude), c.getString(longitude),null);
            String msg = String.format("Local: "+l);
            Log.i("LOCATION",msg);
        }
        Log.i("LOCATION","Nº de loacais visitados: "+locais.size());
    }
    private LocalAdapter.LocalOnClickListener onClickLocal(){
        return new LocalAdapter.LocalOnClickListener() {
            @Override
            public void onClickLocal(View view, int idx) {
                Local local = locais.get(idx);
                Toast.makeText(getContext(),"Local: "+local,Toast.LENGTH_LONG).show();
            }
        };
    }
}