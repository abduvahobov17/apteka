package com.asgardiateam.aptekaproject.controller;

import com.asgardiateam.aptekaproject.entity.dynamicquery.criteria.UserCriteria;
import com.asgardiateam.aptekaproject.enums.BotState;
import com.asgardiateam.aptekaproject.enums.ClientType;
import com.asgardiateam.aptekaproject.enums.Lang;
import com.asgardiateam.aptekaproject.payload.MessageDTO;
import com.asgardiateam.aptekaproject.payload.request.UserRequest;
import com.asgardiateam.aptekaproject.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.asgardiateam.aptekaproject.common.ResponseData.ok;
import static com.asgardiateam.aptekaproject.constants.ApiConstants.*;
import static com.asgardiateam.aptekaproject.constants.MessageKey.SUCCESS_MESSAGE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = API_V1 + USERS, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Object getAll(@PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                         UserCriteria criteria) {
        return ok(userService.getAll(pageable, criteria));
    }

    @GetMapping("/{userId}")
    public Object getById(@PathVariable Long userId) {
        return ok(userService.getById(userId));
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public Object create(@RequestBody @Valid UserRequest request) {
        return ok(userService.create(request));
    }

    @PutMapping(path = "/{userId}", consumes = APPLICATION_JSON_VALUE)
    public Object update(@PathVariable Long userId,
                         @RequestBody @Valid UserRequest request) {
        return ok(userService.update(request, userId));
    }

    @DeleteMapping("/{userId}")
    public Object delete(@PathVariable Long userId) {
        userService.deleteById(userId);
        return ok(new MessageDTO(SUCCESS_MESSAGE));
    }

    @GetMapping(CLIENT_TYPES)
    public Object getClientTypes() {
        return ok(List.of(ClientType.values()));
    }

    @GetMapping(BOT_STATES)
    public Object getBotStates() {
        return ok(List.of(BotState.values()));
    }

    @GetMapping(LANG_TYPES)
    public Object getLangTypes() {
        return ok(List.of(Lang.values()));
    }
}
