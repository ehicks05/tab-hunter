package com.hicksteam.tab.importLogic;

public class UGSTab
{
    String artist;
    String name;
    String url;
    double rating;
    int numberRates;
    String type;
    String content;
    String difficulty;
    String capo;
    String tonality;
    String tuning;

    public UGSTab()
    {
    }

    @Override
    public String toString()
    {
        return "UGSTab{" +
                "artist='" + artist + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", rating=" + rating +
                ", numberRates=" + numberRates +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", capo='" + capo + '\'' +
                ", tonality='" + tonality + '\'' +
                ", tuning='" + tuning + '\'' +
                '}';
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

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public double getRating()
    {
        return rating;
    }

    public void setRating(double rating)
    {
        this.rating = rating;
    }

    public int getNumberRates()
    {
        return numberRates;
    }

    public void setNumberRates(int numberRates)
    {
        this.numberRates = numberRates;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getDifficulty()
    {
        return difficulty;
    }

    public void setDifficulty(String difficulty)
    {
        this.difficulty = difficulty;
    }

    public String getCapo()
    {
        return capo;
    }

    public void setCapo(String capo)
    {
        this.capo = capo;
    }

    public String getTonality()
    {
        return tonality;
    }

    public void setTonality(String tonality)
    {
        this.tonality = tonality;
    }

    public String getTuning()
    {
        return tuning;
    }

    public void setTuning(String tuning)
    {
        this.tuning = tuning;
    }
}
