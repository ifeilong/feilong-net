package com.feilong.net.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class TrustAnyTrustManager implements X509TrustManager{

    /** Static instance. */
    // the static instance works for all types
    public static final X509TrustManager INSTANCE = new TrustAnyTrustManager();

    //---------------------------------------------------------------

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificate,String authType) throws CertificateException{
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificate,String authType) throws CertificateException{
    }

    @Override
    public X509Certificate[] getAcceptedIssuers(){
        return new X509Certificate[] {};
    }

}
