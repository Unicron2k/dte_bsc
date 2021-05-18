<?php

//Uses the LearningMethods_has_CourseDescription connecting-table
class LearningMethods{

    private int $idLearningMethods;
    private string $learningMethods;
    private string $dateCreated;

    /**
     * LearningMethods constructor.
     */
    public function __construct(){}

    /**
     * @return int
     */
    public function getIdLearningMethods(): int{
        return $this->idLearningMethods;
    }

    /**
     * @param int $idLearningMethods
     */
    public function setIdLearningMethods(int $idLearningMethods): void{
        $this->idLearningMethods = $idLearningMethods;
    }

    /**
     * @return string
     */
    public function getLearningMethods(): string{
        return $this->learningMethods;
    }

    /**
     * @param string $learningMethods
     */
    public function setLearningMethods(string $learningMethods): void{
        $this->learningMethods = $learningMethods;
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