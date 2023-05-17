package se.sundsvall.myrepresentative.api.model.mandates;

import java.util.List;

import se.sundsvall.myrepresentative.api.model.GetAcquirer;
import se.sundsvall.myrepresentative.api.model.GetIssuer;
import se.sundsvall.myrepresentative.api.model.ParameterBase;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
@Schema(description = "Search permissions request model")
public class MandatesRequest extends ParameterBase {

    @Nullable
    @Schema(description = "Issuer of the mandates", example = "fb2f0290-3820-11ed-a261-0242ac120003", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private GetIssuer mandateIssuer;

    @Valid
    @NotNull
    @Schema(description = "Receiver of the mandates", example = "fb2f0290-3820-11ed-a261-0242ac120002")
    private GetAcquirer mandateAcquirer;

    @ArraySchema(minItems = 0, uniqueItems = true,
            schema = @Schema(description = "Fetch only specified mandates", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"))
    private List<String> mandates;

}
