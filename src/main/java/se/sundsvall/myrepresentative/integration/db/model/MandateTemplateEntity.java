package se.sundsvall.myrepresentative.integration.db.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder(setterPrefix = "with")
@Table(name = "mandate_template")
@NoArgsConstructor
@AllArgsConstructor
public class MandateTemplateEntity {

	@Id
	private String code;

	@Column
	private String title;

	@Column(length = 2000)
	private String description;

	@Column
	private String municipalityId;

}
