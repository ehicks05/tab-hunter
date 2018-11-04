package com.hicksteam.tab.importLogic;

import com.hicksteam.tab.TabLogic;
import com.hicksteam.tab.TabsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ClassTabImporter
{
    private static final Logger log = LoggerFactory.getLogger(TabsController.class);
    private TabLogic tabLogic;

    public ClassTabImporter(TabLogic tabLogic)
    {
        this.tabLogic = tabLogic;
    }

    public void doImport() throws IOException
    {
        List<String> lines = Files.readAllLines(Paths.get("c:", "k", "classtab", "index3.html"));

        String artist = "";
        for (String line : lines)
        {
            if (line.isEmpty() || line .startsWith("<a name="))
                continue;

            if (line.startsWith("<b>"))
            {
                int start = line.indexOf("<b>") + 3;
                int end = line.indexOf("</b>");
                artist = line.substring(start, end);
            }
            if (line.startsWith("<a"))
            {
                int linkStart = line.indexOf("<a") + 2;
                int linkEnd = line.indexOf("</a");
                String link = line.substring(linkStart, linkEnd);

                int hrefStart = link.indexOf("\"") + 1;
                int hrefEnd = link.indexOf("\"", hrefStart);
                String href = link.substring(hrefStart, hrefEnd);

                int titleStart = link.indexOf(">") + 1;
                String name = link.substring(titleStart);

                Path path = Paths.get("c:", "k", "classtab", href);
                String content = "";
                URL url = new URL("http", "classtab.org", 80, "/" + href);
                if (path.toFile().exists())
                {
                    byte[] encoded = Files.readAllBytes(path);
                    content = new String(encoded, Charset.defaultCharset());
                }
                else
                {
                    log.warn("Missing file: {}. Attempting download.", path.toString());

                    StringBuilder contentBuilder = new StringBuilder();
                    List<String> readerLines = new ArrayList<>();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));)
                    {
                        reader.lines().forEach(readerLine -> {
                            contentBuilder.append(readerLine);
                            readerLines.add(readerLine);
                        });
                    }
                    content = contentBuilder.toString();
                    if (!content.isEmpty())
                        log.info("Found file online.");

                    log.info("Saving file to disk.");
                    try (PrintWriter printWriter = new PrintWriter(path.toFile());)
                    {
                        for (String readerLine : readerLines)
                            printWriter.println(readerLine);
                    }
                }

                tabLogic.insertTab(artist, name, content, "guitar", 0D, 0, "classtab.org", url.toString(), "", "", "", "");
            }
        }
    }
}
