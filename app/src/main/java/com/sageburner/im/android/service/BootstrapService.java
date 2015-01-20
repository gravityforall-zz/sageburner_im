
package com.sageburner.im.android.service;

import java.util.List;

import com.sageburner.im.android.core.User;
import com.sageburner.im.android.ibe.IBEParamsWrapper;
import com.sageburner.im.android.service.UserService;
import retrofit.RestAdapter;

/**
 * Bootstrap API service
 */
public class BootstrapService {

    private RestAdapter restAdapter;

    /**
     * Create bootstrap service
     * Default CTOR
     */
    public BootstrapService() {
    }

    /**
     * Create bootstrap service
     *
     * @param restAdapter The RestAdapter that allows HTTP Communication.
     */
    public BootstrapService(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

    private UserService getUserService() {
        return getRestAdapter().create(UserService.class);
    }

    private IBEParamsService getIBEParamsService() {
        return getRestAdapter().create(IBEParamsService.class);
    }

//    private NewsService getNewsService() {
//        return getRestAdapter().create(NewsService.class);
//    }

//    private CheckInService getCheckInService() {
//        return getRestAdapter().create(CheckInService.class);
//    }

    private RestAdapter getRestAdapter() {
        return restAdapter;
    }

    /**
     * Get all bootstrap News that exists on Parse.com
     */
//    public List<News> getNews() {
//        return getNewsService().getNews().getResults();
//    }

    /**
     * Get all bootstrap Users that exist on Parse.com
     */
//    public List<User> getUsers() {
//        return getUserService().getUsers().getResults();
//    }

    /**
     * Get all bootstrap Checkins that exists on Parse.com
     */
//    public List<CheckIn> getCheckIns() {
//       return getCheckInService().getCheckIns().getResults();
//    }

    public User authenticate(String email, String password) {
        return getUserService().authenticate(email, password);
    }

    public IBEParamsWrapper getIBEParamsWrapper(int key) {
        return getIBEParamsService().getIBEParamsWrapper(key);
    }
}