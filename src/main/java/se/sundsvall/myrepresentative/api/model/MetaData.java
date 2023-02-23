package se.sundsvall.myrepresentative.api.model;


import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Page data for response.
 * Number of objects on current page (count) is omitted from bolagsverket, and hence omitted here as well
 */
@Getter
@Setter
@Builder(setterPrefix = "with")
@Schema(description = "Metadata model")
public class MetaData {

    @Schema(description = "Current page", example = "5", accessMode = READ_ONLY)
    private int page;

    @Schema(description = "Displayed objects per page", example = "20", accessMode = READ_ONLY)
    private int limit;

    @Schema(description = "Total amount of hits based on provided search parameters", example = "98", accessMode = READ_ONLY)
    private long totalRecords;

    @Schema(description = "Total amount of pages based on provided search parameters", example = "23", accessMode = READ_ONLY)
    private int totalPages;
}

