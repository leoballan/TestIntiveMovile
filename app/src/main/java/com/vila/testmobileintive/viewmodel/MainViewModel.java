package com.vila.testmobileintive.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.vila.testmobileintive.injection.MyApplication;
import com.vila.testmobileintive.logic.paging.PersonDataSource;
import com.vila.testmobileintive.logic.paging.PersonDataSourceFactory;
import com.vila.testmobileintive.model.Person;

import javax.inject.Inject;


public class MainViewModel extends AndroidViewModel
{


    private MutableLiveData<Person> personLD;
    private LiveData<PagedList<Person>> pagedListPersonLD;
    private LiveData <PersonDataSource> pageKeyedDataSourceLiveDataLD;
    private LiveData <String>netWorkStateLD;

    //private PersonDataSourceFactory personDataSourceFactory;

    @Inject
    PersonDataSourceFactory personDataSourceFactory;


    public MainViewModel(@NonNull Application application)
    {
        super(application);
        ((MyApplication)application).getRetrofiComponent().inject(this);
    }

    public LiveData<String>getNetworkStateLD()
    {
        return netWorkStateLD;
    }


    public LiveData<Person>getPersonLD()
    {
        if (personLD == null)
        {
            personLD = new MutableLiveData<>();
        }
        return personLD;
    }


    public void setPersonLD(Person person)
    {
            personLD.setValue(person);
    }



    public LiveData<PagedList<Person>> getPagedListPersonLD()
    {
        if (pagedListPersonLD == null)
        {
            personDataSourceFactory = new PersonDataSourceFactory(getApplication());
            pageKeyedDataSourceLiveDataLD = personDataSourceFactory.getPageKeyedDataSourceMutableLiveData();

            netWorkStateLD = Transformations.switchMap(personDataSourceFactory.getPageKeyedDataSourceMutableLiveData(),
                    PersonDataSource::getNetworkStateLD);

            PagedList.Config config = new PagedList.Config
                    .Builder()
                    .setEnablePlaceholders(false)
                    .setPageSize(100)
                    .build();

            pagedListPersonLD =  new LivePagedListBuilder<>(personDataSourceFactory,config).build();

        }
        return pagedListPersonLD;
    }



    public void chargePersonList()
    {
        personDataSourceFactory.getPersonDataSource().retrycharge();

    }
}
