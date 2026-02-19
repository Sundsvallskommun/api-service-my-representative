package se.sundsvall.myrepresentative.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;
import se.sundsvall.myrepresentative.config.Builder;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@Schema(description = "Paginated response containing a list of mandate details")
@Builder
public record Mandates(
	@Schema(description = "List of mandates", accessMode = READ_ONLY) List<MandateDetails> mandateDetailsList,

	@JsonProperty("_meta") @Schema(implementation = PagingAndSortingMetaData.class, accessMode = READ_ONLY) PagingAndSortingMetaData metaData) {
}
