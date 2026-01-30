package pathways;

import constants.Constants;
import utilities.FileIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PathwayKegg {

    private final String pathwayId;
    private final String pathwayName;

    private final String entry;
    private final String description;
    private final String pathwayClass;
    private final List<String> modules;

    /**
     *
     * @param pathwayId
     * @param pathwayName
     */
    public PathwayKegg(String pathwayId, String pathwayName) {
        this(pathwayId, pathwayName, "", "", "", new ArrayList<>());
    }

    /**
     *
     * @param pathwayId
     * @param pathwayName
     * @param entry
     * @param description
     * @param pathwayClassList
     */
    public PathwayKegg(String pathwayId, String pathwayName, String entry, String description,
                       String pathwayClass, List<String> modules) {
        this.pathwayId = pathwayId;
        this.pathwayName = pathwayName;
        this.entry = entry;
        this.description = description;
        this.pathwayClass = pathwayClass;
        this.modules = modules;
    }

    public String getPathwayId() {
        return pathwayId;
    }

    public String getPathwayName() {
        return pathwayName;
    }

    public String getEntry() {
        return entry;
    }

    public String getDescription() {
        return description;
    }

    public String getPathwayClass() {
        return pathwayClass;
    }

    public List<String> getModules() {
        return modules;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PathwayKegg that = (PathwayKegg) o;
        return Objects.equals(pathwayId, that.pathwayId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pathwayId);
    }

    @Override
    public String toString() {
        return pathwayId + " - " + pathwayName;
    }

    public String getLink() {
        return Constants.KEGG_WEB_LINK + pathwayId;
    }



}