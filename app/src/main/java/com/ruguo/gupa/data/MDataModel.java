package com.ruguo.gupa.data;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.List;

public class MDataModel implements Serializable {

    private List<String> commentContentList;

    public List<String> getCommentContentList() {
        return commentContentList;
    }

    public void setCommentContentList(List<String> commentContentList) {
        this.commentContentList = commentContentList;
    }
}
