package com.testapp.api.config;

import org.springframework.context.annotation.Configuration;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;

@Configuration
public class SecurityConfig {
    
    // VULNERABILITY: Hardcoded database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Password123!";
    
    // VULNERABILITY: Hardcoded AWS credentials
    private static final String AWS_ACCESS_KEY = "AKIAIOSFODNN7EXAMPLE";
    private static final String AWS_SECRET_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";
    
    // VULNERABILITY: Disabled SSL certificate validation
    public void disableSSLValidation() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };
            
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            
            // VULNERABILITY: Disabled hostname verification
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String getDatabaseUrl() {
        return DB_URL;
    }
    
    public String getDatabaseUser() {
        return DB_USER;
    }
    
    public String getDatabasePassword() {
        return DB_PASS;
    }
    
    public String getAwsAccessKey() {
        return AWS_ACCESS_KEY;
    }
    
    public String getAwsSecretKey() {
        return AWS_SECRET_KEY;
    }
}
