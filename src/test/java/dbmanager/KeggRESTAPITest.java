package dbmanager;

import exceptions.KeggCompoundNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import pathways.PathwayKegg;

import org.mockito.Mockito;
import utilities.FileIO;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;


class KeggRESTAPITest {


    @Test
    void testGetPathwaysFromCompound_ParsesPathwaysCorrectly() throws Exception {

        // Fake KEGG response (as if we called: https://rest.kegg.jp/get/C00026)
        List<String> fakeKeggResponse = Arrays.asList(
                "ENTRY       C00026                      Compound",
                "NAME        2-Oxoglutarate",
                "FORMULA     C5H4O5",
                "PATHWAY     map00020  Citrate cycle (TCA cycle)",
                "            map00220  Arginine biosynthesis",
                "            map00400  Phenylalanine, tyrosine and tryptophan biosynthesis",
                "DBLINKS     CAS: 328-50-7",
                "///"
        );

        try (MockedStatic<FileIO> mock = mockStatic(FileIO.class)) {

            mock.when(() -> FileIO.readUrl(anyString()))
                    .thenReturn(fakeKeggResponse);

            List<PathwayKegg> result =
                    null;
            try {
                result = KeggRESTAPI.getPathwaysFromCompound("C00026");


                Assertions.assertNotNull(result);
                Assertions.assertEquals(3, result.size());

                Assertions.assertEquals("map00020", result.get(0).getPathwayId());
                Assertions.assertEquals("Citrate cycle (TCA cycle)", result.get(0).getPathwayName());

                Assertions.assertEquals("map00220", result.get(1).getPathwayId());
                Assertions.assertEquals("Arginine biosynthesis", result.get(1).getPathwayName());

                Assertions.assertEquals("map00400", result.get(2).getPathwayId());
                Assertions.assertEquals("Phenylalanine, tyrosine and tryptophan biosynthesis", result.get(2).getPathwayName());
            } catch (KeggCompoundNotFoundException e) {
                fail("Exception should not be thrown");
            }
        }
    }

    @Test
    void testGetPathwaysFromCompound_IOExceptionThrowsException() throws Exception {

        try (MockedStatic<FileIO> mock = mockStatic(FileIO.class)) {

            mock.when(() -> FileIO.readUrl(anyString()))
                    .thenThrow(new IOException("Network error"));

            assertThrows(
                    KeggCompoundNotFoundException.class,
                    () -> KeggRESTAPI.getPathwaysFromCompound("C00026")
            );
        }
    }


}