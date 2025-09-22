package se.sundsvall.myrepresentative.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.myrepresentative.service.RepresentativesService;

@ExtendWith(MockitoExtension.class)
class MandatesResourceTest {

	@Mock
	private RepresentativesService mockService;

	@InjectMocks
	private MandatesResource mandatesResource;

	@Test
	void getMandates() {

	}
}
