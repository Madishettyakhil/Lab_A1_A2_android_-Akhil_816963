package com.android.androidassignment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView product;
    SearchView searchView;

    ProductDBHelper dbHelper;
    ArrayList<Integer> id = new ArrayList<>();
    ArrayList<String> productname = new ArrayList<String>();
    ArrayList<String> productprice = new ArrayList<String>();

    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
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

        id = dbHelper.getid();
        productname = dbHelper.getProduct();
        productprice = dbHelper.getProductPrice();

        dbHelper.close();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        product = view.findViewById(R.id.product);
        searchView = view.findViewById(R.id.search);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false);
        product.setLayoutManager(layoutManager);
        ProductRecyclerviewAdapter productRecyclerviewAdapter = new
                ProductRecyclerviewAdapter(id,productname,productprice);
        product.setAdapter(productRecyclerviewAdapter);
        product.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                productRecyclerviewAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productRecyclerviewAdapter.getFilter().filter(newText);
                return false;
            }
        });

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
                alertdialog.setTitle("Delete Item");
                alertdialog.setMessage("Clicking on yes will delete the item");
                alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteProductbyid(Integer.parseInt(String.valueOf(id.get(index))));
                        id.remove(index);
                        productname.remove(index);
                        productprice.remove(index);
                        productRecyclerviewAdapter.notifyItemRemoved(index);
                    }
                });
                alertdialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        productRecyclerviewAdapter.notifyDataSetChanged();
                    }
                });
                alertdialog.create().show();

                dbHelper.close();

            }
        };

        ItemTouchHelper ith = new ItemTouchHelper(itsc);
        ith.attachToRecyclerView(product);
    }
}