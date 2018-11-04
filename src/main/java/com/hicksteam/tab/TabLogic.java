package com.hicksteam.tab;

import com.hicksteam.tab.db.gen.Tables;
import com.hicksteam.tab.db.gen.tables.pojos.Tab;
import com.hicksteam.tab.db.gen.tables.records.TabRecord;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.util.List;

import static com.hicksteam.tab.db.gen.tables.Tab.TAB;

@Configuration
public class TabLogic
{
    private static final Logger log = LoggerFactory.getLogger(TabsController.class);
    private DSLContext create;

    public TabLogic(DSLContext create)
    {
        this.create = create;
    }

    public void insertTab(String artist, String name, String content, String type, double rating, int numberRates,
                                 String source, String url, String capo, String difficulty, String tuning, String tonality)
    {

        if (isContentUnique(artist, name, content))
        {
            log.info("Adding {} - {}", artist, name);
            TabRecord tab = create.newRecord(Tables.TAB);
            tab.setArtist(artist);
            tab.setName(name);
            tab.setContent(content);
            tab.setCreatedOn(new Timestamp(System.currentTimeMillis()));
            tab.setRating(rating);
            tab.setNumberRates(numberRates);
            tab.setType(type);
            tab.setSource(source);
            tab.setUrl(url);
            tab.setCapo(capo);
            tab.setDifficulty(difficulty);
            tab.setTonality(tonality);
            tab.setTuning(tuning);
            tab.setHash(content.hashCode());
            tab.store();
        }
        else
        {
            log.info("Not Adding {} - {}. Reason: Duplicate of existing tab.", artist, name);
        }
    }

    private boolean isContentUnique(String artist, String title, String content)
    {
        int hash = content.hashCode();
        List<Tab> matches = create.selectFrom(TAB).where(TAB.HASH.eq(hash)).fetchInto(Tables.TAB).into(Tab.class);
        for (Tab match : matches)
            if (match.getContent().equals(content))
            {
                log.info("Incoming tab '{} - {}' has same content as existing tab '{} - {}'", artist, title, match.getArtist(), match.getName());
                return false;
            }
            else
                log.info("Incoming tab '{} - {}' has a hash collision, but different content, compared to existing tab '{} - {}'",
                        artist, title, match.getArtist(), match.getName());

        return true;
    }
}
