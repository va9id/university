package sysc4806.project.models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestReportFile {
    private static ReportFile reportFile;
    static String FILENAME = "FILENAME";
    static byte[] CONTENT = "FILE_CONTENT".getBytes();

    @BeforeEach
    public void setUp() {
        reportFile = new ReportFile(FILENAME, CONTENT);
    }

    @Test
    public void testGetFileName() {
        assertEquals(reportFile.getFileName(), FILENAME);
    }

    @Test
    public void testSetFileName() {
        reportFile.setFileName("other filename");
        assertEquals(reportFile.getFileName(), "other filename");
    }

    @Test
    public void testGetFileData() {
        assertArrayEquals(reportFile.getFileData(), CONTENT);
    }

    @Test
    public void testSetFileData() {
        byte[] newContent = "New content".getBytes();
        reportFile.setFileData(newContent);
        assertArrayEquals(reportFile.getFileData(), newContent);
    }

    @Test
    public void testSetId() {
        reportFile.setId(100L);
        assertEquals(reportFile.getId(), 100L);
    }
}
