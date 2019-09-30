package com.vila.testmobileintive.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.vila.testmobileintive.R;
import com.vila.testmobileintive.logic.Constantes;
import com.vila.testmobileintive.logic.PersonPagedAdapter;
import com.vila.testmobileintive.model.Person;
import com.vila.testmobileintive.viewmodel.MainViewModel;





public class ListFragment extends Fragment
{
    private MainViewModel viewModel;
    private RecyclerView recyclerView;
    private int numberColumns;
    private PersonPagedAdapter adapter;
    private ProgressBar progressBar;

    public ListFragment()
    {

    }


    static ListFragment newInstance()
    {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


      //  setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.my_move));
        setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.grid_exit_transition));
        setSharedElementEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.my_move));
        setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.grid_exit_transition));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = view.findViewById(R.id.listFragment_recyclerView);
        progressBar = view.findViewById(R.id.listFragment_progressbar);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        init();
        configObserver();
        configRecyclerView();
    }



    private void init()
    {
        if (getActivity()!=null)
        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

//        if (getResources().getBoolean(R.bool.isLandscape))
//        {
//            numberColumns = 5;
//        }else
//        {
//            numberColumns = 3;
//        }
        numberColumns = getResources().getInteger(R.integer.numberColumns);

    }

    private void configRecyclerView()
    {
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),numberColumns));
        adapter = new PersonPagedAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        adapter.setOnPersonPagedAdapterListener((position,imagen) ->
        {
            if (adapter.getCurrentList() != null)
            viewModel.setPersonLD(adapter.getCurrentList().get(position));

            if (getActivity()!=null)
                getActivity().getSupportFragmentManager().beginTransaction()
                        .hide(this)
                        .addSharedElement(imagen,imagen.getTransitionName())
                        .setReorderingAllowed(true)
                        .add(R.id.mainActivity_contenedor,DetailFragment.newInstance(imagen.getTransitionName()),"Detalle")
                        .addToBackStack("Details")
                        .commit();

        });


    }



    private void configObserver()
    {

        Observer<PagedList<Person>> personPagedObserver = (persons ->
        {
                    Log.d("webservice", "dentro del observer ..... no hay errores y pongo la lista en el recycler");
                    adapter.setPersonList(persons);
                //    adapter.submitList(persons);
        });
        viewModel.getPagedListPersonLD().observe(this,personPagedObserver);




        Observer<String>networkObserver = networkState ->
        {

            if (networkState.equals(Constantes.NETWORK_STATE_ERROR))
            {
                DialogoSimple dialogoSimple = DialogoSimple.newInstance(getString(R.string.dialogo_simple_tittle)
                        ,getString(R.string.dialogo_simple_message));
                if (getActivity() != null)
                    dialogoSimple.show(getActivity().getSupportFragmentManager(),"Dialogo error");

                dialogoSimple.setOnDiagoloSimpleListener(() -> viewModel.chargePersonList());

            }else if (networkState.equals(Constantes.NETWORK_STATE_LOADING))
            {
                progressBar.setVisibility(View.VISIBLE);
            }else if (networkState.equals(Constantes.NETWORK_STATE_LOADED))
            {
                progressBar.setVisibility(View.INVISIBLE);

            }
        };
        viewModel.getNetworkStateLD().observe(this,networkObserver);
    }



}
