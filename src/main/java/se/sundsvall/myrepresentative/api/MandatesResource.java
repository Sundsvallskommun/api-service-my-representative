package se.sundsvall.myrepresentative.api;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import se.sundsvall.dept44.common.validators.annotation.ValidMunicipalityId;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.myrepresentative.api.model.CreateMandate;
import se.sundsvall.myrepresentative.api.model.MandateDetails;
import se.sundsvall.myrepresentative.api.model.UpdateMandate;

@RestController
@RequestMapping(value = "/{municipalityId}")
@Tag(name = "MyRepresentatives")
@Validated
@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = {
	Problem.class, ConstraintViolationProblem.class
})))
@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
class MandatesResource {

	@Operation(summary = "Create mandates",
		responses = {
			@ApiResponse(
				responseCode = "201",
				headers = @Header(name = LOCATION, schema = @Schema(type = "string")),
				description = "Successful Operation",
				useReturnTypeSchema = true)
		})
	@PostMapping(value = "/mandates", consumes = APPLICATION_JSON_VALUE, produces = ALL_VALUE)
	ResponseEntity<Void> createMandate(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Valid @RequestBody final CreateMandate request) {
		// TODO implement
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
			.header(CONTENT_TYPE, ALL_VALUE)
			.build();
	}

	@Operation(summary = "Get mandates",
		responses = {
			@ApiResponse(responseCode = "200",
				description = "Successful Operation",
				useReturnTypeSchema = true),
			@ApiResponse(responseCode = "404",
				description = "Not Found",
				content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE,
					schema = @Schema(implementation = Problem.class)))
		})
	@GetMapping(value = "/mandates", produces = APPLICATION_JSON_VALUE)
	ResponseEntity<List<MandateDetails>> getMandates(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "grantorPartyId", description = "PartyId of the grantor (person or organization)", example = "2facc7a8-69e1-4988-9b3d-4da6cefab701") @RequestParam(required = false) @ValidUuid(nullable = true) final String grantorPartyId,
		@Parameter(name = "granteePartyId", description = "PartyId of the grantee of the mandate", example = "2facc7a8-69e1-4988-9b3d-4da6cefab702") @RequestParam(required = false) @ValidUuid(nullable = true) final String granteePartyId,
		@Parameter(name = "signatoryPartyId", description = "PartyId of the signatory", example = "2facc7a8-69e1-4988-9b3d-4da6cefab703") @RequestParam(required = false) @ValidUuid(nullable = true) final String signatoryPartyId) {
		// TODO implement
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
	}

	@Operation(summary = "Update mandate",
		responses = {
			@ApiResponse(responseCode = "200",
				description = "Ok",
				useReturnTypeSchema = true),
			@ApiResponse(responseCode = "404",
				description = "Not Found",
				content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE,
					schema = @Schema(implementation = Problem.class)))

		})
	@PatchMapping(value = "/mandates", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
	ResponseEntity<MandateDetails> updateMandate(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Valid @RequestBody final UpdateMandate updateRequest) {
		// TODO implement
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
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
		@Parameter(name = "id", description = "Id of the mandate", example = "2facc7a8-69e1-4988-9b3d-4da6cefab704") @ValidUuid @PathVariable final String id) {
		// TODO implement
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
			.header(CONTENT_TYPE, ALL_VALUE)
			.build();
	}

}
