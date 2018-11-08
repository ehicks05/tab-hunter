package com.hicksteam.tab;

import com.hicksteam.tab.importLogic.ClassTabImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class ImportController
{
    private static final Logger log = LoggerFactory.getLogger(TabsController.class);
    private ClassTabImporter classTabImporter;

    public ImportController(ClassTabImporter classTabImporter)
    {
        this.classTabImporter = classTabImporter;
    }

    @GetMapping(value = "/import", produces = "application/json")
    public ModelAndView getAjaxSearchResults() throws IOException
    {
        classTabImporter.doImport();

        return new ModelAndView("redirect:/");
    }
}
