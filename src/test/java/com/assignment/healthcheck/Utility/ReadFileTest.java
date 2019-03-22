package com.assignment.healthcheck.Utility;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class ReadFileTest {

    @Test
    public void should_ReturnApiList_when_ReadFileSuccess() {
        List<String> expected = Lists.list("a", "b", "c", "d");

        List<String> response = ReadFile.convertMultipartFileToList(getFileCsvMock());

        assertThat(response).isEqualTo(expected);
    }

    @Test
    public void should_ReturnEmptyList_when_ReadFileBecauseTypeIsNotCsv() {
        List<String> response = ReadFile.convertMultipartFileToList(getFileXlsMock());

        assertThat(response).isEmpty();
    }

    private MultipartFile getFileCsvMock() {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.csv", "text/csv", "website\na\nb\nc\nd".getBytes());
        return mockMultipartFile;
    }

    private MultipartFile getFileXlsMock() {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.xls", "text/xls", "website\na\nb\nc\nd".getBytes());
        return mockMultipartFile;
    }

}