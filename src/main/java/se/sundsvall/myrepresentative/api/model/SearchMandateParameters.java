package se.sundsvall.myrepresentative.api.model;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.dept44.models.api.paging.AbstractParameterPagingBase;

@Schema(description = "SearchMandateParameters model")
public class SearchMandateParameters extends AbstractParameterPagingBase {

	@ValidUuid(nullable = true)
	@Schema(description = "The partyId of the issuing organization or person", example = "fb2f0290-3820-11ed-a261-0242ac120002", requiredMode = NOT_REQUIRED)
	private String grantorPartyId;

	@ValidUuid(nullable = true)
	@Schema(description = "PartyId of the grantee", example = "fb2f0290-3820-11ed-a261-0242ac120004", requiredMode = NOT_REQUIRED)
	private String granteePartyId;

	@ValidUuid(nullable = true)
	@Schema(description = "PartyId of the issuing person / signatory", example = "fb2f0290-3820-11ed-a261-0242ac120003", requiredMode = NOT_REQUIRED)
	private String signatoryPartyId;

	public String getGrantorPartyId() {
		return grantorPartyId;
	}

	public void setGrantorPartyId(String grantorPartyId) {
		this.grantorPartyId = grantorPartyId;
	}

	public String getGranteePartyId() {
		return granteePartyId;
	}

	public void setGranteePartyId(String granteePartyId) {
		this.granteePartyId = granteePartyId;
	}

	public String getSignatoryPartyId() {
		return signatoryPartyId;
	}

	public void setSignatoryPartyId(String signatoryPartyId) {
		this.signatoryPartyId = signatoryPartyId;
	}

	public SearchMandateParameters withGrantorPartyId(String grantorPartyId) {
		this.grantorPartyId = grantorPartyId;
		return this;
	}

	public SearchMandateParameters withGranteePartyId(String granteePartyId) {
		this.granteePartyId = granteePartyId;
		return this;
	}

	public SearchMandateParameters withSignatoryPartyId(String signatoryPartyId) {
		this.signatoryPartyId = signatoryPartyId;
		return this;
	}

	public SearchMandateParameters withPage(int page) {
		this.setPage(page);
		return this;
	}

	public SearchMandateParameters withLimit(int limit) {
		this.setLimit(limit);
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof final SearchMandateParameters that))
			return false;
		if (!super.equals(o))
			return false;
		return Objects.equals(grantorPartyId, that.grantorPartyId) && Objects.equals(granteePartyId, that.granteePartyId) && Objects.equals(signatoryPartyId, that.signatoryPartyId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), grantorPartyId, granteePartyId, signatoryPartyId);
	}

	@Override
	public String toString() {
		return "SearchMandateParameters{" +
			"grantorPartyId='" + grantorPartyId + '\'' +
			", granteePartyId='" + granteePartyId + '\'' +
			", signatoryPartyId='" + signatoryPartyId + '\'' +
			", page=" + page +
			", limit=" + limit +
			'}';
	}
}
