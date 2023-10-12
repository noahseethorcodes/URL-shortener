package com.noahseethorcodes.urlshortener.controllers;

import com.noahseethorcodes.urlshortener.errors.KeyNotFoundException;
import com.noahseethorcodes.urlshortener.errors.TargetURLNotFound;
import com.noahseethorcodes.urlshortener.models.URLobj;
import com.noahseethorcodes.urlshortener.services.URLService;
import com.noahseethorcodes.urlshortener.utils.URLobjModelAssembler;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class MainController {
    @Autowired
    private URLService service;
    @Autowired
    private URLobjModelAssembler assembler;


    //Production Method to see all URLs in database
    @GetMapping("/allURLs")
    public ResponseEntity<CollectionModel<EntityModel<URLobj>>> getAllURLs() {
        List<EntityModel<URLobj>> all_urls = new ArrayList<EntityModel<URLobj>>();
        if (!service.getAllURL().isEmpty()) {
            all_urls = service.getAllURL().stream()
                    .map(assembler::toModel)
                    .toList();
        }
        CollectionModel<EntityModel<URLobj>> all_urls_collection = CollectionModel.of(all_urls,
                linkTo(methodOn(MainController.class).getAllURLs()).withSelfRel());
        return new ResponseEntity<CollectionModel<EntityModel<URLobj>>>(all_urls_collection, HttpStatus.OK);
    }

    @PostMapping("/addURL")
    public ResponseEntity<EntityModel<URLobj>> createNewURL(@Valid @RequestBody URLobj url) {
        URLobj added_url = service.addURL(url);
        return new ResponseEntity<EntityModel<URLobj>>(assembler.toModel(added_url), HttpStatus.CREATED);
    }

    @GetMapping("/peek_{url_key}")
    public ResponseEntity<String> peekURLBehindKey(@PathVariable String url_key) {
        URLobj accessed_url = service.getActiveURLByKey(url_key)
                .orElseThrow(() -> new KeyNotFoundException(url_key));
        String peek_msg = String.format("URL behind key {%s} is '%s'", url_key, accessed_url.getTargetURL());
        return new ResponseEntity<String>(peek_msg, HttpStatus.OK);
    }

    @GetMapping("/goto_{url_key}")
    public ResponseEntity<Void> forwardToTargetURL(@PathVariable String url_key) {
        URLobj accessed_url = service.getActiveURLByKey(url_key)
                .orElseThrow(() -> new KeyNotFoundException(url_key));
        //Graceful Forwarding
        if (!service.isValidTargetURL(accessed_url.getTargetURL())) {
            throw new TargetURLNotFound(accessed_url.getTargetURL(), url_key);
        }
        service.incrementClicks(accessed_url);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .location(URI.create(accessed_url.getTargetURL())).build();
    }

    @GetMapping("/admin/{admin_key}")
    public ResponseEntity<EntityModel<URLobj>> getURLAdminInfo(@PathVariable String admin_key) {
        URLobj accessed_url = service.getActiveURLByAdminKey(admin_key)
                .orElseThrow(() -> new KeyNotFoundException(admin_key));
        return new ResponseEntity<EntityModel<URLobj>>(assembler.toModel(accessed_url), HttpStatus.OK);
    }

    @DeleteMapping("/admin/{admin_key}")
    public ResponseEntity<String> deleteURL(@PathVariable String admin_key) {
        URLobj accessed_url = service.getActiveURLByAdminKey(admin_key)
                .orElseThrow(() -> new KeyNotFoundException(admin_key));
        service.deactivateURL(accessed_url);
        String del_msg = String.format("Successfully deleted shortened URl for '%s'", accessed_url.getTargetURL());
        return new ResponseEntity<String>(del_msg, HttpStatus.OK);
    }
}
