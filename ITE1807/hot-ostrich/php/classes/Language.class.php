<?php

class Language{

    private int $idLanguage;
    private string $language;

    /**
     * Language constructor.
     */
    public function __construct(){}

    /**
     * @return int
     */
    public function getIdLanguage(): int {
        return $this->idLanguage;
    }

    /**
     * @param int $idLanguage
     */
    public function setIdLanguage(int $idLanguage): void {
        $this->idLanguage = $idLanguage;
    }

    /**
     * @return string
     */
    public function getLanguage(): string {
        return $this->language;
    }

    /**
     * @param string $language
     */
    public function setLanguage(string $language): void {
        $this->language = $language;
    }

}