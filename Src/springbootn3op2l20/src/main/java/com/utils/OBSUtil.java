package com.utils;

import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

@Component
public class OBSUtil {

    @Value("${obs.access-key}")
    private String accessKey;

    @Value("${obs.secret-key}")
    private String secretKey;

    @Value("${obs.endpoint}")
    private String endpoint;

    @Value("${obs.bucket-name}")
    private String bucketName;

    private ObsClient createClient() {
        return new ObsClient(accessKey, secretKey, endpoint);
    }

    public String uploadFile(String objectName, InputStream inputStream) {
        try (ObsClient client = createClient()) {
            client.putObject(bucketName, objectName, inputStream);
            return "https://" + bucketName + "." + endpoint.substring(endpoint.indexOf("obs")) + "/" + objectName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteFile(String objectUrl) {
        try (ObsClient client = createClient()) {
            URI uri = new URI(objectUrl);
            String objectKey = uri.getPath().substring(1);
            client.deleteObject(bucketName, objectKey);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

