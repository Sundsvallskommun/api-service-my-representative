package se.sundsvall.myrepresentative.integration.minaombud.jwks;

import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.stereotype.Component;
import org.zalando.problem.Problem;

import generated.se.sundsvall.minaombud.Jwk;
import generated.se.sundsvall.minaombud.JwkSet;

@Component
public class MinaOmbudJwksClient {

    private final MinaOmbudJwksIntegration jwksIntegration;


    public MinaOmbudJwksClient(MinaOmbudJwksIntegration jwksIntegration) {
        this.jwksIntegration = jwksIntegration;
    }

    /**
     * Fetches the jwks from mina ombud and converts it to a com.nimbusds.jose.jwk.JWKSet.
     * @param thirdParty
     * @return
     */
    public JWKSet getJwks(String thirdParty) {
        JwkSet jwks = jwksIntegration.getJwks(thirdParty);
        List<JWK> jwkList = new ArrayList<>();  //Temp list used to initialize the JWKSet at the end

        for (Jwk jwk : jwks.getKeys()) {
            //Parse the JWK to a JOSE JWK
            try {
                JWK parsedJwk = JWK.parse(jwk);
                jwkList.add(parsedJwk);
            } catch (ParseException e) {
                throw Problem.builder()
                        .withTitle("Couldn't parse JWK set from Mina Ombud")
                        .withStatus(INTERNAL_SERVER_ERROR)
                        .build();
            }
        }

        return new JWKSet(jwkList);
    }
}
