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

	@Column(name = "order_ref", nullable = false, length = 36)
	private String orderRef;

	@Column(name = "status", nullable = false, length = 20)
	private String status;

	@Column(name = "personal_number", nullable = false, length = 12)
	private String personalNumber;

	@Column(name = "name")
	private String name;

	@Column(name = "given_name")
	private String givenName;

	@Column(name = "surname")
	private String surname;

	@Column(name = "ip_address", nullable = false, length = 45)
	private String ipAddress;

	@Column(name = "uhi", nullable = false, columnDefinition = "text")
	private String uhi;

	@Column(name = "bank_id_issue_date", nullable = false)
	private LocalDate bankIdIssueDate;

	@Column(name = "mrtd")
	private Boolean mrtd;

	@Column(name = "signature", nullable = false, columnDefinition = "longtext")
	private String signature;

	@Column(name = "ocsp_response", nullable = false, columnDefinition = "longtext")
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

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderRef() {
		return orderRef;
	}

	public void setOrderRef(String orderRef) {
		this.orderRef = orderRef;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPersonalNumber() {
		return personalNumber;
	}

	public void setPersonalNumber(String personalNumber) {
		this.personalNumber = personalNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUhi() {
		return uhi;
	}

	public void setUhi(String uhi) {
		this.uhi = uhi;
	}

	public LocalDate getBankIdIssueDate() {
		return bankIdIssueDate;
	}

	public void setBankIdIssueDate(LocalDate bankIdIssueDate) {
		this.bankIdIssueDate = bankIdIssueDate;
	}

	public Boolean getMrtd() {
		return mrtd;
	}

	public void setMrtd(Boolean mrtd) {
		this.mrtd = mrtd;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getOcspResponse() {
		return ocspResponse;
	}

	public void setOcspResponse(String ocspResponse) {
		this.ocspResponse = ocspResponse;
	}

	public String getRisk() {
		return risk;
	}

	public void setRisk(String risk) {
		this.risk = risk;
	}

	public MandateEntity getMandate() {
		return mandate;
	}

	public void setMandate(MandateEntity mandate) {
		this.mandate = mandate;
	}

	public OffsetDateTime getCreated() {
		return created;
	}

	public void setCreated(OffsetDateTime created) {
		this.created = created;
	}

	public SigningInformationEntity withId(String id) {
		this.id = id;
		return this;
	}

	public SigningInformationEntity withOrderRef(String orderRef) {
		this.orderRef = orderRef;
		return this;
	}

	public SigningInformationEntity withStatus(String status) {
		this.status = status;
		return this;
	}

	public SigningInformationEntity withPersonalNumber(String personalNumber) {
		this.personalNumber = personalNumber;
		return this;
	}

	public SigningInformationEntity withName(String name) {
		this.name = name;
		return this;
	}

	public SigningInformationEntity withGivenName(String givenName) {
		this.givenName = givenName;
		return this;
	}

	public SigningInformationEntity withSurname(String surname) {
		this.surname = surname;
		return this;
	}

	public SigningInformationEntity withIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
		return this;
	}

	public SigningInformationEntity withUhi(String uhi) {
		this.uhi = uhi;
		return this;
	}

	public SigningInformationEntity withBankIdIssueDate(LocalDate bankIdIssueDate) {
		this.bankIdIssueDate = bankIdIssueDate;
		return this;
	}

	public SigningInformationEntity withMrtdStepUp(Boolean mrtdStepUp) {
		this.mrtd = mrtdStepUp;
		return this;
	}

	public SigningInformationEntity withSignature(String signature) {
		this.signature = signature;
		return this;
	}

	public SigningInformationEntity withOcspResponse(String ocspResponse) {
		this.ocspResponse = ocspResponse;
		return this;
	}

	public SigningInformationEntity withRisk(String risk) {
		this.risk = risk;
		return this;
	}

	public SigningInformationEntity withMandate(MandateEntity mandate) {
		this.mandate = mandate;
		return this;
	}

	public SigningInformationEntity withCreated(OffsetDateTime created) {
		this.created = created;
		return this;
	}

	@Override
	public boolean equals(Object o) {
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
