<?php

class Comments{

    private int $idComment;
    private string $title;
    private string $content;
    private string $date;
    private int $CourseDescription_idCourse;
    private int $User_idUser;

    /**
     * Comments constructor.
     */
    public function __construct(){}

    /**
     * @return int
     */
    public function getIdComment(): int {
        return $this->idComment;
    }

    /**
     * @param int $idComment
     */
    public function setIdComment(int $idComment): void {
        $this->idComment = $idComment;
    }

    /**
     * @return string
     */
    public function getTitle(): string {
        return $this->title;
    }

    /**
     * @param string $title
     */
    public function setTitle(string $title): void {
        $this->title = $title;
    }

    /**
     * @return string
     */
    public function getContent(): string {
        return $this->content;
    }

    /**
     * @param string $content
     */
    public function setContent(string $content): void {
        $this->content = $content;
    }

    /**
     * @return string
     */
    public function getDate(): string {
        return $this->date;
    }

    /**
     * @param string $date
     */
    public function setDate(string $date): void {
        $this->date = $date;
    }

    /**
     * @return int
     */
    public function getCourseDescriptionIdCourse(): int {
        return $this->CourseDescription_idCourse;
    }

    /**
     * @param int $CourseDescription_idCourse
     */
    public function setCourseDescriptionIdCourse(int $CourseDescription_idCourse): void {
        $this->CourseDescription_idCourse = $CourseDescription_idCourse;
    }

    /**
     * @return int
     */
    public function getUserIdUser(): int {
        return $this->User_idUser;
    }

    /**
     * @param int $User_idUser
     */
    public function setUserIdUser(int $User_idUser): void {
        $this->User_idUser = $User_idUser;
    }

}