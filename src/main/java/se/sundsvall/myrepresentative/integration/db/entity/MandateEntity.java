package se.sundsvall.myrepresentative.integration.db.entity;

import static java.time.ZoneId.systemDefault;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.hibernate.annotations.TimeZoneStorageType.NORMALIZE;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import org.hibernate.annotations.TimeZoneStorage;
import se.sundsvall.myrepresentative.api.model.MandateStatus;

@Entity
@Table(name = "mandate",
	indexes = {
		@Index(name = "idx_municipality_id_signatory_party_id", columnList = "municipality_id, signatory_party_id"),
		@Index(name = "idx_municipality_id_grantor_party_id", columnList = "municipality_id, grantor_party_id")
	})
public class MandateEntity {

	private static final long DEFAULT_VALIDITY_YEARS = 2;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", length = 36)
	private String id;

	@Column(name = "name", length = 160)    // Max length according to bolagsverket
	private String name;

	@Column(name = "grantor_party_id", nullable = false, length = 36)
	private String grantorPartyId;

	@Column(name = "signatory_party_id", nullable = false, length = 36)
	private String signatoryPartyId;

	@OneToMany(mappedBy = "mandate", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<GranteeEntity> grantees;

	@Column(name = "municipality_id", nullable = false, length = 4)
	private String municipalityId;

	@Column(name = "created", nullable = false)
	@TimeZoneStorage(NORMALIZE)
	private OffsetDateTime created;

	@Column(name = "updated", nullable = false)
	@TimeZoneStorage(NORMALIZE)
	private OffsetDateTime updated;

	@Column(name = "valid_from", nullable = false)
	@TimeZoneStorage(NORMALIZE)
	private OffsetDateTime validFrom;

	@Column(name = "valid_to", nullable = false)
	@TimeZoneStorage(NORMALIZE)
	private OffsetDateTime validTo;

	@Column(name = "status", nullable = false, length = 64)
	@Enumerated(EnumType.STRING)
	private MandateStatus status;

	@PrePersist
	protected void onCreate() {
		if (validFrom == null) {
			validFrom = OffsetDateTime.now(systemDefault()).truncatedTo(MILLIS);
		}
		if (validTo == null) {
			validTo = OffsetDateTime.now(systemDefault()).plusYears(DEFAULT_VALIDITY_YEARS).truncatedTo(MILLIS);
		}
		created = OffsetDateTime.now(systemDefault()).truncatedTo(MILLIS);
		updated = OffsetDateTime.now(systemDefault()).truncatedTo(MILLIS);
	}

	@PreUpdate
	protected void onUpdate() {
		updated = OffsetDateTime.now(systemDefault()).truncatedTo(MILLIS);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSignatoryPartyId() {
		return signatoryPartyId;
	}

	public void setSignatoryPartyId(String signatoryPartyId) {
		this.signatoryPartyId = signatoryPartyId;
	}

	public String getGrantorPartyId() {
		return grantorPartyId;
	}

	public void setGrantorPartyId(String grantorPartyId) {
		this.grantorPartyId = grantorPartyId;
	}

	public List<GranteeEntity> getGrantees() {
		return grantees;
	}

	public void setGrantees(List<GranteeEntity> grantees) {
		this.grantees = grantees;
	}

	public String getMunicipalityId() {
		return municipalityId;
	}

	public void setMunicipalityId(String municipalityId) {
		this.municipalityId = municipalityId;
	}

	public OffsetDateTime getCreated() {
		return created;
	}

	public void setCreated(OffsetDateTime created) {
		this.created = created;
	}

	public OffsetDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(OffsetDateTime updated) {
		this.updated = updated;
	}

	public OffsetDateTime getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(OffsetDateTime validFrom) {
		this.validFrom = validFrom;
	}

	public OffsetDateTime getValidTo() {
		return validTo;
	}

	public void setValidTo(OffsetDateTime validTo) {
		this.validTo = validTo;
	}

	public MandateStatus getStatus() {
		return status;
	}

	public void setStatus(MandateStatus status) {
		this.status = status;
	}

	public MandateEntity withId(String id) {
		this.id = id;
		return this;
	}

	public MandateEntity withName(String name) {
		this.name = name;
		return this;
	}

	public MandateEntity withSignatoryPartyId(String signatoryPartyId) {
		this.signatoryPartyId = signatoryPartyId;
		return this;
	}

	public MandateEntity withGrantorPartyId(String grantorPartyId) {
		this.grantorPartyId = grantorPartyId;
		return this;
	}

	public MandateEntity withGrantees(List<GranteeEntity> grantees) {
		this.grantees = grantees;
		return this;
	}

	public MandateEntity withMunicipalityId(String municipalityId) {
		this.municipalityId = municipalityId;
		return this;
	}

	public MandateEntity withCreated(OffsetDateTime created) {
		this.created = created;
		return this;
	}

	public MandateEntity withUpdated(OffsetDateTime updated) {
		this.updated = updated;
		return this;
	}

	public MandateEntity withValidFrom(OffsetDateTime validFrom) {
		this.validFrom = validFrom;
		return this;
	}

	public MandateEntity withValidTo(OffsetDateTime validTo) {
		this.validTo = validTo;
		return this;
	}

	public MandateEntity withStatus(MandateStatus status) {
		this.status = status;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof final MandateEntity that))
			return false;

		return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(signatoryPartyId, that.signatoryPartyId)
			&& Objects.equals(grantorPartyId, that.grantorPartyId) && Objects.equals(grantees, that.grantees)
			&& Objects.equals(municipalityId, that.municipalityId) && Objects.equals(created, that.created)
			&& Objects.equals(updated, that.updated) && Objects.equals(validFrom, that.validFrom)
			&& Objects.equals(validTo, that.validTo) && status == that.status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, signatoryPartyId, grantorPartyId, grantees, municipalityId, created, updated, validFrom, validTo, status);
	}

	@Override
	public String toString() {
		return "MandateEntity{" +
			"id='" + id + '\'' +
			", name='" + name + '\'' +
			", signatoryPartyId='" + signatoryPartyId + '\'' +
			", grantorPartyId='" + grantorPartyId + '\'' +
			", grantees=" + grantees +
			", municipalityId='" + municipalityId + '\'' +
			", created=" + created +
			", updated=" + updated +
			", validFrom=" + validFrom +
			", validTo=" + validTo +
			", status=" + status +
			'}';
	}
}
