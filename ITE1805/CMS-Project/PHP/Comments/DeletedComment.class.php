<?php
class deletedComment {
    private $deletedCommentID;
    private $commentID;
    private $userID;
    private $deletedComment;
    private $dateDeleted;

    function __construct() {
    }


    // Getters
    function getDeletedCommentID() {
        return $this->deletedCommentID;
    }
    function getCommentID() {
        return $this->commentID;
    }
    function getUserID() {
        return $this->userID;
    }
    function getDeletedComment() {
        return $this->deletedComment;
    }
    function getDateDeleted() {
        return $this->dateDeleted;
    }

    // Setters
    function setCommentID($commentID) {
        $this->commentID = $commentID;
    }
    function setUserID($userID) {
        $this->userID = $userID;
    }
    function setDeletedComment($deletedCommentContent) {
        $this->deletedComment = $deletedCommentContent;
    }
}