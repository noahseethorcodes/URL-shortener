package com.noahseethorcodes.urlshortener.utils;

import com.noahseethorcodes.urlshortener.controllers.MainController;
import com.noahseethorcodes.urlshortener.models.URLobj;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class URLobjModelAssembler implements RepresentationModelAssembler<URLobj, EntityModel<URLobj>> {
    @Override
    public EntityModel<URLobj> toModel(URLobj url) {
        return EntityModel.of(url,
                linkTo(methodOn(MainController.class).getURLAdminInfo(url.getAdminKey())).withSelfRel(),
                linkTo(methodOn(MainController.class).peekURLBehindKey(url.getKey())).withRel("peek at target URL"),
                linkTo(methodOn(MainController.class).forwardToTargetURL(url.getKey())).withRel("forward to target URL"),
                linkTo(methodOn(MainController.class).deleteURL(url.getAdminKey())).withRel("delete URL"));
    }
}
