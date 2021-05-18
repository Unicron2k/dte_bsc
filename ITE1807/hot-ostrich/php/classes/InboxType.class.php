<?php

class InboxType
{
    private int $id;
    private string $name;

    /**
     * InboxType constructor.
     */
    public function __construct()
    {
    }

    public function getId() : int
    {
        return $this->id;
    }

    public function setId(int $id): void
    {
        $this->id = $id;
    }

    public function getName() : string
    {
        return $this->name;
    }

    public function setName(string $name): void
    {
        $this->name = $name;
    }
}