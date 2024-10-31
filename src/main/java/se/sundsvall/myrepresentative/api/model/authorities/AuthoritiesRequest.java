package se.sundsvall.myrepresentative.api.model.authorities;

import se.sundsvall.myrepresentative.api.model.GetAcquirer;
import se.sundsvall.myrepresentative.api.model.GetIssuer;
import se.sundsvall.myrepresentative.api.model.ParameterBase;

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
@Schema(description = "Search authorities request model")
public class AuthoritiesRequest extends ParameterBase {

	@Nullable
	@Schema(description = "Issuer of the authority", example = "fb2f0290-3820-11ed-a261-0242ac120003", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	private GetIssuer authorityIssuer;

	@Valid
	@NotNull
	@Schema(description = "Receiver of the authority", example = "fb2f0290-3820-11ed-a261-0242ac120002")
	private GetAcquirer authorityAcquirer;
}
