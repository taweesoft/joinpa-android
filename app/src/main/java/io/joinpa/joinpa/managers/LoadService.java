package io.joinpa.joinpa.managers;

import android.util.Log;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import io.joinpa.joinpa.models.Token;
import io.joinpa.joinpa.models.User;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by TAWEESOFT on 5/13/16 AD.
 */
public class LoadService {

    private LoadService () {}

    public static LoadService newInstance() {
        return new LoadService();
    }

    public RequestBody getRequestBody(Map<String,String> data) {
        return HttpManager.getInstance().createRequestBody(data);
    }

    public APIService getAPIService() {
        return HttpManager.getInstance().getAPIService(APIService.class);
    }

    public void signIn(Map<String, String> data , Observer observer) {
        RequestBody requestBody = getRequestBody(data);
        APIService apiService = getAPIService();
        Call<Token> call = apiService.signin(requestBody);
        ServerCallBack<Token> callback = new ServerCallBack<Token>();
        callback.addObserver(observer);
        call.enqueue(callback);
    }

    public void signUp(Map<String,String> data , Observer observer) {
        RequestBody requestBody = getRequestBody(data);
        APIService apiService = getAPIService();
        Call<Token> call = apiService.signup(requestBody);
        ServerCallBack<Token> callBack = new ServerCallBack<Token>();
        callBack.addObserver(observer);
        call.enqueue(callBack);
    }

    public void verify(Map<String,String> data , Observer observer) {
        RequestBody requestBody = getRequestBody(data);
        APIService apiService = getAPIService();
        Call<User> call = apiService.verify(data.get("token") , requestBody);
        ServerCallBack<User> callBack = new ServerCallBack<User>();
        callBack.addObserver(observer);
        call.enqueue(callBack);
    }

    class ServerCallBack<T> extends Observable implements Callback<T> {

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            setChanged();
            notifyObservers(response);
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            Log.e("connection error" , t.getMessage());
            // TODO: 5/13/16 AD handle error
        }
    }


}
