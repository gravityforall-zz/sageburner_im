package com.sageburner.im.android.service;

import android.util.Log;
import com.sageburner.im.android.core.Constants;
import com.sageburner.im.android.core.ConversationMessageItem;
import com.sageburner.im.android.core.User;
import com.sageburner.im.android.ibe.IBE;
import com.sageburner.im.android.ibe.IBEParams;
import com.sageburner.im.android.ibe.IBEParamsWrapper;
import com.sageburner.im.android.util.CryptoUtils;
import com.sageburner.im.android.util.Ln;
import com.sageburner.im.android.util.SafeAsyncTask;
import com.sageburner.im.android.util.UserUtils;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import retrofit.http.GET;
import retrofit.http.Query;

import javax.inject.Inject;
import java.util.Collection;


/**
 * Class used for accessing the XMPP service.
 */
public class IBEService {

    @Inject
    protected BootstrapService bootstrapService;

    public IBEService() { }

    public void getIBEParamsWrapper(final Runnable onSuccess) {
        new GetIBEParamsWrapperTask(onSuccess).execute();
    }

    private class GetIBEParamsWrapperTask extends SafeAsyncTask<Boolean> {

        private final Runnable onSuccess;

        private IBEParamsWrapper ibeParamsWrapper;

        protected GetIBEParamsWrapperTask(final Runnable onSuccess) {
            this.onSuccess = onSuccess;
        }

        @Override
        public Boolean call() throws Exception {
            //get IBEParams
            ibeParamsWrapper = bootstrapService.getIBEParamsWrapper(1098054684);
            return true;
        }

        @Override
        protected void onSuccess(final Boolean messageSendSuccess) throws Exception {
            //TODO: implement onSuccess method
            super.onSuccess(messageSendSuccess);

            IBEParams ibeParams = ibeParamsWrapper.getIbeParams();
            createAndInitIBE(ibeParams.getParamsString(), ibeParams.getpByteString(), ibeParams.getsByteString());

            Ln.d("Message send successful: %s", messageSendSuccess);
            onSuccess.run();
        }

        @Override
        protected void onException(final Exception e) throws RuntimeException {
            //TODO: implement onException method
            super.onException(e);

            Ln.e(e.getCause(), "Message send failed.");
        }
    }

    private static void createAndInitIBE(String paramsString, String pByteString, String sByteString ) {
        //IBE Encryption Stuff
        Log.d("BootstrapApplication::onCreate: ", "Initializing IBE...");
        CryptoUtils.setIbe(new IBE(paramsString, pByteString, sByteString));
    }
}