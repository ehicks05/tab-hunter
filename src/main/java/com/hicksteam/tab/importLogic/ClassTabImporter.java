package com.hicksteam.tab.importLogic;

import com.hicksteam.tab.TabLogic;
import com.hicksteam.tab.TabsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.*;

@Configuration
public class ClassTabImporter
{
    private static final Logger log = LoggerFactory.getLogger(TabsController.class);
    private TabLogic tabLogic;
    private boolean importHasRun = false;

    public ClassTabImporter(TabLogic tabLogic)
    {
        this.tabLogic = tabLogic;
    }

    public void doImport() throws IOException
    {
        if (importHasRun)
        {
            log.info("Not running ClassTabImporter.doImport(). Import has already been run.");
            return;
        }
        log.info("Starting ClassTabImporter.doImport().");

        // parse index.htm and identify all the links to tabs that will be imported
        Map<String, List<List<String>>> artistToTabs = getArtistToTabMap(getIndexLines()); // artist to list of list(name, href)

        // open a filesystem of the zip file
        Map<String, Object> env = new HashMap<>();
        env.put("create", false);
        URI zip_disk = URI.create("jar:" + getZipFilePath().toUri().toString());
        try (FileSystem fs = FileSystems.newFileSystem(zip_disk, env))
        {
            artistToTabs.forEach((artist, tabs) -> tabs.forEach(tab -> {
                String name = tab.get(0);
                String href = tab.get(1);
                String url = tab.get(2);

                // check that our temporary zip file contains all the tabs listed in the index.htm
                // if a tab is missing from the zip, download it and add it to the zip file
                Path path = fs.getPath("classtab", href);
                if (Files.notExists(path))
                    addTabToZip(href, path);

                // read content from zip file and insert tab into db
                if (Files.exists(path))
                {
                    try
                    {
                        byte[] encoded = Files.readAllBytes(path);
                        String content = new String(encoded, Charset.defaultCharset());
                        insertTab(artist, name, content, url);
                    }
                    catch (IOException e)
                    {
                        log.error(e.getMessage(), e);
                    }
                }
            }));

            importHasRun = true;
        }
        log.info("Finished ClassTabImporter.doImport().");
    }

    private void addTabToZip(String href, Path path)
    {
        log.warn("Missing file: {}. Attempting download.", path.toString());

        try
        {
            URL url = new URL("http", "classtab.org", 80, "/" + href);
            try (InputStream in = url.openStream())
            {
                Files.copy(in, path);
                log.info("Download and insertion into zip file successful.");
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    private Map<String, List<List<String>>> getArtistToTabMap(List<String> lines) throws IOException
    {
        Map<String, List<List<String>>> artistToTabs = new HashMap<>();
        String artist = "";
        for (String line : lines)
        {
            if (line.startsWith("<b>") && line.contains("</b>"))
            {
                int start = line.indexOf("<b>") + 3;
                int end = line.indexOf("</b>");
                artist = line.substring(start, end);
            }
            if (line.startsWith("<a href") && line.contains(".txt"))
            {
                int linkStart = line.indexOf("<a") + 2;
                int linkEnd = line.indexOf("</a");
                String link = line.substring(linkStart, linkEnd);

                int hrefStart = link.indexOf("\"") + 1;
                int hrefEnd = link.indexOf("\"", hrefStart);
                String href = link.substring(hrefStart, hrefEnd);

                int titleStart = link.indexOf(">") + 1;
                String name = link.substring(titleStart);
                URL url = new URL("http", "classtab.org", 80, "/" + href);

                List<List<String>> artistTabs = artistToTabs.computeIfAbsent(artist, k -> new ArrayList<>());
                artistTabs.add(new ArrayList<>(Arrays.asList(name, href, url.toString())));
            }
        }
        return artistToTabs;
    }

    private void insertTab(String artist, String name, String content, String url)
    {
        tabLogic.insertTab(artist, name, content, "Tab", 0D, 0, "classtab.org", url, "", "", "", "");
    }

    private Path getZipFilePath() throws IOException
    {
        Path zipPath = Paths.get(System.getProperty("java.io.tmpdir"), "classtab.zip");
        if (!zipPath.toFile().exists())
        {
            URL zipUrl = new URL("https", "classtab.org", 443, "/zip/classtab.zip");
            try (BufferedInputStream in = new BufferedInputStream(zipUrl.openStream()))
            {
                Files.copy(in, zipPath);
            }
        }
        return zipPath;
    }

    private List<String> getIndexLines() throws IOException
    {
        List<String> lines = new ArrayList<>();

        URL indexUrl = new URL("https", "classtab.org", 443, "/index_old.htm");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(indexUrl.openStream())))
        {
            reader.lines().forEach(lines::add);
        }
        return lines;
    }
}
