package com.rxmediaapp.serviceparsing;

import java.io.IOException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

/**
 * Created by TEZ on 3/2/2019.
 */

public class CustomHostnameVerifier implements org.apache.http.conn.ssl.X509HostnameVerifier {

    @Override
    public boolean verify(String host, SSLSession session) {
        HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
        return hv.verify(host, session);
    }

    @Override
    public void verify(String host, SSLSocket ssl) throws IOException {
    }

    @Override
    public void verify(String host, X509Certificate cert) throws SSLException {

    }

    @Override
    public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {

    }


}