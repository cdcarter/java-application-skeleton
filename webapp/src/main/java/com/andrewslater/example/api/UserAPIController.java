package com.andrewslater.example.api;

import com.andrewslater.example.Mappings;
import com.andrewslater.example.api.assemblers.UserResourceAssembler;
import com.andrewslater.example.api.resources.UserResource;
import com.andrewslater.example.models.User;
import com.andrewslater.example.repositories.UserRepository;
import com.fasterxml.jackson.annotation.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = "application/hal+json")
public class UserAPIController {
    private static final Logger LOG = LoggerFactory.getLogger(UserAPIController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier("userResourceAssembler")
    private UserResourceAssembler userAssembler;

    @RequestMapping(value = Mappings.API_USER_RESOURCE, method = RequestMethod.GET)
    @JsonView(APIView.Authenticated.class)
    public HttpEntity<UserResource> getUser(@PathVariable Long userId) {
        User user = userRepository.findOne(userId);
        return new ResponseEntity<>(userAssembler.toResource(user), HttpStatus.OK);
    }

    @RequestMapping(value = Mappings.API_USER_RESOURCE, method = RequestMethod.PATCH)
    @JsonView(APIView.Authenticated.class)
    public HttpEntity<UserResource> patchUser(@PathVariable Long userId, @RequestBody User user) {
        User existingUser = userRepository.findOne(userId);
        return new ResponseEntity<>(userAssembler.toResource(user), HttpStatus.OK);
    }
}
