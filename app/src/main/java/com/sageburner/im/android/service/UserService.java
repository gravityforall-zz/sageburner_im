package com.sageburner.im.android.service;

import java.util.List;

import com.sageburner.im.android.core.Constants;
import com.sageburner.im.android.core.User;
import com.sageburner.im.android.core.UsersWrapper;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * User service for connecting the the REST API and
 * getting the users.
 */
public interface UserService {

    /**
     * The {@link retrofit.http.Query} values will be transform into query string paramters
     * via Retrofit
     *
     * @param email The users email
     * @param password The users password
     * @return A login response.
     */
    @GET(Constants.Http.URL_AUTH_FRAG)
    User authenticate(@Query(Constants.Http.PARAM_USERNAME) String email,
                               @Query(Constants.Http.PARAM_PASSWORD) String password);
}
