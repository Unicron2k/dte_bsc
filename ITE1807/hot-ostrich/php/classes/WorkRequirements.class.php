<?php

//Uses the WorkRequirements_has_CourseDescription connecting-table
class WorkRequirements{

    private int $idWorkRequirements;
    private string $workRequirements;
    private string $dateCreated;

    /**
     * WorkRequirements constructor.
     */
    public function __construct(){}

    /**
     * @return int
     */
    public function getIdWorkRequirements(): int{
        return $this->idWorkRequirements;
    }

    /**
     * @param int $idWorkRequirements
     */
    public function setIdWorkRequirements(int $idWorkRequirements): void{
        $this->idWorkRequirements = $idWorkRequirements;
    }

    /**
     * @return string
     */
    public function getWorkRequirements(): string{
        return $this->workRequirements;
    }

    /**
     * @param string $workRequirements
     */
    public function setWorkRequirements(string $workRequirements): void{
        $this->workRequirements = $workRequirements;
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