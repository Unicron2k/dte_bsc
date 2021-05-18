<?php

class CourseCoordinatorWithUser
{
    private int $CourseDescription_idCourse;
    private string $CoursePart;
    private int $idUser;
    private string $firstName;
    private string $lastName;

    public function getCourseDescriptionIdCourse(): int
    {
        return $this->CourseDescription_idCourse;
    }

    public function setCourseDescriptionIdCourse(int $CourseDescription_idCourse): void
    {
        $this->CourseDescription_idCourse = $CourseDescription_idCourse;
    }

    public function getCoursePart(): string
    {
        return $this->CoursePart;
    }

    public function setCoursePart(string $CoursePart): void
    {
        $this->CoursePart = $CoursePart;
    }

    public function getIdUser(): int
    {
        return $this->idUser;
    }

    public function setIdUser(int $idUser): void
    {
        $this->idUser = $idUser;
    }

    public function getFirstName(): string
    {
        return $this->firstName;
    }

    public function setFirstName(string $firstName): void
    {
        $this->firstName = $firstName;
    }

    public function getLastName(): string
    {
        return $this->lastName;
    }

    public function setLastName(string $lastName): void
    {
        $this->lastName = $lastName;
    }

    public function __toString()
    {
        return $this->getFirstName() . " " . $this->getLastName();
    }
}