package com.hicksteam.tab;

import com.hicksteam.tab.db.gen.Tables;
import com.hicksteam.tab.db.gen.tables.pojos.Tab;
import com.hicksteam.tab.db.gen.tables.records.TabRecord;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.hicksteam.tab.db.gen.tables.Tab.TAB;

@Controller
public class ImportController
{
    private static final Logger log = LoggerFactory.getLogger(TabsController.class);
    private DSLContext create;

    public ImportController(DSLContext create)
    {
        this.create = create;
    }

    @GetMapping(value = "/import", produces = "application/json")
    @ResponseBody
    public List<TabRecord> getAjaxSearchResults() throws IOException
    {
        List<String> lines = Files.readAllLines(Paths.get("c:", "k", "classtab", "index3.html"));
        
        List<TabRecord> tabs = new ArrayList<>();

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
                String title = link.substring(titleStart);

                Path path = Paths.get("c:", "k", "classtab", href);
                String content = "";
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
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("http", "classtab.org", 80, "/" + href).openStream()));)
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

                // do we already have this content?
                if (isContentUnique(artist, title, content))
                {
                    log.info("Adding {} - {}", artist, title);
                    TabRecord tab = create.newRecord(Tables.TAB);
                    tab.setArtist(artist);
                    tab.setTitle(title);
                    tab.setContent(content);
                    tab.setAuthorId(0L);
                    tab.setCreatedOn(new Timestamp(System.currentTimeMillis()));
                    tab.setRating(0D);
                    tab.setVotes(0);
                    tab.setVersion(0);
                    tab.setType("guitar");
                    tab.setViews(0);
                    tab.setSource("classtab.org");
                    tab.setContentHash(content.hashCode());
                    tab.store();
                }
            }
        }

        return tabs;
    }

    public boolean isContentUnique(String artist, String title, String content)
    {
        int contentHash = content.hashCode();
        List<Tab> matches = create.selectFrom(TAB).where(TAB.CONTENT_HASH.eq(contentHash)).fetchInto(Tables.TAB).into(Tab.class);
        for (Tab match : matches)
            if (match.getContent().equals(content))
            {
                log.info("Incoming tab '{} - {}' has same content as existing tab '{} - {}'", artist, title, match.getArtist(), match.getTitle());
                return false;
            }
            else
                log.info("Incoming tab '{} - {}' has a hash collision, but different content, compared to existing tab '{} - {}'", artist, title, match.getArtist(), match.getTitle());

        return true;
    }
}
