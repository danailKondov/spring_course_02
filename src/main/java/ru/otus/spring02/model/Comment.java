package ru.otus.spring02.model;

import java.util.Date;

public class Comment {

    private String userName;
    private String commentText;
    private Date commentDate;

    public Comment(String userName, String comment) {
        this.userName = userName;
        commentText = comment;
        commentDate = new Date();
    }

    public Comment() {
        commentDate = new Date();
    }

    public String getUser() {
        return userName;
    }

    public void setUser(String user) {
        this.userName = userName;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (userName != null ? !userName.equals(comment.userName) : comment.userName != null) return false;
        if (getCommentText() != null ? !getCommentText().equals(comment.getCommentText()) : comment.getCommentText() != null)
            return false;
        return getCommentDate() != null ? getCommentDate().equals(comment.getCommentDate()) : comment.getCommentDate() == null;
    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (getCommentText() != null ? getCommentText().hashCode() : 0);
        result = 31 * result + (getCommentDate() != null ? getCommentDate().hashCode() : 0);
        return result;
    }
}
