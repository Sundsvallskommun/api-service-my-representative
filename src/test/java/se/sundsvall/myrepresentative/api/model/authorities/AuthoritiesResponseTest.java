package se.sundsvall.myrepresentative.api.model.authorities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AuthoritiesResponseTest {

    @Test
    void addAuthority() {
        AuthoritiesResponse authoritiesResponse = new AuthoritiesResponse();
        authoritiesResponse.addAuthority(Authority.builder().build());
        assertThat(authoritiesResponse.getAuthorities()).hasSize(1);
    }
}