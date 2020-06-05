package com.example.together.data.api.user_apis;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.together.data.api.APIInterface;
import com.example.together.data.model.GeneralResponse;
import com.example.together.data.model.GroupDetails;
import com.example.together.data.model.Interests;
import com.example.together.data.model.LoginResponse;
import com.example.together.data.model.User;
import com.example.together.data.model.UserGroup;
import com.example.together.data.model.UserInterests;
import com.example.together.data.model.UserLogin;
import com.example.together.utils.HelperClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.together.data.Urls.API_URL;
import static com.example.together.utils.HelperClass.BEARER_HEADER;
import static com.example.together.utils.HelperClass.TAG;

public class UserAPIProvider {

    private LoginResponse loginResponse;
    private UserAPIInterface userInterface;

    public UserAPIProvider() {
        Log.i(TAG, "UserUserAPIProvider -- cons() ");
        // logs request and response information.
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        userInterface = retrofit.create(UserAPIInterface.class);
    }

    /**
     * send userdata to database through calling api sing up
     *
     * @param user user body that will send to db
     * @return response contain whether sign up failed or success
     */
    MutableLiveData<LoginResponse> signUp(User user) {

        MutableLiveData<LoginResponse> resSignUp = new MutableLiveData<>();

        Call<LoginResponse> signUpCall = userInterface.signUp(user);

        signUpCall.enqueue(new Callback<LoginResponse>() {


            @Override
            public void onResponse(Call<LoginResponse> call,
                                   Response<LoginResponse> res) {
                Log.i(TAG, "UserAPIProvider -- signUp() enqueue()  body >> " +
                        res.body());

                loginResponse = res.body();
                // coz if response is mean will object will carry token and id only
                loginResponse.setSuccess(res.body().getResponse() == null);
                resSignUp.setValue(loginResponse);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.i(TAG, "UserAPIProvider -- onFailure: " + t.getMessage());
                // here server is down
                loginResponse = new LoginResponse();
                loginResponse.setConFailed(true);
                resSignUp.setValue(loginResponse);
                t.printStackTrace();
                call.cancel();
            }
        });
        return resSignUp;
    }

