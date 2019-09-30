package com.vila.testmobileintive.injection;

import com.vila.testmobileintive.logic.paging.PersonDataSource;
import com.vila.testmobileintive.viewmodel.MainViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = RetrofitModule.class)
public interface RetrofitComponent
{
    void inject(PersonDataSource personDataSource);
    void inject(MainViewModel mainViewModel);
}
