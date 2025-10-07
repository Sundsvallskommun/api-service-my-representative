package se.sundsvall.myrepresentative.integration.db.entity;

import static java.time.ZoneId.systemDefault;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.hibernate.annotations.TimeZoneStorageType.NORMALIZE;
import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.TimeZoneStorage;
import org.zalando.problem.Problem;

@Entity
@Table(name = "mandate",
	indexes = {
		@Index(name = "idx_municipality_id_signatory_party_id", columnList = "municipality_id, signatory_party_id"),
		@Index(name = "idx_municipality_id_grantor_party_id", columnList = "municipality_id, grantor_party_id"),
		@Index(name = "idx_municipality_id_grantee", columnList = "municipality_id, grantee_party_id"),
		@Index(name = "idx_municipality_id_namespace", columnList = "municipality_id, namespace")
	})
public class MandateEntity {

	// If no value is provided for inactiveAfter, we set it to activeFrom + DEFAULT_VALID_YEARS (3 years)
	private static final int DEFAULT_VALID_YEARS = 3;

	// Will give date/time 1970-01-01T00:00Z which will be used as a "not deleted" flag in the 'deleted' column.
	// Used when creating a new mandate (in @PrePersist) and when checking if a mandate is deleted or not
	// Will be the same as '1970-01-01 01:00:00.000000'
	public static final OffsetDateTime NOT_DELETED = OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);

	// Concatenating the sql to get rid of "non-escaped" characters warning when using text-block.
	private static final String STATUS_FORMULA = "case " +
		"when deleted != '1970-01-01 01:00:00.000000' then 'DELETED' " +
		"when active_from > curdate() then 'INACTIVE' " +
		"when inactive_after < curdate() then 'EXPIRED' " +
		"else 'ACTIVE' " +
		"end";

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

	@Column(name = "grantee_party_id", nullable = false, length = 36)
	private String granteePartyId;

	@Column(name = "namespace", nullable = false, length = 128)
	private String namespace;

	@Column(name = "municipality_id", nullable = false, length = 4)
	private String municipalityId;

	@TimeZoneStorage(NORMALIZE)
	@Column(name = "created", nullable = false)
	private OffsetDateTime created;

	@TimeZoneStorage(NORMALIZE)
	@Column(name = "updated", nullable = false)
	private OffsetDateTime updated;

	@Column(name = "active_from", nullable = false)
	private LocalDate activeFrom;

	@Column(name = "inactive_after", nullable = false)
	private LocalDate inactiveAfter;

	@TimeZoneStorage(NORMALIZE)
	@Column(name = "deleted")
	private OffsetDateTime deleted;

	@Formula(STATUS_FORMULA)
	private String status;

	// A mandate may have many signing information entries (one for each time it has been signed)
	@OneToMany(mappedBy = "mandate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private final List<SigningInformationEntity> signingInformation = new ArrayList<>();

	@PrePersist
	protected void onCreate() {
		created = OffsetDateTime.now(systemDefault()).truncatedTo(MILLIS);
		updated = OffsetDateTime.now(systemDefault()).truncatedTo(MILLIS);
		deleted = NOT_DELETED;

		if (inactiveAfter == null) {
			if (activeFrom != null) {
				inactiveAfter = activeFrom.plusYears(DEFAULT_VALID_YEARS);
			} else {
				// This should never occur as we have validation for this in the API layer, but just to be sure.
				throw Problem.builder()
					.withTitle("Cannot set validity period for mandate")
					.withStatus(INTERNAL_SERVER_ERROR)
					.withDetail("activeFrom and/or incactiveAfter is null, cannot set validity period")
					.build();
			}
		}
	}

	@PreUpdate
	protected void onUpdate() {
		updated = OffsetDateTime.now(systemDefault()).truncatedTo(MILLIS);
	}

	// Convenience method to get the current signing information (the one with the latest 'signed' timestamp)
	public SigningInformationEntity getLatestSigningInformation() {
		return signingInformation.stream()
			.max(Comparator.comparing(SigningInformationEntity::getSigned))
			.orElse(null);
	}

	public MandateEntity addSigningInformation(SigningInformationEntity entity) {
		entity.setMandate(this);
		this.signingInformation.add(entity);
		return this;
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

	public String getGrantorPartyId() {
		return grantorPartyId;
	}

	public void setGrantorPartyId(String grantorPartyId) {
		this.grantorPartyId = grantorPartyId;
	}

	public String getSignatoryPartyId() {
		return signatoryPartyId;
	}

	public void setSignatoryPartyId(String signatoryPartyId) {
		this.signatoryPartyId = signatoryPartyId;
	}

	public String getGranteePartyId() {
		return granteePartyId;
	}

	public void setGranteePartyId(String granteePartyId) {
		this.granteePartyId = granteePartyId;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
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

	public LocalDate getActiveFrom() {
		return activeFrom;
	}

	public void setActiveFrom(LocalDate activeFrom) {
		this.activeFrom = activeFrom;
	}

	public LocalDate getInactiveAfter() {
		return inactiveAfter;
	}

	public void setInactiveAfter(LocalDate inactiveAfter) {
		this.inactiveAfter = inactiveAfter;
	}

	public OffsetDateTime getDeleted() {
		return deleted;
	}

	public void setDeleted(OffsetDateTime deleted) {
		this.deleted = deleted;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Returns an unmodifiable list of signing information entities associated with this mandate.
	 * Use addSigningInformation() to add new entries
	 * 
	 * @return an unmodifiable list of signing information entities
	 */
	public List<SigningInformationEntity> getSigningInformation() {
		return Collections.unmodifiableList(signingInformation);
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

	public MandateEntity withGrantee(String grantee) {
		this.granteePartyId = grantee;
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

	public MandateEntity withActiveFrom(LocalDate activeFrom) {
		this.activeFrom = activeFrom;
		return this;
	}

	public MandateEntity withInactiveAfter(LocalDate inactiveAfter) {
		this.inactiveAfter = inactiveAfter;
		return this;
	}

	public MandateEntity withDeleted(OffsetDateTime deleted) {
		this.deleted = deleted;
		return this;
	}

	public MandateEntity withNamespace(String namespace) {
		this.namespace = namespace;
		return this;
	}

	public MandateEntity withStatus(String status) {
		this.status = status;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof final MandateEntity that))
			return false;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "MandateEntity{" +
			"id='" + id + '\'' +
			", name='" + name + '\'' +
			", grantorPartyId='" + grantorPartyId + '\'' +
			", signatoryPartyId='" + signatoryPartyId + '\'' +
			", granteePartyId='" + granteePartyId + '\'' +
			", namespace='" + namespace + '\'' +
			", municipalityId='" + municipalityId + '\'' +
			", created=" + created +
			", updated=" + updated +
			", activeFrom=" + activeFrom +
			", inactiveAfter=" + inactiveAfter +
			", deleted='" + deleted + '\'' +
			", status='" + status + '\'' +
			'}';
	}
}
