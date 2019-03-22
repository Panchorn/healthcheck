package com.assignment.healthcheck.service;

import com.assignment.healthcheck.client.RestClient;
import com.assignment.healthcheck.model.Report;
import io.vavr.control.Try;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HealthCheckServiceTest {

    @InjectMocks
    private HealthCheckService healthCheckService;
    @Mock
    private RestClient restClient;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(healthCheckService, "healthCheckReportEndpoint", "mockendpoint");
    }

    @Test
    public void should_ReturnReportSuccessAll_when_CallApiSuccessAll() {
        Report expected = new Report(4, 4, 0, Long.MIN_VALUE);
        ResponseEntity<String> responseEntityOk = new ResponseEntity<>(HttpStatus.OK);

        when(restClient.get(anyString())).thenReturn(Try.success(responseEntityOk));
        when(restClient.post(anyString(), any(Report.class))).thenReturn(Try.success(responseEntityOk));

        Report response = healthCheckService.check(getFileMock());

        assertThat(response).isEqualToIgnoringGivenFields(expected, "total_time");
    }

    @Test
    public void should_ReturnReportFailAll_when_CallApiFailAll() {
        Report expected = new Report(4, 0, 4, Long.MIN_VALUE);
        ResponseEntity<String> responseEntityOk = new ResponseEntity<>(HttpStatus.OK);

        when(restClient.get(anyString())).thenReturn(Try.failure(new Exception()));
        when(restClient.post(anyString(), any(Report.class))).thenReturn(Try.success(responseEntityOk));

        Report response = healthCheckService.check(getFileMock());

        assertThat(response).isEqualToIgnoringGivenFields(expected, "total_time");
    }

    @Test
    public void should_ReturnReportSuccess2AndFail2_when_CallApiSuccess2AndFail2() {
        Report expected = new Report(4, 2, 2, Long.MIN_VALUE);
        ResponseEntity<String> responseEntityOk = new ResponseEntity<>(HttpStatus.OK);

        when(restClient.get("a")).thenReturn(Try.success(responseEntityOk));
        when(restClient.get("b")).thenReturn(Try.success(responseEntityOk));
        when(restClient.get("c")).thenReturn(Try.failure(new Exception()));
        when(restClient.get("d")).thenReturn(Try.failure(new Exception()));
        when(restClient.post(anyString(), any(Report.class))).thenReturn(Try.success(responseEntityOk));

        Report response = healthCheckService.check(getFileMock());

        assertThat(response).isEqualToIgnoringGivenFields(expected, "total_time");
    }

    @Test
    public void should_ReturnReportFail1BecauseHttpStatusIsTimeout_when_CallApiThenResponseTimeout() {
        Report expected = new Report(4, 3, 1, Long.MIN_VALUE);
        ResponseEntity<String> responseEntityOk = new ResponseEntity<>(HttpStatus.OK);
        ResponseEntity<String> responseEntityTimeout = new ResponseEntity<>(HttpStatus.REQUEST_TIMEOUT);

        when(restClient.get("a")).thenReturn(Try.success(responseEntityTimeout));
        when(restClient.get("b")).thenReturn(Try.success(responseEntityOk));
        when(restClient.get("c")).thenReturn(Try.success(responseEntityOk));
        when(restClient.get("d")).thenReturn(Try.success(responseEntityOk));
        when(restClient.post(anyString(), any(Report.class))).thenReturn(Try.success(responseEntityOk));

        Report response = healthCheckService.check(getFileMock());

        assertThat(response).isEqualToIgnoringGivenFields(expected, "total_time");
    }

    private MultipartFile getFileMock() {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.csv", "text/csv", "website\na\nb\nc\nd".getBytes());
        return mockMultipartFile;
    }

}