package se.sundsvall.myrepresentative.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesRequest;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesResponse;
import se.sundsvall.myrepresentative.api.model.jwks.Jwks;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesRequest;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesResponse;
import se.sundsvall.myrepresentative.api.validation.RequestValidator;
import se.sundsvall.myrepresentative.service.RepresentativesService;
import se.sundsvall.myrepresentative.service.jwt.JwksCache;

@RestController
@RequestMapping(value = "/")
@Tag(name = "MyRepresentatives")
@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = Problem.class)))
@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = Problem.class)))
@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = Problem.class)))
public class RepresentativesResource {

	public static final String JWKS_ENDPOINT = "/jwks";

	private final RepresentativesService representativesService;
	private final JwksCache jwksCache;
	private final RequestValidator requestValidator;

	public RepresentativesResource(final RepresentativesService representativesService, JwksCache jwksCache, RequestValidator requestValidator) {
		this.representativesService = representativesService;
		this.jwksCache = jwksCache;
		this.requestValidator = requestValidator;
	}

	@ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(schema = @Schema(implementation = MandatesResponse.class)))
	@GetMapping(value = "/getMandates", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<MandatesResponse> getMandates(@Valid @ParameterObject MandatesRequest request) {
		requestValidator.validate(request);
		return ResponseEntity.ok(representativesService.getMandates(request));
	}

	@ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(schema = @Schema(implementation = AuthoritiesResponse.class)))
	@GetMapping(value = "/getAuthorities", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<AuthoritiesResponse> getAuthorities(@Valid @ParameterObject AuthoritiesRequest request) {
		requestValidator.validate(request);
		return ResponseEntity.ok(representativesService.getAuthorities(request));
	}

	@ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(schema = @Schema(implementation = Jwks.class)))
	@GetMapping(value = JWKS_ENDPOINT, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Jwks> jwks() {
		return ResponseEntity.ok(jwksCache.getJwks());
	}
}
