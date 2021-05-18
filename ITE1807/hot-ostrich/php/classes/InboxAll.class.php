<?php

class InboxAll
{
    private int $id;
    private string $title;
    private string $name;
    private string $date;
    private int $opened;

    public function getOpened(): int
    {
        return $this->opened;
    }

    public function setOpened(int $opened): void
    {
        $this->opened = $opened;
    }

    /**
     * InboxAll.class constructor.
     */
    public function __construct()
    {
    }

    public function getId(): int
    {
        return $this->id;
    }

    public function setId(int $id): void
    {
        $this->id = $id;
    }

    public function getTitle(): string
    {
        return $this->title;
    }

    public function setTitle(string $title): void
    {
        $this->title = $title;
    }

    public function getName(): string
    {
        return $this->name;
    }

    public function setName(string $name): void
    {
        $this->name = $name;
    }

    public function getDate(): string
    {
        return $this->date;
    }

    public function setDate(string $date): void
    {
        $this->date = $date;
    }

    public function __toString()
    {
        if ($this->getOpened() == 0) {
            return $this->getTitle() . " (ULEST)";
        }
        return $this->getTitle();
    }
}