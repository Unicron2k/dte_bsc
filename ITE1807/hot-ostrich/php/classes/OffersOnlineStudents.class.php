<?php

class OffersOnlineStudents{

    // TODO: Can Boolean replace int, due to database being tinyint?
    private int $idOffersOnlineStudents;
    private int $streaming;
    private int $webMeetingLecture;
    private int $webMeetingEvening;
    private int $followUp;
    private int $organizedArrangements;
    private string $other;
    private int $CourseDescription_idCourse;

    /**
     * OffersOnlineStudents constructor.
     */
    public function __construct(){}

    /**
     * @return int
     */
    public function getIdOffersOnlineStudents(): int {
        return $this->idOffersOnlineStudents;
    }

    /**
     * @param int $idOffersOnlineStudents
     */
    public function setIdOffersOnlineStudents(int $idOffersOnlineStudents): void {
        $this->idOffersOnlineStudents = $idOffersOnlineStudents;
    }

    /**
     * @return int
     */
    public function getStreaming(): int {
        return $this->streaming;
    }

    /**
     * @param int $streaming
     */
    public function setStreaming(int $streaming): void {
        $this->streaming = $streaming;
    }

    /**
     * @return int
     */
    public function getWebMeetingLecture(): int {
        return $this->webMeetingLecture;
    }

    /**
     * @param int $webMeetingLecture
     */
    public function setWebMeetingLecture(int $webMeetingLecture): void {
        $this->webMeetingLecture = $webMeetingLecture;
    }

    /**
     * @return int
     */
    public function getWebMeetingEvening(): int {
        return $this->webMeetingEvening;
    }

    /**
     * @param int $webMeetingEvening
     */
    public function setWebMeetingEvening(int $webMeetingEvening): void {
        $this->webMeetingEvening = $webMeetingEvening;
    }

    /**
     * @return int
     */
    public function getFollowUp(): int {
        return $this->followUp;
    }

    /**
     * @param int $followUp
     */
    public function setFollowUp(int $followUp): void {
        $this->followUp = $followUp;
    }

    /**
     * @return int
     */
    public function getOrganizedArrangements(): int {
        return $this->organizedArrangements;
    }

    /**
     * @param int $organizedArrangements
     */
    public function setOrganizedArrangements(int $organizedArrangements): void {
        $this->organizedArrangements = $organizedArrangements;
    }

    /**
     * @return string
     */
    public function getOther(): string {
        return $this->other;
    }

    /**
     * @param string $other
     */
    public function setOther(string $other): void {
        $this->other = $other;
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

}