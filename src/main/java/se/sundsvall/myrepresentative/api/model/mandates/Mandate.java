package se.sundsvall.myrepresentative.api.model.mandates;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import se.sundsvall.myrepresentative.api.model.ResponseAcquirer;
import se.sundsvall.myrepresentative.api.model.ResponseIssuer;
import se.sundsvall.myrepresentative.api.model.Role;
import se.sundsvall.myrepresentative.api.model.MandateStatus;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(setterPrefix = "with")
@Schema(description = "Mandate information model.")
public class Mandate {

    private ResponseIssuer mandateIssuer;

    @ArraySchema(schema = @Schema(implementation = ResponseAcquirer.class))
    private List<ResponseAcquirer> mandateAcquirers;

    @Schema(description = "If the issuer is an organization or private person", example = "ORGANIZATION")
    private Role mandateRole;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(description = "Date when the mandate was issued")
    private LocalDateTime issuedDate;

    @ArraySchema(schema = @Schema(implementation = Permission.class))
    private List<Permission> permissions;

    @Getter
    @Setter
    @Builder(setterPrefix = "with")
    public static class Permission {

        @Schema(description = "Code for the specific permission", example = "bf1a690b-33d6-4a3e-b407-e7346fa1c97c")
        private String code;

        @Schema(description = "If the current permission is active or passive", example = "Aktiv", allowableValues = {"Aktiv", "Passiv"})
        private MandateStatus mandateStatus;

        @Schema(description = "UUID for the mandate", example = "95189b70-c0cc-432f-a1ef-bb75b876ab75")
        private String mandate;
    }
}
