package se.sundsvall.myrepresentative.integration.minaombud.jwks;

import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.stereotype.Component;
import org.zalando.problem.Problem;

import generated.se.sundsvall.minaombud.Jwk;
import generated.se.sundsvall.minaombud.JwkSet;

@Component
public class MinaOmbudJwksClient {

    private final MinaOmbudJwksIntegration jwksIntegration;


    public MinaOmbudJwksClient(final MinaOmbudJwksIntegration jwksIntegration) {
        this.jwksIntegration = jwksIntegration;
    }

    /**
     * Fetches the jwks from mina ombud and converts it to a com.nimbusds.jose.jwk.JWKSet.
     * @param thirdParty The third party to fetch the jwks for
     * @return  A com.nimbusds.jose.jwk.JWKSet
     */
    public JWKSet getJwks(final String thirdParty) {
        final JwkSet jwks = jwksIntegration.getJwks(thirdParty);
        final List<JWK> jwkList = new ArrayList<>();  //Temp list used to initialize the JWKSet at the end

        for (final Jwk jwk : jwks.getKeys()) {
            //Parse the JWK to a JOSE JWK
            try {
                JWK parsedJwk = JWK.parse(jwkToMap(jwk));

                jwkList.add(parsedJwk);
            } catch (final ParseException e) {
                throw Problem.builder()
                        .withTitle("Couldn't parse JWK set from Mina Ombud")
                        .withStatus(INTERNAL_SERVER_ERROR)
                        .build();
            }
        }

        return new JWKSet(jwkList);
    }

    /**
     * Convert a JWK to a Map
     * Breaking changes in OpenAPI Generator 7.0 means that the generated classes from Mina Ombud
     * doesn't have the same structure as before. It doesn't inherit from a hashmap anymore, which
     * breaks the integration with Nimbus JOSE JWT library. This method is a workaround to convert
     * the generated JWK to a map that can be used by the Nimbus JOSE JWT library.
     * @param jwk
     * @return
     */
    private HashMap<String, Object> jwkToMap(final Jwk jwk) {
        HashMap<String, Object> jwkMap = new HashMap<>();

        Optional.ofNullable(jwk.getAdditionalProperties().get("e")).ifPresent(e -> jwkMap.put("e", e));
        Optional.ofNullable(jwk.getAdditionalProperties().get("n")).ifPresent(n -> jwkMap.put("n", n));
        Optional.ofNullable(jwk.getKty()).ifPresent(kty -> jwkMap.put("kty", kty));
        Optional.ofNullable(jwk.getUse()).ifPresent(use -> jwkMap.put("use", use));
        Optional.ofNullable(jwk.getKeyOps()).ifPresent(keyOps -> jwkMap.put("keyOps", keyOps));
        Optional.ofNullable(jwk.getAlg()).ifPresent(alg -> jwkMap.put("alg", alg));
        Optional.ofNullable(jwk.getKid()).ifPresent(kid -> jwkMap.put("kid", kid));
        Optional.ofNullable(jwk.getX5u()).ifPresent(x5u -> jwkMap.put("x5u", x5u));
        Optional.ofNullable(jwk.getX5c()).ifPresent(x5c -> jwkMap.put("x5c", x5c));
        Optional.ofNullable(jwk.getX5t()).ifPresent(x5t -> jwkMap.put("x5t", x5t));
        Optional.ofNullable(jwk.getX5tHashS256()).ifPresent(x5tHashS256 -> jwkMap.put("x5tHashS256", x5tHashS256));
        return jwkMap;
    }

}
