package com.daybreaktech.xrpltools.backendapi.domain;

public enum AirdropCategories {

    FEATURED(false),
    FAUCETS(false),
    UPCOMING(false),
    MISC(false),
    HOLDERS(false),
    EXPIRED(false),
    TRASH(true),
    ARCHIVE(true);


    AirdropCategories(boolean hidden) {
        this.hidden = hidden;
    }

    private boolean hidden;

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
