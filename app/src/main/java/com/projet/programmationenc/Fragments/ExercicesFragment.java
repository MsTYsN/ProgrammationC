package com.projet.programmationenc.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.projet.programmationenc.Adapters.AdapterExercices;
import com.projet.programmationenc.Moduls.CardViewExercices;
import com.projet.programmationenc.ui.HomeActivity;
import com.projet.programmationenc.R;

import java.util.ArrayList;
import java.util.List;

public class ExercicesFragment extends Fragment {
    private RecyclerView rvexercices;
    private RecyclerView.Adapter rvadapter;//bridge between data(list) and recyclerview because we can't just load are of our items at once into the recyclerview in order to avoid bad performance
    private RecyclerView.LayoutManager rvmanager;//aligns single items in our list
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Exercices");
        return inflater.inflate(R.layout.fragment_exercices,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Quiz et exercices");
        List<CardViewExercices> cardViewExercicesList = new ArrayList<>();
        cardViewExercicesList.add(new CardViewExercices(R.drawable.ic_code_grray_34dp,"Les bases du langage C","Quiz 1 : Les bases du langage C","Quiz 2 : Les bases du langage C", "Exercice 1 : Les bases du langage C", "Exercice 2 : Les bases du langage C",false));
        cardViewExercicesList.add(new CardViewExercices(R.drawable.ic_all_inclusive_grray_34dp,"Les structures conditionelles et les boucles","Quiz 1 : Les structures conditionelles et les boucles","Quiz 2 : Les structures conditionelles et les boucles", "Exercice 1 : Les structures conditionelles et les boucles", "Exercice 2 : Les structures conditionelles et les boucles",false));
        cardViewExercicesList.add(new CardViewExercices(R.drawable.ic_build_grray_34dp,"Les fonctions, les tableaux et les pointeurs","Quiz 1 : Les fonctions, les tableaux et les pointeurs","Quiz 2 : Les fonctions, les tableaux et les pointeurs", "Exercice 1 : Les fonctions, les tableaux et les pointeurs", "Exercice 2 : Les fonctions, les tableaux et les pointeurs",false));
        cardViewExercicesList.add(new CardViewExercices(R.drawable.ic_widgets_grray_34dp,"Les chaînes de caractères","Quiz 1 : Les chaînes de caractères","Quiz 2 : Les chaînes de caractères", "Exercice 1 : Les chaînes de caractères", "Exercice 2 : Les chaînes de caractères",false));
        cardViewExercicesList.add(new CardViewExercices(R.drawable.ic_device_hub_grray_34dp,"Les structures et les énumérations","Quiz 1 : Les structures et les énumérations","Quiz 2 : Les structures et les énumérations", "Exercice 1 : Les structures et les énumérations", "Exercice 2 : Les structures et les énumérations",false));
        cardViewExercicesList.add(new CardViewExercices(R.drawable.ic_description_grray_34dp,"Les fichiers","Quiz 1 : Les fichiers","Quiz 2 : Les fichiers", "Exercice 1 : Les fichiers", "Exercice 2 : Les fichiers",false));

        rvexercices = view.findViewById(R.id.rvexercices);
        rvexercices.setHasFixedSize(true);
        rvmanager = new LinearLayoutManager(getActivity());
        rvadapter = new AdapterExercices(cardViewExercicesList,getActivity());

        rvexercices.setLayoutManager(rvmanager);
        rvexercices.setAdapter(rvadapter);
    }
}
