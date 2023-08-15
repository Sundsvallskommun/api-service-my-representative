package apptest;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class CommonStubs {

	public static void stubAllTokens() {
		stubOmbudToken();
		stubPartyToken();
	}

	public static void stubPartyToken() {
		stubFor(post("/partytoken")
			.willReturn(aResponse()
				.withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.withBody("""
					{
					    "access_token": "MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI3",
					    "expires_in": -1,
					    "refresh_token": "IwOGYzYTlmM2YxOTQ5MGE3YmNmMDFkNTVk",
					    "scope": "create",
					    "token_type": "bearer"
					}
					""")));
	}

	public static void stubOmbudToken() {
		stubFor(post("/ombudtoken")
			.willReturn(aResponse()
				.withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.withBody("""
					{
					    "access_token": "MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI4",
					    "expires_in": -1,
					    "refresh_token": "IwOGYzYTlmM2YxOTQ5MGE3YmNmMDFkNTVl",
					    "scope": "create",
					    "token_type": "bearer"
					}
					""")));
	}
}
