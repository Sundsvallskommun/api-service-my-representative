package se.sundsvall.myrepresentative.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static se.sundsvall.myrepresentative.config.OpenApiConfigurationExtension.JWKS_ENDPOINT;

import jakarta.validation.Valid;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;

import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesRequest;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesResponse;
import se.sundsvall.myrepresentative.api.model.jwks.Jwks;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesRequest;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesResponse;
import se.sundsvall.myrepresentative.api.validation.RequestValidator;
import se.sundsvall.myrepresentative.service.RepresentativesService;
import se.sundsvall.myrepresentative.service.jwt.JwtService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/")
@Tag(name = "MyRepresentatives")
@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = { Problem.class, ConstraintViolationProblem.class })))
@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
public class RepresentativesResource {

	private final RepresentativesService representativesService;
	private final RequestValidator requestValidator;
	private final JwtService jwtService;

	public RepresentativesResource(final RepresentativesService representativesService, RequestValidator requestValidator, JwtService jwtService) {
		this.representativesService = representativesService;
		this.requestValidator = requestValidator;
		this.jwtService = jwtService;
	}

	@ApiResponse(responseCode = "200", description = "Successful Operation", useReturnTypeSchema = true)
	@GetMapping(value = "/mandates", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<MandatesResponse> getMandates(@Valid @ParameterObject MandatesRequest request) {
		requestValidator.validate(request);
		return ResponseEntity.ok(representativesService.getMandates(request));
	}

	@ApiResponse(responseCode = "200", description = "Successful Operation", useReturnTypeSchema = true)
	@GetMapping(value = "/authorities", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<AuthoritiesResponse> getAuthorities(@Valid @ParameterObject AuthoritiesRequest request) {
		requestValidator.validate(request);
		return ResponseEntity.ok(representativesService.getAuthorities(request));
	}

	@ApiResponse(responseCode = "200", description = "Successful Operation", useReturnTypeSchema = true)
	@GetMapping(value = JWKS_ENDPOINT, produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<Jwks> jwks() {
		return ResponseEntity.ok(jwtService.getJwks());
	}
}
