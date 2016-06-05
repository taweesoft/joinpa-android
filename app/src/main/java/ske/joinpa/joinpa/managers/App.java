package ske.joinpa.joinpa.managers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import ske.joinpa.joinpa.models.Friend;
import ske.joinpa.joinpa.models.InternalData;
import ske.joinpa.joinpa.models.Token;
import ske.joinpa.joinpa.models.User;

/**
 * Created by TAWEESOFT on 5/14/16 AD.
 */
public class App {

    private static App app;
    private InternalData internalData;
    private EventManager eventManager;
    private PlaceManager placeManager;

    private App() {
        internalData = InternalData.getInstance();
        eventManager = new EventManager(internalData.events);
        placeManager = new PlaceManager(internalData.places);
    }

    public static App getInstance() {
        if (app == null ) app = new App();
        return app;
    }

    public void saveToken(Token token , Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.SP_KEY , context.MODE_PRIVATE).edit();
        editor.putString("token", token.getKey());
        editor.apply();
        loadToken(context);
    }

    private void clearToken(Context context) {
        getInternalData().token = null;
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.SP_KEY , context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    public InternalData getInternalData() {
        return internalData;
    }

    public void loadToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_KEY, context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        internalData.token = new Token(token);
    }

    public Token getToken() {
        if(getInternalData().token == null) return new Token("temp");
        return getInternalData().token;
    }

    public void setUser(User user) {
        getInternalData().user = user;
    }

    public User getUser() {
        return getInternalData().user;
    }

    public void clear(Context context) {
        clearToken(context);
        setUser(null);
    }

    public void refreshFriendList(List<Friend> friends) {
        getUser().getFriendList().clear();
        getUser().getFriendList().addAll(friends);
    }

    public boolean isAuthenticated() {
        return getInternalData().token.getKey() != null;
    }


    public EventManager getEventManager() {
        return eventManager;
    }

    public PlaceManager getPlaceManager() {
        return placeManager;
    }
}