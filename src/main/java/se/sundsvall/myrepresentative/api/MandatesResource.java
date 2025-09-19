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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import se.sundsvall.dept44.common.validators.annotation.ValidMunicipalityId;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.myrepresentative.api.model.CreateMandate;
import se.sundsvall.myrepresentative.api.model.MandateDetails;
import se.sundsvall.myrepresentative.api.model.UpdateMandate;

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

	@Operation(summary = "Create mandates",
		responses = {
			@ApiResponse(responseCode = "201",
				description = "Created",
				useReturnTypeSchema = true)
		})
	@PostMapping(value = "/mandates", produces = APPLICATION_JSON_VALUE)
	ResponseEntity<Void> createMandate(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Valid @ParameterObject final CreateMandate request) {
		// TODO implement
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}

	@Operation(summary = "Get mandates",
		responses = {
			@ApiResponse(responseCode = "200",
				description = "Successful Operation",
				useReturnTypeSchema = true)
		})
	@GetMapping(value = "/mandates", produces = APPLICATION_JSON_VALUE)
	ResponseEntity<MandateDetails> getMandates(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "signatoryPartyId") @RequestParam(required = false) final String issuerPartyId,
		@Parameter(name = "acquirerPartyId", description = "PartyId of the acquirer of the mandate") @RequestParam(required = false) @ValidUuid(nullable = true) final String acquirerPartyId,
		@Parameter(name = "signatoryPartyId", description = "PartyId of the issuing person") @RequestParam(required = false) @ValidUuid(nullable = true) final String signatoryPartyId) {
		// TODO implement
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}

	@Operation(summary = "Update mandate",
		responses = {
			@ApiResponse(responseCode = "202",
				description = "Accepted",
				useReturnTypeSchema = true)
		})
	@PatchMapping(value = "/mandates", produces = APPLICATION_JSON_VALUE)
	ResponseEntity<MandateDetails> updateMandate(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Valid @ParameterObject final UpdateMandate updateRequest) {
		// TODO implement
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}

	@Operation(summary = "Delete mandate",
		responses = {
			@ApiResponse(responseCode = "202",
				description = "Accepted",
				useReturnTypeSchema = true)
		})
	@DeleteMapping(value = "/mandates", produces = APPLICATION_JSON_VALUE)
	ResponseEntity<MandateDetails> deleteMandate(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Valid @ParameterObject final CreateMandate createRequest) {
		// TODO implement
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}

}
