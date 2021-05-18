<?php

//Uses the AcademicContent_has_CourseDescription connecting-table
class AcademicContent{

    private int $idAcademicContent;
    private string $academicContent;
    private string $dateCreated;

    /**
     * AcademicContent constructor.
     */
    public function __construct(){}

    /**
     * @return int
     */
    public function getIdAcademicContent(): int{
        return $this->idAcademicContent;
    }

    /**
     * @param int $idAcademicContent
     */
    public function setIdAcademicContent(int $idAcademicContent): void{
        $this->idAcademicContent = $idAcademicContent;
    }

    /**
     * @return string
     */
    public function getAcademicContent(): string{
        return $this->academicContent;
    }

    /**
     * @param string $academicContent
     */
    public function setAcademicContent(string $academicContent): void{
        $this->academicContent = $academicContent;
    }

    /**
     * @return string
     */
    public function getDateCreated(): string{
        return $this->dateCreated;
    }

    /**
     * @param string $dateCreated
     */
    public function setDateCreated(string $dateCreated): void{
        $this->dateCreated = $dateCreated;
    }

}