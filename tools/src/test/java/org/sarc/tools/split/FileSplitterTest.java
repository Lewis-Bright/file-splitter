package org.sarc.tools.split;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;

public class FileSplitterTest {

    private static final String FILE_CONTENTS = "line1\nline2\nline3\nline4\nline5\nline6";

    private static final String SPLIT_FILES_SUBFOLDER = "split-files";

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private File file;

    @Before
    public void setup() throws IOException {
        file = tempFolder.newFile();
        FileUtils.writeStringToFile(file, FILE_CONTENTS, Charset.defaultCharset());
    }

    @Test
    public void testSplitFiles() {
        FileSplitter.main(file.getAbsolutePath(), "2");
        File splitFilesSubfolder = new File(file.getAbsolutePath() + File.separator + SPLIT_FILES_SUBFOLDER);
        assertThat(splitFilesSubfolder)
                .as("File should be split into txt files with same prefix")
                .isDirectoryContaining(file -> file.getName().startsWith("splitFile")
                        && FilenameUtils.getExtension(file.getName()).equals("txt"));
        File[] splitFiles = splitFilesSubfolder.listFiles();
        assertThat(splitFiles).as("Split files should exist").isNotNull();
        assertThat(splitFiles)
                .as("Splitting 6 lines into files with 2 lines results in 3 files")
                .hasSize(3);

        assertThat(splitFiles[0]).as("First file should contain first 2 lines").hasContent("line1\nline2");
        assertThat(splitFiles[1]).as("Second file should contain next 2 lines").hasContent("line3\nline4");
        assertThat(splitFiles[2]).as("Third file should contain final 2 lines").hasContent("line5\nline6");

    }
}
