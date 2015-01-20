package com.sageburner.im.android.service;

import com.sageburner.im.android.core.Constants;
import com.sageburner.im.android.core.User;
import com.sageburner.im.android.core.UsersWrapper;
import com.sageburner.im.android.ibe.IBEParamsWrapper;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * User service for connecting the the REST API and
 * getting the users.
 */
public interface IBEParamsService {

    /**
     * The {@link retrofit.http.Query} values will be transform into query string paramters
     * via Retrofit
     *
     * @param key The IBEParams key
     * @return An IBEParamsWrapper object.
     */
    @GET(Constants.Http.URL_IBEPARAMS_FRAG)
    IBEParamsWrapper getIBEParamsWrapper(@Query(Constants.Http.PARAM_KEY) int key);
}
