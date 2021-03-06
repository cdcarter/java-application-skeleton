package com.andrewslater.example.api.controllers;

import com.andrewslater.example.Mappings;
import com.andrewslater.example.api.APIView;
import com.andrewslater.example.models.SystemSettings;
import com.andrewslater.example.services.SystemSettingsService;
import com.fasterxml.jackson.annotation.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminSettingsAPIController {
    private static final Logger LOG = LoggerFactory.getLogger(AdminSettingsAPIController.class);

    @Autowired
    private SystemSettingsService service;

    @RequestMapping(value = Mappings.ADMIN_API_SETTINGS, method = RequestMethod.GET)
    @JsonView(APIView.Internal.class)
    public HttpEntity<SystemSettings> getSettings() {
        return getResponseEntity(service.getSystemSettings());
    }

    @RequestMapping(value = Mappings.ADMIN_API_SETTINGS, method = RequestMethod.PATCH)
    @JsonView(APIView.Internal.class)
    public HttpEntity<SystemSettings> saveSettings(@RequestBody SystemSettings settings) {
        SystemSettings savedSettings = service.patchSystemSettings(settings);
        return getResponseEntity(savedSettings);
    }

    private HttpEntity<SystemSettings> getResponseEntity(SystemSettings systemSettings) {
        return new ResponseEntity<>(systemSettings, HttpStatus.OK);
    }
}
