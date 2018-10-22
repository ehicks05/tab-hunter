package com.hicksteam.tab;

import com.hicksteam.tab.db.gen.tables.pojos.Tab;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

import static com.hicksteam.tab.db.gen.Tables.TAB;

@Controller
public class TabsController
{
    private static final Logger log = LoggerFactory.getLogger(TabsController.class);
    private DSLContext create;

    public TabsController(DSLContext create)
    {
        this.create = create;
    }

    @GetMapping("/")
    public ModelAndView showIndex()
    {
        List<Tab> topTenTabs = create.selectFrom(TAB).orderBy(TAB.VIEWS.desc()).limit(10).fetchInto(TAB).into(Tab.class);

        ModelAndView mav = new ModelAndView("index");
        mav.addObject("topTenTabs", topTenTabs);
        return mav;
    }

    @GetMapping("/tab")
    public ModelAndView showTab(@RequestParam int tabId)
    {
        ModelAndView mav = new ModelAndView("tab");

        Tab tab = create.selectFrom(TAB).where(TAB.ID.eq(tabId)).fetchAny().into(Tab.class);
        if (tab == null)
        {
            log.error("couldn't find tab.");
            mav.setStatus(HttpStatus.NOT_FOUND);
        }
        else
        {
            mav.addObject("tab", tab);

            create.update(TAB)
                    .set(TAB.VIEWS, TAB.VIEWS.add(1))
                    .where(TAB.ID.eq(tabId))
                    .execute();
        }
        return mav;
    }

    @GetMapping("/artist")
    public ModelAndView showArtistTabs(@RequestParam String artist)
    {
        List<Tab> artistTabs = create.selectFrom(TAB).where(TAB.ARTIST.eq(artist))
                .orderBy(TAB.VIEWS.desc()).limit(10).fetchInto(TAB).into(Tab.class);

        ModelAndView mav = new ModelAndView("artist");
        mav.addObject("artistTabs", artistTabs);
        return mav;
    }

    @GetMapping(value = "/ajaxSearch", produces = "application/json")
    @ResponseBody
    public List<String> getAjaxSearchResults(@RequestParam String query)
    {
        return findTabsByQueryString(query).stream()
                .map(result -> result.getArtist() + " - " + result.getTitle())
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public ModelAndView getSearchResults(@RequestParam String query)
    {
        if (query.isEmpty())
            return new ModelAndView("redirect:/");

        List<Tab> results;
        if (query.contains(" - "))
        {
            String queryArtist = query.split(" - ")[0];
            String queryTitle = query.split(" - ")[1];

            results = create.selectFrom(TAB)
                    .where(TAB.ARTIST.likeIgnoreCase("%" + queryArtist + "%"))
                    .and(TAB.TITLE.likeIgnoreCase("%" + queryTitle + "%"))
                    .orderBy(TAB.VIEWS.desc()).limit(10).fetchInto(TAB).into(Tab.class);
        }
        else
            results = findTabsByQueryString(query);

        ModelAndView mav = new ModelAndView("searchResults");
        mav.addObject("searchResults", results);
        return mav;
    }

    private List<Tab> findTabsByQueryString(@RequestParam String query)
    {
        return create.selectFrom(TAB)
                .where(TAB.ARTIST.likeIgnoreCase("%" + query + "%"))
                .or(TAB.TITLE.likeIgnoreCase("%" + query + "%"))
                .orderBy(TAB.VIEWS.desc()).limit(10).fetchInto(TAB).into(Tab.class);
    }
}
