<?php

//Uses the CompetenceGoals_has_CourseDescription connecting-table
class CompetenceGoals{

    private int $idCompetenceGoals;
    private string $competenceGoals;
    private string $dateCreated;

    /**
     * CompetenceGoals constructor.
     */
    public function __construct(){}

    /**
     * @return int
     */
    public function getIdCompetenceGoals(): int{
        return $this->idCompetenceGoals;
    }

    /**
     * @param int $idCompetenceGoals
     */
    public function setIdCompetenceGoals(int $idCompetenceGoals): void{
        $this->idCompetenceGoals = $idCompetenceGoals;
    }

    /**
     * @return string
     */
    public function getCompetenceGoals(): string{
        return $this->competenceGoals;
    }

    /**
     * @param string $competenceGoals
     */
    public function setCompetenceGoals(string $competenceGoals): void{
        $this->competenceGoals = $competenceGoals;
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