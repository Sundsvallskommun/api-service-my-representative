package se.sundsvall.myrepresentative.integration.db.entity;

import static org.hibernate.annotations.TimeZoneStorageType.NORMALIZE;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import org.hibernate.annotations.TimeZoneStorage;

/**
 * Entity for storing BankID signature details when operating on a mandate.
 */
@Entity
@Table(name = "signing_information")
public class SigningInformationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", length = 36)
	private String id;

	@Column(name = "external_transaction_id", nullable = false, length = 36)
	private String externalTransactionId;

	@Column(name = "order_ref", nullable = false, length = 36)
	private String orderRef;

	@Column(name = "status", nullable = false, length = 20)
	private String status;

	@Column(name = "personal_number", nullable = false, length = 12)
	private String personalNumber;

	@Column(name = "name")
	private String name;

	@Column(name = "given_name", nullable = false)
	private String givenName;

	@Column(name = "surname", nullable = false)
	private String surname;

	@Column(name = "ip_address", nullable = false, length = 45)
	private String ipAddress;

	@Column(name = "uhi", columnDefinition = "text")
	private String uhi;

	@Column(name = "bank_id_issue_date")
	private LocalDate bankIdIssueDate;

	@Column(name = "mrtd")
	private Boolean mrtd;

	@Column(name = "signature", nullable = false, columnDefinition = "longtext")
	private String signature;

	@Column(name = "ocsp_response", columnDefinition = "longtext")
	private String ocspResponse;

	@Column(name = "risk", length = 20)
	private String risk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mandate_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_signing_information_mandate"))
	private MandateEntity mandate;

	@Column(name = "created", nullable = false)
	@TimeZoneStorage(NORMALIZE)
	private OffsetDateTime created;

	@PrePersist
	void prePersist() {
		created = OffsetDateTime.now();
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getExternalTransactionId() {
		return externalTransactionId;
	}

	public void setExternalTransactionId(final String externalTransactionId) {
		this.externalTransactionId = externalTransactionId;
	}

	public String getOrderRef() {
		return orderRef;
	}

	public void setOrderRef(final String orderRef) {
		this.orderRef = orderRef;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	public String getPersonalNumber() {
		return personalNumber;
	}

	public void setPersonalNumber(final String personalNumber) {
		this.personalNumber = personalNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(final String givenName) {
		this.givenName = givenName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(final String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUhi() {
		return uhi;
	}

	public void setUhi(final String uhi) {
		this.uhi = uhi;
	}

	public LocalDate getBankIdIssueDate() {
		return bankIdIssueDate;
	}

	public void setBankIdIssueDate(final LocalDate bankIdIssueDate) {
		this.bankIdIssueDate = bankIdIssueDate;
	}

	public Boolean getMrtd() {
		return mrtd;
	}

	public void setMrtd(final Boolean mrtd) {
		this.mrtd = mrtd;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(final String signature) {
		this.signature = signature;
	}

	public String getOcspResponse() {
		return ocspResponse;
	}

	public void setOcspResponse(final String ocspResponse) {
		this.ocspResponse = ocspResponse;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(final String risk) {
		this.risk = risk;
	}

	public MandateEntity getMandate() {
		return mandate;
	}

	public void setMandate(final MandateEntity mandate) {
		this.mandate = mandate;
	}

	public OffsetDateTime getCreated() {
		return created;
	}

	public void setCreated(final OffsetDateTime created) {
		this.created = created;
	}

	public SigningInformationEntity withId(final String id) {
		this.id = id;
		return this;
	}

	public SigningInformationEntity withExternalTransactionId(final String externalTransactionId) {
		this.externalTransactionId = externalTransactionId;
		return this;
	}

	public SigningInformationEntity withOrderRef(final String orderRef) {
		this.orderRef = orderRef;
		return this;
	}

	public SigningInformationEntity withStatus(final String status) {
		this.status = status;
		return this;
	}

	public SigningInformationEntity withPersonalNumber(final String personalNumber) {
		this.personalNumber = personalNumber;
		return this;
	}

	public SigningInformationEntity withName(final String name) {
		this.name = name;
		return this;
	}

	public SigningInformationEntity withGivenName(final String givenName) {
		this.givenName = givenName;
		return this;
	}

	public SigningInformationEntity withSurname(final String surname) {
		this.surname = surname;
		return this;
	}

	public SigningInformationEntity withIpAddress(final String ipAddress) {
		this.ipAddress = ipAddress;
		return this;
	}

	public SigningInformationEntity withUhi(final String uhi) {
		this.uhi = uhi;
		return this;
	}

	public SigningInformationEntity withBankIdIssueDate(final LocalDate bankIdIssueDate) {
		this.bankIdIssueDate = bankIdIssueDate;
		return this;
	}

	public SigningInformationEntity withMrtdStepUp(final Boolean mrtdStepUp) {
		this.mrtd = mrtdStepUp;
		return this;
	}

	public SigningInformationEntity withSignature(final String signature) {
		this.signature = signature;
		return this;
	}

	public SigningInformationEntity withOcspResponse(final String ocspResponse) {
		this.ocspResponse = ocspResponse;
		return this;
	}

	public SigningInformationEntity withRisk(final String risk) {
		this.risk = risk;
		return this;
	}

	public SigningInformationEntity withMandate(final MandateEntity mandate) {
		this.mandate = mandate;
		return this;
	}

	public SigningInformationEntity withCreated(final OffsetDateTime created) {
		this.created = created;
		return this;
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof final SigningInformationEntity that))
			return false;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "SigningInformationEntity{" +
			"id='" + id + '\'' +
			", externalTransactionId='" + externalTransactionId + '\'' +
			", orderRef='" + orderRef + '\'' +
			", status='" + status + '\'' +
			", personalNumber='" + personalNumber + '\'' +
			", name='" + name + '\'' +
			", givenName='" + givenName + '\'' +
			", surname='" + surname + '\'' +
			", ipAddress='" + ipAddress + '\'' +
			", uhi='" + uhi + '\'' +
			", bankIdIssueDate=" + bankIdIssueDate +
			", mrtd=" + mrtd +
			", risk='" + risk + '\'' +
			", mandate=" + mandate +
			", created=" + created +
			'}';
	}
}
