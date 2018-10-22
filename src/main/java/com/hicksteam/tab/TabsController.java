package com.hicksteam.tab;

import com.hicksteam.tab.db.gen.tables.pojos.Tab;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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
    public ModelAndView showTabs()
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
}
