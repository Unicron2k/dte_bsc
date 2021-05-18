<?php

class StudyPoints{
    private int $idStudyPoints;
    private int $studyPoints;

    /**
     * StudyPoints constructor.
     */
    public function __construct(){
    }

    /**
     * @return int
     */
    public function getIdStudyPoints() : int {
        return $this->idStudyPoints;
    }

    /**
     * @param int $idStudyPoints
     */
    public function setIdStudyPoints(int $idStudyPoints) : void {
        $this->idStudyPoints = $idStudyPoints;
    }

    /**
     * @return int
     */
    public function getStudyPoints() : int {
        return $this->studyPoints;
    }

    /**
     * @param int $studyPoints
     */
    public function setStudyPoints(int $studyPoints) : void {
        $this->studyPoints = $studyPoints;
    }

}