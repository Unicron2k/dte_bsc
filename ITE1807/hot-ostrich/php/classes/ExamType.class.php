<?php

class ExamType{

    private int $idExamType;
    private string $examType;

    /**
     * ExamType constructor.
     */
    public function __construct(){}

    /**
     * @return int
     */
    public function getIdExamType(): int {
        return $this->idExamType;
    }

    /**
     * @param int $idExamType
     */
    public function setIdExamType(int $idExamType): void {
        $this->idExamType = $idExamType;
    }

    /**
     * @return string
     */
    public function getExamType(): string {
        return $this->examType;
    }

    /**
     * @param string $examType
     */
    public function setExamType(string $examType): void {
        $this->examType = $examType;
    }

}