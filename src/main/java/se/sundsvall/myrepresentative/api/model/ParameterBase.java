package se.sundsvall.myrepresentative.api.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ParameterBase {

    @Schema(description = "Page number", example = "0", defaultValue = "0")
    @Min(0)
    protected int page = 0;

    @Schema(description = "Result size per page", example = "100", defaultValue = "100")
    @Min(1)
    @Max(1000)
    protected int limit = 100;
}
