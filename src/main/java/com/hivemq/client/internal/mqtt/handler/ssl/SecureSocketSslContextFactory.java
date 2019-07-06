package com.hivemq.client.internal.mqtt.handler.ssl;

import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;

import io.netty.handler.ssl.SslHandler;
import io.netty.util.internal.SystemPropertyUtil;

/**
 * Creates a bogus {@link SSLContext}.  A client-side context created by this
 * factory accepts any certificate even if it is invalid.  A server-side context
 * created by this factory sends a bogus certificate defined in {@link SecureSocketKeyStore}.
 * <p>
 * You will have to create your context differently in a real world application.
 *
 * <h3>Client Certificate Authentication</h3>
 * <p>
 * To enable client certificate authentication:
 * <ul>
 * <li>Enable client authentication on the server side by calling
 * {@link SSLEngine#setNeedClientAuth(boolean)} before creating
 * {@link SslHandler}.</li>
 * <li>When initializing an {@link SSLContext} on the client side,
 * specify the {@link KeyManager} that contains the client certificate as
 * the first argument of {@link SSLContext#init(KeyManager[], TrustManager[], SecureRandom)}.</li>
 * <li>When initializing an {@link SSLContext} on the server side,
 * specify the proper {@link TrustManager} as the second argument of
 * {@link SSLContext#init(KeyManager[], TrustManager[], SecureRandom)}
 * to validate the client certificate.</li>
 * </ul>
 */
public final class SecureSocketSslContextFactory {

    private static final String PROTOCOL = "TLSv1.2";

    private static SSLContext sslContext;

    static {
        try {
            sslContext = SSLContext.getInstance(PROTOCOL);
            sslContext.init(null, SecureSocketTrustManagerFactory.getTrustManagers(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SSLContext getServerContext() {
        String algorithm = SystemPropertyUtil.get("ssl.KeyManagerFactory.algorithm");
        if (algorithm == null) {
            algorithm = "SunX509";
        }

        SSLContext serverContext;
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(SecureSocketKeyStore.asInputStream(), SecureSocketKeyStore.getKeyStorePassword());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
            kmf.init(ks, SecureSocketKeyStore.getCertificatePassword());

            serverContext = SSLContext.getInstance(PROTOCOL);
            serverContext.init(kmf.getKeyManagers(), null, null);

        } catch (Exception e) {
            throw new Error("Failed to initialize the server-side SSLContext", e);
        }
        return serverContext;
    }

    public static SSLContext getSslContext() {
        return sslContext;
    }

    private SecureSocketSslContextFactory() {
    }

}