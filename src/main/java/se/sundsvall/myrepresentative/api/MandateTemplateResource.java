package se.sundsvall.myrepresentative.api;


import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;

import se.sundsvall.myrepresentative.api.model.MandateTemplate;
import se.sundsvall.myrepresentative.service.mandatetemplate.MandateTemplateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/mandates/templates")
@Tag(name = "Mandate Templates", description = "Resources for managing mandate templates")
@ApiResponses(value = {
	@ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = {Problem.class, ConstraintViolationProblem.class}))),
	@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class))),
	@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
})
public class MandateTemplateResource {

	private final MandateTemplateService service;

	public MandateTemplateResource(final MandateTemplateService service) {this.service = service;}

	@GetMapping
	@Operation(description = "Get all mandate templates", responses = {
		@ApiResponse(responseCode = "200", description = "OK - Successful operation", useReturnTypeSchema = true)})
	ResponseEntity<List<MandateTemplate>> getAllMandateTemplates() {

		return ResponseEntity.ok(service.getTemplates());
	}

	@GetMapping("/{id}")
	@Operation(description = "Get a mandate template by id", responses = {
		@ApiResponse(responseCode = "200", description = "OK - Successful operation", useReturnTypeSchema = true)
	})
	ResponseEntity<MandateTemplate> getMandateTemplate(@PathVariable(name = "id") final String id) {
		return ResponseEntity.ok(service.getTemplate(id));
	}

	@PostMapping
	@Operation(description = "Create Mandate Template", responses = {
		@ApiResponse(responseCode = "201", description = "Created - Successful operation", headers = @Header(name = LOCATION, description = "Location of the created resource."))
	})
	ResponseEntity<Void> createMandateTemplate(@RequestBody final MandateTemplate mandateTemplate) {
		return created(
			UriComponentsBuilder.fromPath("/mandates/templates/{id}")
				.buildAndExpand(service.createTemplate(mandateTemplate))
				.toUri())
			.header(CONTENT_TYPE, ALL_VALUE)
			.build();

	}

	@PutMapping("/{id}")
	@Operation(description = "Update Mandate Template", responses = {
		@ApiResponse(responseCode = "204", description = "No Content - Successful operation"),
	})
	ResponseEntity<Void> updateMandateTemplate(@PathVariable(name = "id") final String id, @RequestBody final MandateTemplate mandateTemplate) {
		service.updateTemplate(id, mandateTemplate);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	@Operation(description = "Delete Mandate Template", responses = {
		@ApiResponse(responseCode = "204", description = "No Content - Successful operation"),
	})
	ResponseEntity<Void> deleteMandateTemplate(@PathVariable(name = "id") final String id) {
		service.deleteTemplate(id);
		return ResponseEntity.noContent().build();
	}

}
