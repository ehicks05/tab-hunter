package com.hicksteam.tab;

import org.jooq.SelectFieldOrAsterisk;

import java.util.Arrays;
import java.util.List;

import static com.hicksteam.tab.db.gen.Tables.TAB;

public enum TabProjection
{
    ALL(Arrays.asList(TAB.asterisk())),
    LIST(Arrays.asList(TAB.HASH, TAB.ARTIST, TAB.NAME, TAB.RATING, TAB.NUMBER_RATES, TAB.VIEWS, TAB.TYPE)),
    SEARCH(Arrays.asList(TAB.HASH, TAB.ARTIST, TAB.NAME, TAB.VIEWS));

    private List<SelectFieldOrAsterisk> fields;

    TabProjection(List<SelectFieldOrAsterisk> fields)
    {
        this.fields = fields;
    }

    public List<SelectFieldOrAsterisk> getFields()
    {
        return fields;
    }
}
