package com.asgardiateam.aptekaproject.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditingAwareBean implements AuditorAware<String> {

    @Override
    @SuppressWarnings("NullableProblems")
    public Optional<String> getCurrentAuditor() {
        return Optional.of("User");
    }
}
