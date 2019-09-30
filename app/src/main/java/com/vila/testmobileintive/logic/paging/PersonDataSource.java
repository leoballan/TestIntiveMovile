package com.vila.testmobileintive.logic.paging;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.vila.testmobileintive.injection.MyApplication;
import com.vila.testmobileintive.logic.Constantes;
import com.vila.testmobileintive.logic.RetrofitApi;
import com.vila.testmobileintive.model.Person;
import com.vila.testmobileintive.model.Result;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vila.testmobileintive.logic.Constantes.NETWORK_STATE_LOADING;

public class PersonDataSource extends PageKeyedDataSource<Integer, Person>
{
  //  private Context contexto;
    private final static int FIRST_PAGE = 1;
    private final static int NUM_OF_PERSONS = 50;
    private MutableLiveData <String>networkStateLD;
    private boolean successLoadInitial;
    private LoadInitialParams<Integer> initialParams;
    private LoadInitialCallback<Integer,Person> initialCallback;
    private LoadParams<Integer> params;
    private LoadCallback<Integer,Person> callback;

    @Inject
    RetrofitApi retrofitApi;

    PersonDataSource(Context contexto)
    {
   //     this.contexto = context;
        ((MyApplication)contexto.getApplicationContext()).getRetrofiComponent().inject(this);
        networkStateLD = new MutableLiveData<>();
    }

    @Override
    public void loadInitial
            (@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Person> callback)
    {
        networkStateLD.postValue(NETWORK_STATE_LOADING);
        this.initialParams = params;
        this.initialCallback = callback;
        retrofitApi.getPersonByPage("name,email,picture,login","foobar",FIRST_PAGE,NUM_OF_PERSONS)
                .enqueue(new Callback<Result>()
                {
                    @Override
                    public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response)
                    {
                        if (response.isSuccessful() && response.body() != null)
                        {
                            successLoadInitial = true;
                            Log.d("webservice","Load initial on response exito");
                            networkStateLD.postValue(Constantes.NETWORK_STATE_LOADED);
                            callback.onResult(response.body().getPersonList()
                                    ,null,FIRST_PAGE +1);
                        }else
                        {
                            Log.d("webservice","Load initial Error with retrofit request ... "+response.errorBody());
                            networkStateLD.postValue(Constantes.NETWORK_STATE_ERROR);
                            successLoadInitial = false;
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<Result> call,@NonNull Throwable t)
                    {
                        Log.d("webservice","Load initial on failure Error with retrofit request ... "+t.getMessage());
                        networkStateLD.postValue(Constantes.NETWORK_STATE_ERROR);
                        successLoadInitial = false;
                    }
                });
    }



    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Person> callback)
    {
        networkStateLD.postValue(NETWORK_STATE_LOADING);

        retrofitApi.getPersonByPage("name,email,picture,login","foobar",params.key,NUM_OF_PERSONS)
                .enqueue(new Callback<Result>()
                {
                    @Override
                    public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response)
                    {
                        Integer adjacentKey = (params.key>1)? params.key-1 : null;
                        if (response.isSuccessful() && response.body() != null)
                        {
                            Log.d("webservice","Load before on response exito");
                            networkStateLD.postValue(NETWORK_STATE_LOADING);

                            callback.onResult(response.body().getPersonList(),adjacentKey);
                        }else
                        {
                            Log.d("webservice","Load before on response Error with retrofit request ... "+response.errorBody());
                            networkStateLD.postValue(Constantes.NETWORK_STATE_ERROR);

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Result> call,@NonNull Throwable t)
                    {
                        Log.d("webservice","Load before on failure Error with retrofit request ... "+t.getMessage());
                        networkStateLD.postValue(Constantes.NETWORK_STATE_ERROR);

                    }
                });
    }



    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Person> callback)
    {
        networkStateLD.postValue(NETWORK_STATE_LOADING);
        this.params = params;
        this.callback = callback;
        retrofitApi.getPersonByPage("name,email,picture,login","foobar",params.key,NUM_OF_PERSONS)
                .enqueue(new Callback<Result>()
                {
                    @Override
                    public void onResponse(@NonNull Call<Result> call,@NonNull Response<Result> response)
                    {
                        if (response.isSuccessful() && response.body() != null)
                        {
                            Log.d("webservice","Load after on response exito");
                            networkStateLD.postValue(Constantes.NETWORK_STATE_LOADED);

                            callback.onResult(response.body().getPersonList(),params.key+1);
                        }else
                        {
                            Log.d("webservice","Load after on response Error with retrofit request ... "+response.errorBody());
                            networkStateLD.postValue(Constantes.NETWORK_STATE_ERROR);

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Result> call,@NonNull Throwable t)
                    {
                        Log.d("webservice","Load after on failure Error with retrofit request ... "+t.getMessage());
                        networkStateLD.postValue(Constantes.NETWORK_STATE_ERROR);
                    }
                });
    }



    public MutableLiveData<String> getNetworkStateLD()
    {
        if (networkStateLD == null)
        {
            networkStateLD = new MutableLiveData<>();
        }
        return networkStateLD;
    }

    public void retrycharge()
    {
        networkStateLD.postValue("");
        if (!successLoadInitial)
        {
            loadInitial(initialParams,initialCallback);
        }else
        {
            loadAfter(params,callback);
        }

    }
}
