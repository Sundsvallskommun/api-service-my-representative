package se.sundsvall.myrepresentative.integration.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Entity for storing BankID signature details when operating on a mandate.
 */
@Entity
@Table(name = "bankid_signature")
public class BankIdSignatureEntity {

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

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "given_name", nullable = false)
	private String givenName;

	@Column(name = "surname", nullable = false)
	private String surname;

	@Column(name = "ip_address", nullable = false, length = 45)
	private String ipAddress;

	@Column(name = "uhi", nullable = false, columnDefinition = "TEXT")
	private String uhi;

	@Column(name = "bank_id_issue_date", nullable = false)
	private LocalDate bankIdIssueDate;

	@Column(name = "mrtd_step_up")
	private Boolean mrtdStepUp = false;

	@Column(name = "signature_data", nullable = false, columnDefinition = "LONGTEXT")
	private String signatureData;

	@Column(name = "ocsp_response", nullable = false, columnDefinition = "LONGTEXT")
	private String ocspResponse;

	@Column(name = "risk", nullable = false, length = 20)
	private String risk;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mandate_id", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_bankid_signature_mandate"))
	private MandateEntity mandate;

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

	public Boolean getMrtdStepUp() {
		return mrtdStepUp;
	}

	public void setMrtdStepUp(Boolean mrtdStepUp) {
		this.mrtdStepUp = mrtdStepUp;
	}

	public String getSignatureData() {
		return signatureData;
	}

	public void setSignatureData(String signatureData) {
		this.signatureData = signatureData;
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

	public BankIdSignatureEntity withId(String id) {
		this.id = id;
		return this;
	}

	public BankIdSignatureEntity withOrderRef(String orderRef) {
		this.orderRef = orderRef;
		return this;
	}

	public BankIdSignatureEntity withStatus(String status) {
		this.status = status;
		return this;
	}

	public BankIdSignatureEntity withPersonalNumber(String personalNumber) {
		this.personalNumber = personalNumber;
		return this;
	}

	public BankIdSignatureEntity withName(String name) {
		this.name = name;
		return this;
	}

	public BankIdSignatureEntity withGivenName(String givenName) {
		this.givenName = givenName;
		return this;
	}

	public BankIdSignatureEntity withSurname(String surname) {
		this.surname = surname;
		return this;
	}

	public BankIdSignatureEntity withIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
		return this;
	}

	public BankIdSignatureEntity withUhi(String uhi) {
		this.uhi = uhi;
		return this;
	}

	public BankIdSignatureEntity withBankIdIssueDate(LocalDate bankIdIssueDate) {
		this.bankIdIssueDate = bankIdIssueDate;
		return this;
	}

	public BankIdSignatureEntity withMrtdStepUp(Boolean mrtdStepUp) {
		this.mrtdStepUp = mrtdStepUp;
		return this;
	}

	public BankIdSignatureEntity withSignatureData(String signatureData) {
		this.signatureData = signatureData;
		return this;
	}

	public BankIdSignatureEntity withOcspResponse(String ocspResponse) {
		this.ocspResponse = ocspResponse;
		return this;
	}

	public BankIdSignatureEntity withRisk(String risk) {
		this.risk = risk;
		return this;
	}

	public BankIdSignatureEntity withMandate(MandateEntity mandate) {
		this.mandate = mandate;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof final BankIdSignatureEntity that))
			return false;
		return Objects.equals(id, that.id) && Objects.equals(orderRef, that.orderRef) && Objects.equals(status, that.status) && Objects.equals(personalNumber, that.personalNumber) && Objects.equals(name, that.name) && Objects.equals(givenName,
			that.givenName) && Objects.equals(surname, that.surname) && Objects.equals(ipAddress, that.ipAddress) && Objects.equals(uhi, that.uhi) && Objects.equals(bankIdIssueDate, that.bankIdIssueDate) && Objects.equals(mrtdStepUp, that.mrtdStepUp)
			&& Objects.equals(signatureData, that.signatureData) && Objects.equals(ocspResponse, that.ocspResponse) && Objects.equals(risk, that.risk) && Objects.equals(mandate, that.mandate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, orderRef, status, personalNumber, name, givenName, surname, ipAddress, uhi, bankIdIssueDate, mrtdStepUp, signatureData, ocspResponse, risk, mandate);
	}

	@Override
	public String toString() {
		return "BankIdSignatureEntity{" +
			"id=" + id +
			", orderRef='" + orderRef + '\'' +
			", status='" + status + '\'' +
			", personalNumber='" + personalNumber + '\'' +
			", name='" + name + '\'' +
			", givenName='" + givenName + '\'' +
			", surname='" + surname + '\'' +
			", ipAddress='" + ipAddress + '\'' +
			", uhi='" + uhi + '\'' +
			", bankIdIssueDate=" + bankIdIssueDate +
			", mrtdStepUp=" + mrtdStepUp +
			", risk='" + risk + '\'' +
			", mandate=" + mandate +
			'}';
	}
}
