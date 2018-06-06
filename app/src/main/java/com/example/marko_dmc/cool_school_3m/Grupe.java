package com.example.marko_dmc.cool_school_3m;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.ScrollView;

public class Grupe extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate( R.layout.grupe, container, false );


        ScrollView scrol=rootView.findViewById( R.id.scrol );

        Button bHrvatski = rootView.findViewById(R.id.idHrvatski);
        bHrvatski.getBackground().setAlpha(100);
        Button bMatematika = rootView.findViewById(R.id.idMatematika);
        Button bLikovni = rootView.findViewById(R.id.idLikovni);
        Button bPrirodaIDrustvo = rootView.findViewById(R.id.idPrirodaIDrustvo);
        Button bEngleski = rootView.findViewById(R.id.idEngleski);
/*

        bHrvatski.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Hrvatski.class);
                startActivity(i);
            }
        });

        bMatematika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(MainActivity.this, Matematika.class);
                startActivity(ii);
            }
        });

        bEngleski.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iii = new Intent(MainActivity.this, Engleski.class);
                startActivity(iii);
            }
        }); */


        return rootView;


    }
}
