<?php


class Fakultet
{
    private int $idFakultet;
    //private String $nameFakultet;

    /**
     * @return int
     */
    public function getIdFakultet(): int
    {
        return $this->idFakultet;
    }

    /**
     * @param int $idFakultet
     */
    public function setIdFakultet(int $idFakultet): void
    {
        $this->idFakultet = $idFakultet;
    }

    /**
     * @return String
     */
    public function getNameFakultet(): string
    {
        return $this->nameFakultet;
    }

    /**
     * @param String $nameFakultet
     */
    public function setNameFakultet(string $nameFakultet): void
    {
        $this->nameFakultet = $nameFakultet;
    }

}