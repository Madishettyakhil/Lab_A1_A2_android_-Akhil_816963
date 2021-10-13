package com.android.androidassignment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProviderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProviderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ProductDBHelper dbHelper;
    ArrayList<String> providerlist = new ArrayList<String>();
    ArrayList<String> providerlist2 = new ArrayList<String>();
    RecyclerView provider;

    public ProviderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProviderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProviderFragment newInstance(String param1, String param2) {
        ProviderFragment fragment = new ProviderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        dbHelper = new ProductDBHelper(getContext());
        dbHelper.openDataBase();

        providerlist = dbHelper.getProvider();

        dbHelper.close();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_provider, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Set<String> providerset = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        providerset.addAll(providerlist);
        providerlist2.clear();
        providerlist2.addAll(providerset);

        provider = view.findViewById(R.id.provider);
        ProviderRecyclerviewAdapter providerRecyclerviewAdapter = new ProviderRecyclerviewAdapter(providerlist,
                providerlist2);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false);
        provider.setLayoutManager(layoutManager);
        provider.setAdapter(providerRecyclerviewAdapter);
        provider.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        ItemTouchHelper.SimpleCallback itsc = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                dbHelper = new ProductDBHelper(getContext());

                final int index = viewHolder.getAdapterPosition();

                AlertDialog.Builder alertdialog = new AlertDialog.Builder(getContext());
                alertdialog.setTitle("Delete Provider");
                alertdialog.setMessage("Clicking on yes will delete the provider");
                alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteProductbyprovider(providerlist2.get(index));
                        providerlist2.remove(index);
                        providerlist = dbHelper.getProvider();
                        providerRecyclerviewAdapter.notifyItemRemoved(index);
                    }
                });
                alertdialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        providerRecyclerviewAdapter.notifyDataSetChanged();
                    }
                });
                alertdialog.create().show();

                dbHelper.close();

            }
        };

        ItemTouchHelper ith = new ItemTouchHelper(itsc);
        ith.attachToRecyclerView(provider);

    }
}