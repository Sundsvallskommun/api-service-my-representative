package se.sundsvall.myrepresentative.api.model.jwks;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Simple representation of the JSON Web Key Set (JWKS).")
@NoArgsConstructor
@AllArgsConstructor
public class Jwks {

	private List<Map<String, Object>> keys;
}
