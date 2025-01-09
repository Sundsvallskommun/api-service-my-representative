package se.sundsvall.myrepresentative.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import se.sundsvall.dept44.common.validators.annotation.ValidMunicipalityId;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesRequest;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesResponse;
import se.sundsvall.myrepresentative.api.model.jwks.Jwks;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesRequest;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesResponse;
import se.sundsvall.myrepresentative.api.validation.RequestValidator;
import se.sundsvall.myrepresentative.service.RepresentativesService;
import se.sundsvall.myrepresentative.service.jwt.JwtService;

@RestController
@RequestMapping(value = "/{municipalityId}/")
@Tag(name = "MyRepresentatives")
@Validated
@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = {
	Problem.class, ConstraintViolationProblem.class
})))
@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
class RepresentativesResource {

	private final RepresentativesService representativesService;

	private final RequestValidator requestValidator;

	private final JwtService jwtService;

	RepresentativesResource(final RepresentativesService representativesService, final RequestValidator requestValidator, final JwtService jwtService) {
		this.representativesService = representativesService;
		this.requestValidator = requestValidator;
		this.jwtService = jwtService;
	}

	@Operation(summary = "Get mandates",
		responses = {
			@ApiResponse(responseCode = "200",
				description = "Successful Operation",
				useReturnTypeSchema = true)
		})
	@GetMapping(value = "/mandates", produces = APPLICATION_JSON_VALUE)
	ResponseEntity<MandatesResponse> getMandates(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Valid @ParameterObject final MandatesRequest request) {
		requestValidator.validate(request);
		return ResponseEntity.ok(representativesService.getMandates(municipalityId, request));
	}

	@Operation(summary = "Get authorities",
		responses = {
			@ApiResponse(responseCode = "200",
				description = "Successful Operation",
				useReturnTypeSchema = true)
		})
	@GetMapping(value = "/authorities", produces = APPLICATION_JSON_VALUE)
	ResponseEntity<AuthoritiesResponse> getAuthorities(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Valid @ParameterObject final AuthoritiesRequest request) {
		requestValidator.validate(request);
		return ResponseEntity.ok(representativesService.getAuthorities(municipalityId, request));
	}

	@Operation(summary = "Get JWKS",
		responses = {
			@ApiResponse(responseCode = "200",
				description = "Successful Operation",
				useReturnTypeSchema = true)
		})
	@GetMapping(value = "/jwks", produces = APPLICATION_JSON_VALUE)
	ResponseEntity<Jwks> jwks(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId) {
		return ResponseEntity.ok(jwtService.getJwks(municipalityId));
	}
}
