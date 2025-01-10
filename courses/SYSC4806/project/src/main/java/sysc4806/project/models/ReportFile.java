package sysc4806.project.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;


@Entity
public class ReportFile {

    private String fileName;

    @Lob
    private byte[] fileData;

    @Id
    @GeneratedValue
    private Long id;

    /**
     * Constructor for ReportFile
     * @param fileName String filename
     * @param fileData byte[] data of file
     */
    public ReportFile(String fileName, byte[] fileData) {
        this.fileName = fileName;
        this.fileData = fileData;
    }

    /**
     * Default constructor
     */
    public ReportFile() {}

    /**
     * Gets the filename
     * @return String file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the filename
     * @param fileName String filename to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Gets the filedata
     * @return byte[] the file data
     */
    public byte[] getFileData() {
        return fileData;
    }

    /**
     * Sets the filedata
     * @param fileData byte[] filedata to set
     */
    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    /**
     * Sets the id
     * @param id Long id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the id
     * @return Long the id
     */
    public Long getId() {
        return id;
    }
}
