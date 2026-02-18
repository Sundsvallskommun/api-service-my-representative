package se.sundsvall.myrepresentative.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import se.sundsvall.dept44.common.validators.annotation.ValidMunicipalityId;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.myrepresentative.api.model.CreateMandate;
import se.sundsvall.myrepresentative.api.model.MandateDetails;
import se.sundsvall.myrepresentative.api.model.Mandates;
import se.sundsvall.myrepresentative.api.model.SearchMandateParameters;
import se.sundsvall.myrepresentative.api.validation.ValidNamespace;
import se.sundsvall.myrepresentative.service.RepresentativesService;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

@RestController
@RequestMapping(value = "/{municipalityId}/{namespace}")
@Tag(name = "MyRepresentatives")
@Validated
@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = {
	Problem.class, ConstraintViolationProblem.class
})))
@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
class MandatesResource {

	private final RepresentativesService representativesService;

	MandatesResource(final RepresentativesService representativesService) {
		this.representativesService = representativesService;
	}

	@Operation(summary = "Create mandates",
		responses = {
			@ApiResponse(
				responseCode = "201",
				headers = @Header(name = LOCATION, schema = @Schema(type = "string")),
				description = "Successful Operation",
				useReturnTypeSchema = true),
			@ApiResponse(
				responseCode = "403",
				headers = @Header(name = LOCATION, schema = @Schema(type = "string")),
				description = "Forbidden",
				useReturnTypeSchema = true),
			@ApiResponse(responseCode = "409",
				description = "Conflict",
				content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE,
					schema = @Schema(implementation = Problem.class)))
		})
	@PostMapping(value = "/mandates", consumes = APPLICATION_JSON_VALUE, produces = ALL_VALUE)
	ResponseEntity<Void> createMandate(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "namespace", description = "Namespace", example = "MY_NAMESPACE") @ValidNamespace @PathVariable final String namespace,
		@Valid @RequestBody final CreateMandate request) {

		final var mandateId = representativesService.createMandate(municipalityId, namespace, request);

		return ResponseEntity.created(fromPath("/{municipalityId}/{namespace}/mandates/{id}")
			.buildAndExpand(municipalityId, namespace, mandateId).toUri())
			.header(CONTENT_TYPE, ALL_VALUE)
			.build();
	}

	@Operation(summary = "Search mandates",
		responses = {
			@ApiResponse(responseCode = "200",
				description = "Successful Operation",
				useReturnTypeSchema = true)
		})
	@GetMapping(value = "/mandates", produces = APPLICATION_JSON_VALUE)
	ResponseEntity<Mandates> searchMandates(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "namespace", description = "Namespace", example = "MY_NAMESPACE") @ValidNamespace @PathVariable final String namespace,
		@Valid final SearchMandateParameters searchParameters) {
		return ResponseEntity.ok(representativesService.searchMandates(municipalityId, namespace, searchParameters));
	}

	@Operation(summary = "Get mandate by id, also returns BankID signing information",
		responses = {
			@ApiResponse(responseCode = "200",
				description = "Successful Operation",
				useReturnTypeSchema = true),
			@ApiResponse(responseCode = "404",
				description = "Not Found",
				content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE,
					schema = @Schema(implementation = Problem.class)))
		})
	@GetMapping(value = "/mandates/{id}", produces = APPLICATION_JSON_VALUE)
	ResponseEntity<MandateDetails> getMandateById(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "namespace", description = "Namespace", example = "MY_NAMESPACE") @ValidNamespace @PathVariable final String namespace,
		@Parameter(name = "id", description = "Id of the mandate", example = "2facc7a8-69e1-4988-9b3d-4da6cefab704") @ValidUuid @PathVariable final String id) {

		return ResponseEntity.ok(representativesService.getMandateDetails(municipalityId, namespace, id));
	}

	@Operation(summary = "Delete mandate, performs a soft delete",
		responses = {
			@ApiResponse(responseCode = "204",
				description = "No Content",
				useReturnTypeSchema = true)
		})
	@DeleteMapping(value = "/mandates/{id}", produces = ALL_VALUE)
	ResponseEntity<Void> deleteMandate(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "namespace", description = "Namespace", example = "MY_NAMESPACE") @ValidNamespace @PathVariable final String namespace,
		@Parameter(name = "id", description = "Id of the mandate", example = "2facc7a8-69e1-4988-9b3d-4da6cefab704") @ValidUuid @PathVariable final String id) {
		representativesService.deleteMandate(municipalityId, namespace, id);
		return ResponseEntity.noContent().build();
	}

}
