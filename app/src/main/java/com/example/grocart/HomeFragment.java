package com.example.grocart;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    public View onCreateView(LayoutInflater i,ViewGroup c,Bundle b){

        View v=i.inflate(R.layout.fragment_home,c,false);

        LinearLayout veg=v.findViewById(R.id.categoryVeg);
        EditText search=v.findViewById(R.id.searchBar);

        veg.setOnClickListener(x->open("veg"));

        search.setOnEditorActionListener((a,b1,c1)->{
            open(search.getText().toString());
            return true;
        });

        return v;
    }

    void open(String c){
        Intent i=new Intent(getActivity(),CategoryActivity.class);
        i.putExtra("category",c);
        startActivity(i);
    }
}