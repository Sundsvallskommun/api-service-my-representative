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
					    "access_token": "0000000000000000AAAAAAAAAAAAAAAA",
					    "expires_in": -1,
					    "refresh_token": "1111111111111111BBBBBBBBBBBBBBBB",
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
					    "access_token": "2222222222222222CCCCCCCCCCCCCCCC",
					    "expires_in": -1,
					    "refresh_token": "3333333333333333DDDDDDDDDDDDDDDD",
					    "scope": "create",
					    "token_type": "bearer"
					}
					""")));
	}
}
