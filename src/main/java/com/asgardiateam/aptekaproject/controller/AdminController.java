package com.asgardiateam.aptekaproject.controller;

import com.asgardiateam.aptekaproject.payload.request.AdminRequest;
import com.asgardiateam.aptekaproject.service.interfaces.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.asgardiateam.aptekaproject.common.ResponseData.ok;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/admin", produces = APPLICATION_JSON_VALUE)
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public Object getMe() {
        return ok(adminService.getMe());
    }

    @PutMapping
    public Object updateMe(@RequestBody @Valid AdminRequest request) {
        return ok(adminService.updateMe(request));
    }
}
