package se.sundsvall.myrepresentative.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(setterPrefix = "with")
@Schema(description = "Base information for the issuer of mandates / authorities if provided, 'partyId' and 'type' must be set.")
@AllArgsConstructor
@NoArgsConstructor
public class MandateTemplate {

	@Schema(description = "Code for the specific template", example = "bf1a690b-33d6-4a3e-b407-e7346fa1c97c")
	private String code;

	@Schema(description = "Title for the specific template", example = "Fullmakt för att göra och hantera anmälan")
	private String title;

	@Schema(description = "Description for the specific template", example = "Behörigheten ger fullmaktshavaren rätt att upprätta anmälan, ta del av eget utrymme och ändra uppgifter gällande åtgärden samt på annat sätt företräda byggherren i åtgärder enligt 6 kap. 5 § plan- och byggförordningen (2011:338). Behörigheten omfattar även rätt att upprätta, se och ändra uppgifter gällande start- och slutbesked enligt 10 kap. 3 och 4 §§ plan- och bygglagen (2010:900).")
	private String description;

}
