package com.vila.testmobileintive.ui;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.vila.testmobileintive.R;
import com.vila.testmobileintive.model.Person;
import com.vila.testmobileintive.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;


public class DetailFragment extends Fragment
{
    private ImageView imageView;
    private TextView name, username, email;
    private MainViewModel viewModel;
    private final static String TRANSITION_NAME = "transitionName";
    private int cont = 0;
    private List<TextView> textViews = new ArrayList<>();

    public DetailFragment(){ }



     static DetailFragment newInstance(String transitionNAme)
    {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(TRANSITION_NAME,transitionNAme);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Transition transition =
                TransitionInflater.from(getContext())
                        .inflateTransition(R.transition.my_move);
        setSharedElementEnterTransition(transition);
    //    setSharedElementReturnTransition(transition);
        setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.grid_exit_transition));
        setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.grid_exit_transition));



            setEnterSharedElementCallback(new SharedElementCallback()
            {
                @Override
                public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots)
                {
                    super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);

                    animateView(textViews.get(cont));


                }
            });



    }


    private void animateView(View view)
    {
        ObjectAnimator translateAnimator = ObjectAnimator.ofFloat
                (view,"translationX",350f,0f).setDuration(600);
        translateAnimator.setInterpolator(new DecelerateInterpolator());
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view,"alpha",0,1).setDuration(600);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translateAnimator,alphaAnimator);
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                if (cont<2)
                {
                    cont++;
                    animateView(textViews.get(cont));

                }

            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        imageView = view.findViewById(R.id.detailFragment_imageview);
        name = view.findViewById(R.id.detailFragment_name);
        username = view.findViewById(R.id.detailFragment_user);
        email = view.findViewById(R.id.detailFragment_email);
        textViews.add(name);
        textViews.add(username);
        textViews.add(email);
        if (savedInstanceState == null)
        {
            name.setAlpha(0);
            username.setAlpha(0);
            email.setAlpha(0);
        }

        if (getArguments() != null)
        {
            imageView.setTransitionName(getArguments().getString(TRANSITION_NAME));
        }
        postponeEnterTransition();

        return view;

    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        init();
    }



    private void init()
    {
        if (getActivity()!=null)
        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);


        if (viewModel.getPersonLD().getValue() != null)
        fillData(viewModel.getPersonLD().getValue());

    }



    private void fillData(Person p)
    {
        postponeEnterTransition();

        Glide.with(getActivity())
                .load(p.getPicture().getLarge())
                .crossFade()
                .centerCrop()
                .dontAnimate()
                .listener(new RequestListener<String, GlideDrawable>()
                {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource)
                    {

                        startPostponedEnterTransition();
                        //getParentFragment().startPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource)
                    {
                        startPostponedEnterTransition();
                        return false;
                    }
                })
                .into(imageView);

        String completeName = p.getName().getTitle() + " " + p.getName().getFirst() +
                " " + p.getName().getLast();

        name.setText(completeName);
        email.setText(p.getEmail());
        username.setText(p.getLogin().getUsername());

    }


}
