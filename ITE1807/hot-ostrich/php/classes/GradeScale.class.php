<?php

class GradeScale{

    private int $idGradeScale;
    private string $scale;

    /**
     * GradeScale constructor.
     */
    public function __construct(){}

    /**
     * @return int
     */
    public function getIdGradeScale(): int {
        return $this->idGradeScale;
    }

    /**
     * @param int $idGradeScale
     */
    public function setIdGradeScale(int $idGradeScale): void {
        $this->idGradeScale = $idGradeScale;
    }

    /**
     * @return string
     */
    public function getScale(): string {
        return $this->scale;
    }

    /**
     * @param string $scale
     */
    public function setScale(string $scale): void {
        $this->scale = $scale;
    }

}