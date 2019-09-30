package com.vila.testmobileintive.logic.paging;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.vila.testmobileintive.model.Person;

public class PersonDataSourceFactory extends DataSource.Factory<Integer, Person>
{
    private PersonDataSource personDataSource;
    private MutableLiveData<PersonDataSource> pageKeyedDataSourceMutableLiveData =
            new MutableLiveData<>();
    private Context contexto;


    public PersonDataSourceFactory(Context contexto)
    {
        this.contexto = contexto;
    }

    @NonNull
    @Override
    public DataSource<Integer,Person> create()
    {
        personDataSource = new PersonDataSource(contexto);
        pageKeyedDataSourceMutableLiveData.postValue(personDataSource);
        return personDataSource;
    }

    public MutableLiveData<PersonDataSource> getPageKeyedDataSourceMutableLiveData()
    {
        return pageKeyedDataSourceMutableLiveData;
    }

    public PersonDataSource getPersonDataSource()
    {
        return  personDataSource;
    }

}
