<?php

class Degree{
    private int $idDegree;
    private string $degree;

    /**
     * Degree constructor.
     */
    public function __construct(){
    }

    /**
     * @return int
     */
    public function getIdDegree() : int {
        return $this->idDegree;
    }

    /**
     * @param int $idDegree
     */
    public function setIdDegree(int $idDegree) : void {
        $this->idDegree = $idDegree;
    }

    /**
     * @return string
     */
    public function getDegree() : string {
        return $this->degree;
    }

    /**
     * @param string $degree
     */
    public function setDegree(string $degree) : void {
        $this->degree = $degree;
    }

}