    /**
     * take user data Login and call login api if user exist response will be null
     * and id and token will be filled with data
     * otherwise response will contain response error whether the email or password not valid
     * then store result of call to liveData of {@link LoginResponse}
     * to activity can observe on it
     *
     * @param userLogin will carry email and password for user
     */
    MutableLiveData<LoginResponse> login(UserLogin userLogin) {
        Log.i(TAG, "UserAPIProvider -- login() ");
        MutableLiveData<LoginResponse> resLoginLiveData = new MutableLiveData<>();

        Call<LoginResponse> loginCall = userInterface.login(userLogin);

        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> res) {
                Log.i(TAG, "UserAPIProvider -- login() enqueue() onResponse() resBody >> "
                        + res.body());

                loginResponse = res.body();
                loginResponse.setSuccess(res.body().getResponse() == null);
                resLoginLiveData.setValue(loginResponse);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // here server is down
                loginResponse = new LoginResponse();
                loginResponse.setConFailed(true);
                resLoginLiveData.setValue(loginResponse);
                t.printStackTrace();
                call.cancel();
            }
        });

        return resLoginLiveData;

    }

    /**
     * get user data
     *
     * @param id of the user you want to retrieve data for it
     * @return {@link User} object that carry data
     */
    MutableLiveData<User> fetchUserData(int id, String token) {
        MutableLiveData<User> userData = new MutableLiveData<>();

        Call<User> userCall = userInterface.fetchUserData(id,
                BEARER_HEADER + token);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.i(TAG, "UserAPIProvider -- onResponse: fetchUserData() body >> "
                        + response.body());

                userData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                userData.setValue(null);
                t.printStackTrace();
                Log.d(TAG, "onFailure: errMsg " + t.getMessage());
                call.cancel();

            }
        });

        return userData;

    }

    //TODO: MOVED
    MutableLiveData<GeneralResponse> updateUserProfile (int userId, String header,User user){

        MutableLiveData<GeneralResponse> updateResponse = new MutableLiveData<>();
        Call<GeneralResponse> updateProfileCall=userInterface.updateUserProfile(userId, HelperClass.BEARER_HEADER+header,user);
        updateProfileCall.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                updateResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
//                GeneralResponse generalRes = new GeneralResponse();
//                generalRes.response = t.getMessage();
                updateResponse.setValue(null);
                t.printStackTrace();
                call.cancel();

            }
        });
        return updateResponse;


    }




    MutableLiveData<GeneralResponse> updateUserInterests (int userId,String header, UserInterests interests){

        MutableLiveData<GeneralResponse> updateInterestsResponse = new MutableLiveData<>();
        Call<GeneralResponse> updateInterestCall=userInterface.updateUserInterests(userId,header,interests);
        updateInterestCall.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                Log.i(TAG, "ApiProvider -- updateInterests() enqueue()  body >> " +
                        response.body());
                updateInterestsResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
//                GeneralResponse generalRes = new GeneralResponse();
//                generalRes.response = t.getMessage();
//                updateInterestsResponse.setValue(generalRes);
                updateInterestsResponse.setValue(null);
                t.printStackTrace();
                call.cancel();

            }
        });
        return updateInterestsResponse;

    }


    MutableLiveData<ArrayList<UserGroup>> getAllUserGroups(int userId, String header){
        MutableLiveData<ArrayList<UserGroup>> groupsRes=new MutableLiveData<>();

        Call<ArrayList<UserGroup>> getAllUserGroupsCall=userInterface.getAllUserGroups(userId,HelperClass.BEARER_HEADER+header);

        getAllUserGroupsCall.enqueue(new Callback<ArrayList<UserGroup>>() {
            @Override
            public void onResponse(Call<ArrayList<UserGroup>> call, Response<ArrayList<UserGroup>> response) {
                groupsRes.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<UserGroup>> call, Throwable t) {
                groupsRes.setValue(null);

                t.printStackTrace();
                call.cancel();

            }
        });

        return  groupsRes;

    }
    MutableLiveData<ArrayList<Interests>> getAllInterests(){
        MutableLiveData<ArrayList<Interests>> allInterests=new MutableLiveData<>();
        Call<ArrayList<Interests>> getAllInterestsCall=userInterface.getAllInterests();
        getAllInterestsCall.enqueue(new Callback<ArrayList<Interests>>() {
            @Override
            public void onResponse(Call<ArrayList<Interests>> call, Response<ArrayList<Interests>> response) {
                allInterests.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Interests>> call, Throwable t) {
                allInterests.setValue(null);
                t.printStackTrace();
                call.cancel();

            }
        });



        return allInterests;

    }

    MutableLiveData<GroupDetails> getSpecificGroupDetails (int groupId, String header){
        MutableLiveData<GroupDetails> groupMutableLiveData=new MutableLiveData<>();
        Call<GroupDetails> groupCall=userInterface.getSpecificGroupDetails(groupId,HelperClass.BEARER_HEADER+header);
        groupCall.enqueue(new Callback<GroupDetails>() {
            @Override
            public void onResponse(Call<GroupDetails> call, Response<GroupDetails> response) {
                groupMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GroupDetails> call, Throwable t) {
                groupMutableLiveData.setValue(null);
                t.printStackTrace();
                call.cancel();
            }
        });


        return groupMutableLiveData;
    }

    MutableLiveData<GeneralResponse> removeMemberFromGroup(int groupId,int id,int adminId,String header){
        MutableLiveData<GeneralResponse> removeMemberResponse=new MutableLiveData<>();

        Call<GeneralResponse> removeMemberCall=userInterface.removeMemberFromGroup(groupId,id,adminId,HelperClass.BEARER_HEADER+header);
        removeMemberCall.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                removeMemberResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
//                GeneralResponse generalRes = new GeneralResponse();
//                generalRes.response = t.getMessage();
                removeMemberResponse.setValue(null);
                t.printStackTrace();
                call.cancel();

            }
        });
        return  removeMemberResponse;



    }



    //LEAVE GROUP

    MutableLiveData<GeneralResponse> leaveGroup(int groupId,int id,String token){
        MutableLiveData<GeneralResponse> leaveRes=new MutableLiveData<>();
        Call<GeneralResponse> leaveGroupCall=userInterface.leaveGroup(groupId, id, HelperClass.BEARER_HEADER+token);
        leaveGroupCall.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                leaveRes.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                leaveRes.setValue(null);

            }
        });

        return leaveRes;



    }

    ///Logout
    MutableLiveData<GeneralResponse> logout(int id){
        MutableLiveData<GeneralResponse> logoutRes=new MutableLiveData<>();

        Call<GeneralResponse> logoutCall=userInterface.logout(id);
        logoutCall.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                logoutRes.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                logoutRes.setValue(null);

            }
        });

        return  logoutRes;

    }


}