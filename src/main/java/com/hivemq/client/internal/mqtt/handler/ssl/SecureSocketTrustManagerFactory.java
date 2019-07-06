package com.hivemq.client.internal.mqtt.handler.ssl;

import java.security.KeyStore;
import java.security.cert.X509Certificate;

import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactorySpi;
import javax.net.ssl.X509TrustManager;

/**
 * Bogus {@link TrustManagerFactorySpi} which accepts any certificate even if it is invalid.
 */
public class SecureSocketTrustManagerFactory extends TrustManagerFactorySpi {

    private static final TrustManager DUMMY_TRUST_MANAGER = new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }
    };

    private static final TrustManager[] TRUST_MANAGERS = new TrustManager[]{DUMMY_TRUST_MANAGER};

    static TrustManager[] getTrustManagers() {
        return TRUST_MANAGERS;
    }

    @Override
    protected TrustManager[] engineGetTrustManagers() {
        return getTrustManagers();
    }

    @Override
    protected void engineInit(KeyStore keystore) {
    }

    @Override
    protected void engineInit(ManagerFactoryParameters managerFactoryParameters) {
    }

}