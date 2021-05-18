<?php

class InboxIndividual
{
    private int $id;
    private string $title;
    private string $description;
    private int $opened;
    private string $name;
    private string $date;

    /**
     * InboxIndividual.class constructor.
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

    public function getDescription(): string
    {
        return $this->description;
    }

    public function setDescription(string $description): void
    {
        $this->description = $description;
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

    public function isOpened(): bool
    {
        return $this->opened == 1;
    }

    public function setOpened(int $opened): void
    {
        $this->opened = $opened;
    }
}