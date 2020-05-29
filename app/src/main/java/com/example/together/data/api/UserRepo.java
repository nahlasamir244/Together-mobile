package com.example.together.data.api;

import androidx.lifecycle.MutableLiveData;

import com.example.together.data.model.GeneralResponse;
import com.example.together.data.model.Group;
import com.example.together.data.model.JoinGroupResponse;
import com.example.together.data.model.LoginResponse;
import com.example.together.data.model.User;
import com.example.together.data.model.UserLogin;

import java.util.List;

public class UserRepo {

    private ApiProvider provider;

    public UserRepo() {
        provider = new ApiProvider();
    }


    public MutableLiveData<String> signUp(User user) {
        return provider.signUp(user);
    }

    public MutableLiveData<LoginResponse> login(UserLogin userLogin) {
        return provider.login(userLogin);
    }

    public MutableLiveData<User> fetchUserData(int id) {
        return provider.fetchUserData(id);
    }

    public MutableLiveData<GeneralResponse> createGroup(Group group) {
        return provider.createGroup(group);
    }

    public MutableLiveData<GeneralResponse> joinGroup(int gpId, int userID) {
        return provider.requestJoinGroup(gpId, userID);
    }

    public MutableLiveData<List<JoinGroupResponse>> getAllResponsesForGroup(int gpId) {
        return provider.getAllResponsesForGroup(gpId);
    }

    public MutableLiveData<GeneralResponse> addGroupMember(int gpID, int userID, int adminID) {
        return provider.addGroupMember(gpID, userID, adminID);
    }
}