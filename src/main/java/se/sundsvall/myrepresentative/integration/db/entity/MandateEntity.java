package se.sundsvall.myrepresentative.integration.db.entity;

import static java.time.ZoneId.systemDefault;
import static java.time.temporal.ChronoUnit.MILLIS;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import org.hibernate.annotations.Formula;

@Entity
@Table(name = "mandate",
	indexes = {
		@Index(name = "idx_municipality_id_signatory_party_id", columnList = "municipality_id, signatory_party_id"),
		@Index(name = "idx_municipality_id_grantor_party_id", columnList = "municipality_id, grantor_party_id"),
		@Index(name = "idx_municipality_id_grantee", columnList = "municipality_id, grantee_party_id"),
		@Index(name = "idx_municipality_id_namespace", columnList = "municipality_id, namespace")
	})
public class MandateEntity {

	// Concatenating the sql to get rid of "non-escaped" characters warning.
	private static final String STATUS_FORMULA = "case " +
		"when deleted != 'false' then 'DELETED' " +
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

	@Column(name = "created", nullable = false)
	private LocalDateTime created;

	@Column(name = "updated", nullable = false)
	private LocalDateTime updated;

	@Column(name = "active_from", nullable = false)
	private LocalDate activeFrom;

	@Column(name = "inactive_after", nullable = false)
	private LocalDate inactiveAfter;

	// "false" or timestamp of deletion
	// When we soft delete the mandate we set this to a timestamp to be able to keep them unique.
	@Column(name = "deleted", nullable = false, length = 36)
	private String deleted = "false";

	@Formula(STATUS_FORMULA)
	private String status;

	@PrePersist
	protected void onCreate() {
		created = LocalDateTime.now(systemDefault()).truncatedTo(MILLIS);
		updated = LocalDateTime.now(systemDefault()).truncatedTo(MILLIS);
	}

	@PreUpdate
	protected void onUpdate() {
		updated = LocalDateTime.now(systemDefault()).truncatedTo(MILLIS);
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

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
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

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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

	public MandateEntity withGrantee(String grantee) {
		this.granteePartyId = grantee;
		return this;
	}

	public MandateEntity withMunicipalityId(String municipalityId) {
		this.municipalityId = municipalityId;
		return this;
	}

	public MandateEntity withCreated(LocalDateTime created) {
		this.created = created;
		return this;
	}

	public MandateEntity withUpdated(LocalDateTime updated) {
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

	public MandateEntity withDeleted(String deleted) {
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
		return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(grantorPartyId, that.grantorPartyId) && Objects.equals(signatoryPartyId, that.signatoryPartyId) && Objects.equals(granteePartyId, that.granteePartyId)
			&& Objects.equals(namespace, that.namespace) && Objects.equals(municipalityId, that.municipalityId) && Objects.equals(created, that.created) && Objects.equals(updated, that.updated) && Objects.equals(activeFrom, that.activeFrom) && Objects
				.equals(inactiveAfter, that.inactiveAfter) && Objects.equals(deleted, that.deleted) && Objects.equals(status, that.status);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, grantorPartyId, signatoryPartyId, granteePartyId, namespace, municipalityId, created, updated, activeFrom, inactiveAfter, deleted, status);
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
