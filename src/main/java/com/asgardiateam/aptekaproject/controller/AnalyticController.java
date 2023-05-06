package com.asgardiateam.aptekaproject.controller;

import com.asgardiateam.aptekaproject.service.interfaces.AnalyticService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.asgardiateam.aptekaproject.constants.ApiConstants.ANALYTICS;
import static com.asgardiateam.aptekaproject.constants.ApiConstants.API_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = API_V1 + ANALYTICS, produces = APPLICATION_JSON_VALUE)
public class AnalyticController {

    private final AnalyticService analyticService;

//    @GetMapping(USERS)

}
