<?php
class comment {
    private $CommentID;
    private $FileID;
    private $UserID;
    private $Date;
    private $Comment;
    private $File_FileID;
    private $File_User_UserID;
    private $User_UserID;
    private $Comments_CommentID;
    private $Comments_File_FileID;
    private $Comments_File_User_UserID;
    private $Comments_User_UserID;

    function __construct() {
    }

    /**
     * @return mixed
     */
    public function getCommentID()
    {
        return $this->CommentID;
    }

    /**
     * @param mixed $CommentID
     */
    public function setCommentID($CommentID): void
    {
        $this->CommentID = $CommentID;
    }

    /**
     * @return mixed
     */
    public function getFileID()
    {
        return $this->FileID;
    }

    /**
     * @param mixed $FileID
     */
    public function setFileID($FileID): void
    {
        $this->FileID = $FileID;
    }

    /**
     * @return mixed
     */
    public function getUserID()
    {
        return $this->UserID;
    }

    /**
     * @param mixed $UserID
     */
    public function setUserID($UserID): void
    {
        $this->UserID = $UserID;
    }

    /**
     * @return mixed
     */
    public function getDate()
    {
        return $this->Date;
    }

    /**
     * @param mixed $Date
     */
    public function setDate($Date): void
    {
        $this->Date = $Date;
    }

    /**
     * @return mixed
     */
    public function getComment()
    {
        return $this->Comment;
    }

    /**
     * @param mixed $Comment
     */
    public function setComment($Comment): void
    {
        $this->Comment = $Comment;
    }

    /**
     * @return mixed
     */
    public function getFileFileID()
    {
        return $this->File_FileID;
    }

    /**
     * @param mixed $File_FileID
     */
    public function setFileFileID($File_FileID): void
    {
        $this->File_FileID = $File_FileID;
    }

    /**
     * @return mixed
     */
    public function getFileUserUserID()
    {
        return $this->File_User_UserID;
    }

    /**
     * @param mixed $File_User_UserID
     */
    public function setFileUserUserID($File_User_UserID): void
    {
        $this->File_User_UserID = $File_User_UserID;
    }

    /**
     * @return mixed
     */
    public function getUserUserID()
    {
        return $this->User_UserID;
    }

    /**
     * @param mixed $User_UserID
     */
    public function setUserUserID($User_UserID): void
    {
        $this->User_UserID = $User_UserID;
    }

    /**
     * @return mixed
     */
    public function getCommentsCommentID()
    {
        return $this->Comments_CommentID;
    }

    /**
     * @param mixed $Comments_CommentID
     */
    public function setCommentsCommentID($Comments_CommentID): void
    {
        $this->Comments_CommentID = $Comments_CommentID;
    }

    /**
     * @return mixed
     */
    public function getCommentsFileFileID()
    {
        return $this->Comments_File_FileID;
    }

    /**
     * @param mixed $Comments_File_FileID
     */
    public function setCommentsFileFileID($Comments_File_FileID): void
    {
        $this->Comments_File_FileID = $Comments_File_FileID;
    }

    /**
     * @return mixed
     */
    public function getCommentsFileUserUserID()
    {
        return $this->Comments_File_User_UserID;
    }

    /**
     * @param mixed $Comments_File_User_UserID
     */
    public function setCommentsFileUserUserID($Comments_File_User_UserID): void
    {
        $this->Comments_File_User_UserID = $Comments_File_User_UserID;
    }

    /**
     * @return mixed
     */
    public function getCommentsUserUserID()
    {
        return $this->Comments_User_UserID;
    }

    /**
     * @param mixed $Comments_User_UserID
     */
    public function setCommentsUserUserID($Comments_User_UserID): void
    {
        $this->Comments_User_UserID = $Comments_User_UserID;
    }






}