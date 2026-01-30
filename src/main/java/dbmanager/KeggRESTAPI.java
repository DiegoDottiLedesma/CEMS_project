package dbmanager;

import constants.Constants;
import exceptions.KeggCompoundNotFoundException;
import pathways.PathwayKegg;
import utilities.FileIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static utilities.FileIO.readUrl;

public class KeggRESTAPI {

    /**
     * Method to get the pathways in which a KEGG compound is involved
     * @param keggCompoundId
     * @return
     * @throws KeggCompoundNotFoundException
     */
    public static List<PathwayKegg> getPathwaysFromCompound(String keggCompoundId) throws KeggCompoundNotFoundException {
        List<PathwayKegg> pathways = new ArrayList<>();

        String url = Constants.KEGG_ENDPOINT_GET + keggCompoundId;
        try {
            List<String> lines = FileIO.readUrl(url);

            boolean inPathway = false;

            for (String line : lines) {
                line = line.trim();
                if (line.startsWith("PATHWAY")) {
                    inPathway = true;
                    line = line.replace("PATHWAY", "").trim();
                    String[] parts = line.trim().split("\\s+", 2);
                    if (parts.length == 2) {
                        String pathwayId = parts[0].trim();
                        String pathwayName = parts[1].trim();
                        PathwayKegg pw = new PathwayKegg(pathwayId, pathwayName);
                        pathways.add(pw);
                    }

                } else if (inPathway && line.startsWith("map")) {
                    String[] parts = line.trim().split("\\s+", 2);
                    if (parts.length == 2) {
                        String pathwayId = parts[0].trim();
                        String pathwayName = parts[1].trim();
                        PathwayKegg pw = new PathwayKegg(pathwayId, pathwayName);
                        pathways.add(pw);
                    }
                }

            }

            return pathways;
        } catch (IOException e) {
            e.printStackTrace();
            throw new KeggCompoundNotFoundException("Error retrieving KEGG data for compound: " + keggCompoundId);
        }
    }


    // @TODO Implement the method getFullPathwaysFromCompound

}
