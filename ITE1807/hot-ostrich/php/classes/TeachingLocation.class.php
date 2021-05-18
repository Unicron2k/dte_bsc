<?php

class TeachingLocation{

    // TODO: Refactor database/Class/Service to allow for addition of TeachingLocation
    // TODO: Can Boolean replace int, due to database being tinyint?
    private int $idTeachingLocation;
    private int $narvik;
    private int $tromso;
    private int $alta;
    private int $moIRana;
    private int $bodo;
    private int $webBased;

    /**
     * TeachingLocation constructor.
     */
    public function __construct(){}

    /**
     * @return int
     */
    public function getIdTeachingLocation(): int {
        return $this->idTeachingLocation;
    }

    /**
     * @param int $idTeachingLocation
     */
    public function setIdTeachingLocation(int $idTeachingLocation): void {
        $this->idTeachingLocation = $idTeachingLocation;
    }

    /**
     * @return int
     */
    public function getNarvik(): int {
        return $this->narvik;
    }

    /**
     * @param int $narvik
     */
    public function setNarvik(int $narvik): void {
        $this->narvik = $narvik;
    }

    /**
     * @return int
     */
    public function getTromso(): int {
        return $this->tromso;
    }

    /**
     * @param int $tromso
     */
    public function setTromso(int $tromso): void {
        $this->tromso = $tromso;
    }

    /**
     * @return int
     */
    public function getAlta(): int {
        return $this->alta;
    }

    /**
     * @param int $alta
     */
    public function setAlta(int $alta): void {
        $this->alta = $alta;
    }

    /**
     * @return int
     */
    public function getMoIRana(): int {
        return $this->moIRana;
    }

    /**
     * @param int $moIRana
     */
    public function setMoIRana(int $moIRana): void {
        $this->moIRana = $moIRana;
    }

    /**
     * @return int
     */
    public function getBodo(): int {
        return $this->bodo;
    }

    /**
     * @param int $bodo
     */
    public function setBodo(int $bodo): void {
        $this->bodo = $bodo;
    }

    /**
     * @return int
     */
    public function getWebBased(): int {
        return $this->webBased;
    }

    /**
     * @param int $webBased
     */
    public function setWebBased(int $webBased): void {
        $this->webBased = $webBased;
    }

}