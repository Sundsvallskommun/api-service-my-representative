package se.sundsvall.myrepresentative.api.model.authorities;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import se.sundsvall.myrepresentative.api.model.MetaData;

@Getter
@Setter
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Mandate response model")
public class AuthoritiesResponse {

	@Builder.Default
	private List<Authority> authorities = new ArrayList<>();

	public void addAuthority(Authority authority) {
		authorities.add(authority);
	}

	@JsonProperty("_meta")
	@Schema(implementation = MetaData.class, accessMode = READ_ONLY)
	private MetaData metaData;
}
