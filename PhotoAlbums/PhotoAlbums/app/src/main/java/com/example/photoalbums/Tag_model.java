package com.example.photoalbums;

public class Tag_model {
    String tagname;

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public String getTagvalue() {
        return tagvalue;
    }

    public void setTagvalue(String tagvalue) {
        this.tagvalue = tagvalue;
    }

    String tagvalue;

    public Tag_model(String tagname, String tagvalue) {
        this.tagname = tagname;
        this.tagvalue = tagvalue;
    }
}
