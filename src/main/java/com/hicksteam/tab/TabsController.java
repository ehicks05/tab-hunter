package com.hicksteam.tab;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hicksteam.tab.db.gen.tables.pojos.Tab;
import com.hicksteam.tab.importLogic.UGSTab;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hicksteam.tab.db.gen.Tables.TAB;

@Controller
public class TabsController
{
    private static final Logger log = LoggerFactory.getLogger(TabsController.class);
    private DSLContext create;
    private TabLogic tabLogic;
    private static final int RESULT_SIZE = 50;

    public TabsController(DSLContext create, TabLogic tabLogic)
    {
        this.create = create;
        this.tabLogic = tabLogic;
    }

    @Value("${git.total.commit.count}")
    private String commitCount;

    @ModelAttribute("commitCount")
    public String getCommitCount() {
        return commitCount;
    }

    @Value("${basic.message}")
    private String devOrProd;

    @ModelAttribute("devOrProd")
    public String getDevOrProd() {
        return devOrProd;
    }

    @GetMapping("/")
    public String showIndex(Model model)
    {
        List<Tab> tabs = create.selectFrom(TAB).orderBy(TAB.VIEWS.desc()).limit(RESULT_SIZE).fetchInto(TAB).into(Tab.class);

        model.addAttribute("title", "Top " + RESULT_SIZE + " Tabs");
        model.addAttribute("tabs", tabs);

        return "index";
    }

    @GetMapping("/all")
    public ModelAndView showAll()
    {
        List<Tab> tabs = create.selectFrom(TAB).orderBy(TAB.VIEWS.desc()).fetchInto(TAB).into(Tab.class);

        ModelAndView mav = new ModelAndView("index");
        mav.addObject("title", "All Tabs");
        mav.addObject("tabs", tabs);
        return mav;
    }

    @GetMapping("/tab")
    public ModelAndView showTab(@RequestParam int tabHash)
    {
        ModelAndView mav = new ModelAndView("tab");

        Tab tab = create.selectFrom(TAB).where(TAB.HASH.eq(tabHash)).fetchAny().into(Tab.class);
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
                    .where(TAB.HASH.eq(tabHash))
                    .execute();
        }
        return mav;
    }

    @GetMapping("/artist")
    public ModelAndView showArtistTabs(@RequestParam String artist)
    {
        List<Tab> tabs = create.selectFrom(TAB).where(TAB.ARTIST.eq(artist))
                .orderBy(TAB.VIEWS.desc()).fetchInto(TAB).into(Tab.class);

        ModelAndView mav = new ModelAndView("index");
        mav.addObject("title", artist);
        mav.addObject("tabs", tabs);
        return mav;
    }

    @GetMapping(value = "/ajaxSearch", produces = "application/json")
    @ResponseBody
    public List<AjaxSearchResult> getAjaxSearchResults(@RequestParam String query)
    {
        return findTabsByQueryString(query).stream()
                .map(result -> new AjaxSearchResult(result.getArtist(), result.getName(), result.getArtist() + " - " + result.getName()))
                .distinct().collect(Collectors.toList());
    }

    public class AjaxSearchResult
    {
        private String artist;
        private String name;
        private String display;

        public AjaxSearchResult(String artist, String name, String display)
        {
            this.artist = artist;
            this.name = name;
            this.display = display;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AjaxSearchResult that = (AjaxSearchResult) o;
            return Objects.equals(display, that.display);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(display);
        }

        public String getArtist()
        {
            return artist;
        }

        public void setArtist(String artist)
        {
            this.artist = artist;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public String getDisplay()
        {
            return display;
        }

        public void setDisplay(String display)
        {
            this.display = display;
        }
    }

    @GetMapping("/search")
    public ModelAndView getSearchResults(@RequestParam String query, @RequestParam(required = false) String artist, @RequestParam(required = false) String name)
    {
        if (artist == null) artist = "";
        if (name == null) name = "";
        
        if (query.isEmpty() && artist.isEmpty() && name.isEmpty())
            return new ModelAndView("redirect:/");
        
        if (artist.isEmpty() && name.isEmpty() && query.contains(" - "))
        {
            artist = query.split(" - ")[0];
            name = query.split(" - ")[1];
        }

        List<Tab> results;
        if (!artist.isEmpty() && !name.isEmpty())
        {
            results = create.selectFrom(TAB)
                    .where(TAB.ARTIST.likeIgnoreCase("%" + artist + "%"))
                    .and(TAB.NAME.likeIgnoreCase("%" + name + "%"))
                    .orderBy(TAB.VIEWS.desc()).limit(RESULT_SIZE).fetchInto(TAB).into(Tab.class);
        }
        else
            results = findTabsByQueryString(query);

        ModelAndView mav = new ModelAndView("index");
        mav.addObject("title", "Search Results");
        mav.addObject("tabs", results);
        return mav;
    }

    private List<Tab> findTabsByQueryString(@RequestParam String query)
    {
        
        return create.selectFrom(TAB)
                .where(TAB.ARTIST.likeIgnoreCase("%" + query + "%"))
                .or(TAB.NAME.likeIgnoreCase("%" + query + "%"))
                .orderBy(TAB.VIEWS.desc()).limit(RESULT_SIZE).fetchInto(TAB).into(Tab.class);
    }

    @GetMapping("/admin")
    public ModelAndView showAdmin()
    {
        ModelAndView mav = new ModelAndView("admin");
        return mav;
    }

    @GetMapping("/admin/search")
    public ModelAndView adminSearch(@RequestParam String query)
    {
        ModelAndView mav = new ModelAndView("redirect:/search?query=" + query);

        ProcessBuilder processBuilder = new ProcessBuilder("node", "c:/projects/ugs/index.js", query);
        try
        {
            Process p = processBuilder.start();
            OutputStream processIn = p.getOutputStream();
            InputStream processOut = p.getInputStream();

            StringBuilder sb = new StringBuilder();
            byte[] bytes = new byte[4096];
            while (processOut.read(bytes) != -1)
            {
                sb.append(new String(bytes, Charset.forName("UTF-8")));
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

            UGSTab[] ugsTabs = objectMapper.readValue(sb.toString(), UGSTab[].class);
            for (UGSTab ugsTab : ugsTabs)
            {
                tabLogic.insertTab(ugsTab.getArtist(), ugsTab.getName(), ugsTab.getContent(), ugsTab.getType(),
                        ugsTab.getRating(), ugsTab.getNumberRates(), "https://www.ultimate-guitar.com/", ugsTab.getUrl(),
                        ugsTab.getCapo(), ugsTab.getDifficulty(), ugsTab.getTuning(), ugsTab.getTonality());
            }
        }
        catch (IOException e)
        {
            log.error(e.getMessage(), e);
        }

        return mav;
    }

}
