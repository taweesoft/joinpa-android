package io.joinpa.joinpa.managers.commands;

import java.util.HashMap;
import java.util.Map;

import io.joinpa.joinpa.managers.LoadService;

/**
 * Created by TAWEESOFT on 5/21/16 AD.
 */
public class AcceptFriendRequestResponse extends ObjectResponse {

    private String otherUserId;

    public AcceptFriendRequestResponse(String otherUserId) {
        this.otherUserId = otherUserId;
    }

    @Override
    public void execute() {
        LoadService loadService = LoadService.newInstance();
        Map<String,String> data = new HashMap<>();
        data.put("otherUserId", otherUserId);
        loadService.acceptFriendRequest(data, this);
    }
}
