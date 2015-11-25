package com.lealpoints.service.implementations;

import org.easymock.EasyMock;
import org.junit.Test;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static org.easymock.EasyMock.*;

public class SMSServiceImplTest {

    @Test
    public void testSendSMSMessage() throws IOException, MessagingException {

        final HttpURLConnection httpURLConnection = creteHttpURLConnection();

        SMSServiceImpl smsService = new SMSServiceImpl(null) {

            @Override
            URL getUrl(String phone, String message) throws MalformedURLException {
                return new URL("http://www.google.com");
            }

            @Override
            InputStreamReader getInputStreamReader(HttpURLConnection connection) throws IOException {
                return super.getInputStreamReader(connection);
            }

            @Override
            HttpURLConnection getHttpURLConnection(URL url) throws IOException {
                return httpURLConnection;
            }

            @Override
            public String getResponse(HttpURLConnection connection) throws IOException {
                return "OK";
            }
        };

        smsService.sendSMSMessage("6623471507", "message");
        verify(httpURLConnection);
    }

    private HttpURLConnection creteHttpURLConnection() throws ProtocolException {
        final HttpURLConnection httpURLConnection = EasyMock.createStrictMock(HttpURLConnection.class);
        httpURLConnection.setRequestMethod(anyString());
        expectLastCall();
        httpURLConnection.disconnect();
        expectLastCall();
        replay(httpURLConnection);
        return httpURLConnection;
    }
}