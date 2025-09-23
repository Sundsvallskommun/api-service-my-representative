package se.sundsvall.myrepresentative.integration.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "grantee",
	indexes = {
		@Index(name = "idx_mandate_id", columnList = "mandate_id"),
		@Index(name = "idx_party_id", columnList = "party_id")
	})
public class GranteeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", length = 36)
	private String id;

	@Column(name = "party_id", nullable = false, length = 36)
	private String partyId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mandate_id", nullable = false, foreignKey = @ForeignKey(name = "fk_grantee_mandate"), referencedColumnName = "id")
	private MandateEntity mandate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public MandateEntity getMandate() {
		return mandate;
	}

	public void setMandate(MandateEntity mandate) {
		this.mandate = mandate;
	}

	public GranteeEntity withId(String id) {
		this.id = id;
		return this;
	}

	public GranteeEntity withPartyId(String partyId) {
		this.partyId = partyId;
		return this;
	}

	public GranteeEntity withMandate(MandateEntity mandate) {
		this.mandate = mandate;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof final GranteeEntity that))
			return false;
		return Objects.equals(id, that.id) && Objects.equals(partyId, that.partyId) && Objects.equals(mandate, that.mandate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, partyId, mandate);
	}

	@Override
	public String toString() {
		return "GranteeEntity{" +
			"id='" + id + '\'' +
			", partyId='" + partyId + '\'' +
			", mandate=" + mandate +
			'}';
	}
}
