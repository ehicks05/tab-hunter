package com.hicksteam.arc;

import com.hicksteam.tab.db.gen.Tables;
import com.hicksteam.tab.db.gen.tables.Tabs;
import com.hicksteam.tab.db.gen.tables.Users;
import com.hicksteam.tab.db.gen.tables.records.TabsRecord;
import com.hicksteam.tab.db.gen.tables.records.UsersRecord;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TabsController
{
    private DSLContext create;

    public TabsController(DSLContext create)
    {
        this.create = create;
    }

    @GetMapping ("/users")
    public String showTabs()
    {
        Result<TabsRecord> tabs = create.selectFrom(Tables.TABS).fetchInto(Tabs.TABS);
        for (TabsRecord tab : tabs)
        {
            tab.getId();
        }

        Result<UsersRecord> users = create.selectFrom(Tables.USERS).fetchInto(Users.USERS);
        for (UsersRecord user : users)
        {
            System.out.println(user.getUserId());
            System.out.println(user.getUsername());
            System.out.println(user.getPassword());
            System.out.println(user.getEnabled());
        }

        return "tabs";
    }
}
