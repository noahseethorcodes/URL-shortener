package com.noahseethorcodes.urlshortener.services;

import com.noahseethorcodes.urlshortener.models.URLobj;
import com.noahseethorcodes.urlshortener.repositories.URLRepository;
import com.noahseethorcodes.urlshortener.utils.URLKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Service
public class URLService {
    private static final URLKey url_key_gen = new URLKey(7);
    private static final URLKey secret_key_gen = new URLKey(11);
    @Autowired
    private URLRepository repository;

    public List<URLobj> getAllURL() {
        return repository.findAll();
    }

    public URLobj addURL(URLobj url) {
        url.setActive(true);
        url.setClicks(0);
        url.setKey(create_unique_key());
        String admin_key = String.format("%s_%s", url.getKey(), secret_key_gen.NewKey());
        url.setAdminKey(admin_key);
        return repository.save(url);
    }

    private String create_unique_key() {
        String unique_key = url_key_gen.NewKey();
        while (repository.findByKey(unique_key).isPresent()) {
            unique_key = url_key_gen.NewKey();
        }
        return unique_key;
    }


    public Optional<URLobj> getActiveURLByKey(String key) {
        return repository.findByKeyAndActiveTrue(key);
    }

    public Boolean isValidTargetURL(String target_url) {
        HttpURLConnection con = null;
        try {
            URL url = new URI(target_url).toURL();
            con = (HttpURLConnection) url.openConnection();
            return (200 <= con.getResponseCode() && con.getResponseCode() <= 399);
        } catch (URISyntaxException use) {
            return false;
        } catch (IOException ex) {
            assert con != null;
            con.disconnect();
            return false;
        }
    }

    public void incrementClicks(URLobj url) {
        url.setClicks(url.getClicks() + 1);
        repository.save(url);
    }

    public Optional<URLobj> getActiveURLByAdminKey(String admin_key) {
        return repository.findByAdminKeyAndActiveTrue(admin_key);
    }

    public void deactivateURL(URLobj url) {
        url.setActive(false);
        repository.save(url);
    }
}
