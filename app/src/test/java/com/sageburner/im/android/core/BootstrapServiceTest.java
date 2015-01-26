

package com.sageburner.im.android.core;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;

import com.github.kevinsawicki.http.HttpRequest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.sageburner.im.android.ibe.IBEParamsWrapper;
import com.sageburner.im.android.service.BootstrapService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests of {@link com.sageburner.im.android.service.BootstrapService}
 */
@RunWith(MockitoJUnitRunner.class)
public class BootstrapServiceTest {

    /**
     * Create reader for string
     *
     * @param value
     * @return input stream reader
     * @throws IOException
     */
    private static BufferedReader createReader(String value) throws IOException {
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(
                value.getBytes(HttpRequest.CHARSET_UTF8))));
    }

    @Mock
    private HttpRequest request;

    private BootstrapService service;

    /**
     * Set up default mocks
     *
     * @throws IOException
     */
    @Before
    public void before() throws IOException {
        //service = new BootstrapService("foo", new UserAgentProvider()) {
        service = new BootstrapService() {
            protected HttpRequest execute(HttpRequest request) throws IOException {
                return BootstrapServiceTest.this.request;
            }
        };
        doReturn(true).when(request).ok();
    }

    /**
     * Verify getting users with an empty response
     *
     * @throws IOException
     */
    @Test
    public void authenticateValidUser() throws IOException {
        doReturn(createReader("")).when(request).bufferedReader();
        User user = service.authenticate("user1@sageburner.com", "password");
        assertNotNull(user);
        assertTrue(user.getLastName().equalsIgnoreCase("1"));
    }

    /**
     * Verify getting users with an empty response
     *
     * @throws IOException
     */
    @Test
    public void authenticateInvalidUser() throws IOException {
        doReturn(createReader("")).when(request).bufferedReader();
        User user = service.authenticate("jake@sageburner.com", "password");
        assertNull(user);
    }

    /**
     * Verify getting news with an empty response
     *
     * @throws IOException
     */
    @Test
    public void getNewIBEParams() throws IOException {
        doReturn(createReader("")).when(request).bufferedReader();
        IBEParamsWrapper ibeParamsWrapper = service.getIBEParamsWrapper(0);
        assertNotNull(ibeParamsWrapper);
        assertNotNull(ibeParamsWrapper.getIbeParams());
        assertNotNull(ibeParamsWrapper.getKey());

        assertNotNull(ibeParamsWrapper.getIbeParams().getParamsString());
        assertNotNull(ibeParamsWrapper.getIbeParams().getpByteString());
        assertNotNull(ibeParamsWrapper.getIbeParams().getsByteString());
    }

    /**
     * Verify getting news with an empty response
     *
     * @throws IOException
     */
    @Test
    public void getExistingIBEParams() throws IOException {
        doReturn(createReader("")).when(request).bufferedReader();
        IBEParamsWrapper ibeParamsWrapper = service.getIBEParamsWrapper(266068321);
        assertNotNull(ibeParamsWrapper);
        assertNotNull(ibeParamsWrapper.getIbeParams());
        assertNotNull(ibeParamsWrapper.getKey());

        assertNotNull(ibeParamsWrapper.getIbeParams().getParamsString());
        assertNotNull(ibeParamsWrapper.getIbeParams().getpByteString());
        assertNotNull(ibeParamsWrapper.getIbeParams().getsByteString());
    }

    /**
     * Verify getting news with an empty response
     *
     * @throws IOException
     */
    @Test
    public void getInvalidIBEParams() throws IOException {
        doReturn(createReader("")).when(request).bufferedReader();
        IBEParamsWrapper ibeParamsWrapper = service.getIBEParamsWrapper(12345);
        assertNull(ibeParamsWrapper);
    }
}
