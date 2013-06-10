package jp.scid.bio.store.remote;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import jp.scid.bio.store.remote.RemoteSource.RemoteEntry;

import org.junit.Before;
import org.junit.Test;

public class RemoteSourceTest {
    RemoteSource model;
    
    @Before
    public void setUp() throws Exception {
        model = new RemoteSource();
    }

    @Test
    public void retrieveCount() throws IOException {
        assertTrue(model.retrieveCount("lung cancer") >= 91600);
        assertEquals(0, model.retrieveCount("invalid query"));
    }

    @Test
    public void searchIdentifiers() throws IOException {
        List<String> result1 = model.searchIdentifiers("lung cancer", 1, 4);
        assertEquals(4, result1.size());
    }
    
    @Test
    public void searchEntry() throws IOException {
        List<RemoteEntry> result = model.searchEntry("J00231", "510145809");
        
        assertEquals(2, result.size());
        
        RemoteEntry j00231 = result.get(0);
        assertEquals("J00231", j00231.identifier());
        assertEquals("J00231", j00231.accession());
        assertEquals("Human Ig gamma3 heavy chain disease OMM protein mRNA.", j00231.definition());
        assertEquals(1089, j00231.sequenceLength());
        assertEquals("Eukaryota; Metazoa; Chordata; Craniata; Vertebrata; Euteleostomi;"
            + " Mammalia; Eutheria; Euarchontoglires; Primates; Haplorrhini;"
            + " Catarrhini; Hominidae; Homo.", j00231.taxonomy());
        
        RemoteEntry E510145809 = result.get(1);
        assertEquals("510145809", E510145809.identifier());
        assertEquals("HJ110587", E510145809.accession());
        assertEquals("Sequence 23 from patent US 8450283.", E510145809.definition());
        assertEquals(21, E510145809.sequenceLength());
        assertEquals("Unknown. Unclassified.", E510145809.taxonomy());
    }
}
