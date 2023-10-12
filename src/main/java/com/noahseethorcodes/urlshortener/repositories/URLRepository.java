package com.noahseethorcodes.urlshortener.repositories;

import com.noahseethorcodes.urlshortener.models.URLobj;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface URLRepository extends JpaRepository<URLobj, Long> {
    Optional<URLobj> findByKey(String key);

    Optional<URLobj> findByAdminKey(String admin_key);

    Optional<URLobj> findByKeyAndActiveTrue(String key);

    Optional<URLobj> findByAdminKeyAndActiveTrue(String key);
}
