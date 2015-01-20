

package com.sageburner.im.android.core;

/**
 * Bootstrap constants
 */
public final class Constants {
    private Constants() {}

    public static final class Auth {
        private Auth() {}

        /**
         * Account type id
         */
        public static final String BOOTSTRAP_ACCOUNT_TYPE = "com.sageburner.im.android";

        /**
         * Account name
         */
        public static final String BOOTSTRAP_ACCOUNT_NAME = "sageburner_im";

        /**
         * Provider id
         */
        public static final String BOOTSTRAP_PROVIDER_AUTHORITY = "com.sageburner.im.android.sync";

        /**
         * Auth token type
         */
        public static final String AUTHTOKEN_TYPE = BOOTSTRAP_ACCOUNT_TYPE;
    }

    /**
     * All HTTP is done through a REST style API built for demonstration purposes on Parse.com
     * Thanks to the nice people at Parse for creating such a nice system for us to use for bootstrap!
     */
    public static final class Http {
        private Http() {}


        /**
         * Base URL for all requests
         */
        public static final String URL_BASE = "https://auth.sageburner.com/service";
//        public static final String URL_BASE = "https://api.parse.com";


        /**
         * Authentication Service
         */
        public static final String URL_AUTH_FRAG = "/login";

        /**
         * IBEParams Service
         */
        public static final String URL_IBEPARAMS_FRAG =  "/getIBEParams";

        /**
         * PARAMS for auth
         */
//        public static final String PARAM_USERNAME = "username";
        //TODO: Fix lame hibernate interface to actually query by username
        public static final String PARAM_USERNAME = "username";
        public static final String PARAM_PASSWORD = "password";

        public static final String PARAM_KEY = "key";

        public static final String PARSE_APP_ID = "zHb2bVia6kgilYRWWdmTiEJooYA17NnkBSUVsr4H";
        public static final String PARSE_REST_API_KEY = "N2kCY1T3t3Jfhf9zpJ5MCURn3b25UpACILhnf5u9";
        public static final String HEADER_PARSE_REST_API_KEY = "X-Parse-REST-API-Key";
        public static final String HEADER_PARSE_APP_ID = "X-Parse-Application-Id";
        public static final String CONTENT_TYPE_JSON = "application/json";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String SESSION_TOKEN = "sessionToken";


    }


    public static final class Extra {
        private Extra() {}

        public static final String NEWS_ITEM = "news_item";

        public static final String USER = "user";

    }

    public static final class Intent {
        private Intent() {}

        /**
         * Action prefix for all intents created
         */
        public static final String INTENT_PREFIX = "com.sageburner.im.android.";

    }

    public static class Notification {
        private Notification() {
        }

        public static final int TIMER_NOTIFICATION_ID = 1000; // Why 1000? Why not? :)
    }

    public static final class XMPP {
        private XMPP() {}

        /**
         * XMPP Host
         */
        public static final String HOST = "sageburner.com";

        /**
         * XMPP Port
         */
        public static final int PORT = 48999;

        /**
         * XMPP Username
         */
        public static final String USERNAME = "user1";

        /**
         * XMPP Password
         */
        public static final String PASSWORD = "password";
    }

    public static final class Crypto {
        private Crypto() {
        }

        /**
         * Symmetric Crypto Algorithm
         */
        public static final String CRYPTO_ALGORITHM = "AES";

        /**
         * Symmetric Crypto Algorithm Mode
         */
        public static final String CRYPTO_ALGORITHM_MODE = "AES/ECB/PKCS7Padding";

        /**
         * AES Message Seaparator
         */
        public static final String CRYPTO_MESSAGE_SEPARATOR = ";";
    }
}