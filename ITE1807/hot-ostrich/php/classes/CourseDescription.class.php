<?php

class CourseDescription
{

    const ARCHIVED_FALSE = 0;
    const ARCHIVED_TRUE = 1;
    const ARCHIVED_UPDATED = 2;

    // FIXME: fix nullable types with minor database update
    private int $idCourse;
    private int $year;
    private String $dateCreated; //Auto-set in database upon insertion
    private ?String $dateChanged; // FIXME: Set to $dateCreated in database upon creation and remove nullable type, remove initializer in service-class
    private bool $singleCourse;
    private bool $continuation;
    private bool $semesterFall;
    private bool $semesterSpring;
    private int $archived;
    private int $CreatedBy_idUser;
    private int $Language_idLanguage;
    private int $ExamType_idExamType;
    private int $GradeScale_idGradeScale;
    private int $TeachingLocation_idTeachingLocation;
    private ?int $ArchivedBy_idUser; // FIXME: Set to 0 in database upon creation and remove nullable type

    /**
     * CourseDescription constructor.
     */
    public function __construct(){ }

    /**
     * @return int
     */
    public function getIdCourse(): int {
        return $this->idCourse;
    }

    /**
     * @param int $idCourse
     */
    public function setIdCourse(int $idCourse): void {
        $this->idCourse = $idCourse;
    }

    /**
     * @return int
     */
    public function getYear(): int {
        return $this->year;
    }

    /**
     * @param int $year
     */
    public function setYear(int $year): void {
        $this->year = $year;
    }

    /**
     * @return String
     */
    public function getDateCreated(): String {
        return $this->dateCreated;
    }

    /**
     * @param String $dateCreated
     */
    public function setDateCreated(String $dateCreated): void {
        $this->dateCreated = $dateCreated;
    }

    /**
     * @return String
     */
    public function getDateChanged(): ?String { // FIXME: fix nullable types with minor database update
        return $this->dateChanged;
    }

    /**
     * @param String $dateChanged
     */
    public function setDateChanged(String $dateChanged): void {
        $this->dateChanged = $dateChanged;
    }

    /**
     * @return bool
     */
    public function isSingleCourse(): bool {
        return $this->singleCourse;
    }

    /**
     * @param bool $singleCourse
     */
    public function setSingleCourse(bool $singleCourse): void {
        $this->singleCourse = $singleCourse;
    }

    /**
     * @return bool
     */
    public function isContinuation(): bool {
        return $this->continuation;
    }

    /**
     * @param bool $continuation
     */
    public function setContinuation(bool $continuation): void {
        $this->continuation = $continuation;
    }

    public function getSemester() : string {
        if ($this->isSemesterFall() && $this->isSemesterSpring()) {
            return "Høst og Vår";
        } else if ($this->isSemesterFall()) {
            return "Høst";
        } else if ($this->isSemesterSpring()) {
            return "Vår";
        }
        return "Ingen termin oppgitt.";
    }

    /**
     * @return bool
     */
    public function isSemesterFall(): bool {
        return $this->semesterFall;
    }

    /**
     * @param bool $semesterFall
     */
    public function setSemesterFall(bool $semesterFall): void {
        $this->semesterFall = $semesterFall;
    }

    /**
     * @return bool
     */
    public function isSemesterSpring(): bool {
        return $this->semesterSpring;
    }

    /**
     * @param bool $semesterSpring
     */
    public function setSemesterSpring(bool $semesterSpring): void {
        $this->semesterSpring = $semesterSpring;
    }

    /**
     * @return int
     */
    public function getArchived(): int {
        return $this->archived;
    }

    /**
     * ARCHIVED_FALSE = 0;
     * ARCHIVED_TRUE = 1;
     * ARCHIVED_UPDATED = 2;
     * @param int $archived
     */
    public function setArchived(int $archived): void {
        $this->archived = $archived;
    }

    /**
     * @return int
     */
    public function getCreatedByIdUser(): int {
        return $this->CreatedBy_idUser;
    }

    /**
     * @param int $CreatedBy_idUser
     */
    public function setCreatedByIdUser(int $CreatedBy_idUser): void {
        $this->CreatedBy_idUser = $CreatedBy_idUser;
    }

    /**
     * @return int
     */
    public function getLanguageIdLanguage(): int {
        return $this->Language_idLanguage;
    }

    /**
     * @param int $Language_idLanguage
     */
    public function setLanguageIdLanguage(int $Language_idLanguage): void {
        $this->Language_idLanguage = $Language_idLanguage;
    }

    /**
     * @return int
     */
    public function getExamTypeIdExamType(): int {
        return $this->ExamType_idExamType;
    }

    /**
     * @param int $ExamType_idExamType
     */
    public function setExamTypeIdExamType(int $ExamType_idExamType): void {
        $this->ExamType_idExamType = $ExamType_idExamType;
    }

    /**
     * @return int
     */
    public function getGradeScaleIdGradeScale(): int {
        return $this->GradeScale_idGradeScale;
    }

    /**
     * @param int $GradeScale_idGradeScale
     */
    public function setGradeScaleIdGradeScale(int $GradeScale_idGradeScale): void {
        $this->GradeScale_idGradeScale = $GradeScale_idGradeScale;
    }

    /**
     * @return int
     */
    public function getTeachingLocationIdTeachingLocation(): int {
        return $this->TeachingLocation_idTeachingLocation;
    }

    /**
     * @param int $TeachingLocation_idTeachingLocation
     */
    public function setTeachingLocationIdTeachingLocation(int $TeachingLocation_idTeachingLocation): void {
        $this->TeachingLocation_idTeachingLocation = $TeachingLocation_idTeachingLocation;
    }

    /**
     * @return int
     */
    public function getArchivedByIdUser(): ?int { // FIXME: fix nullable types with minor database update
        return $this->ArchivedBy_idUser;
    }

    /**
     * @param int $ArchivedBy_idUser
     */
    public function setArchivedByIdUser(int $ArchivedBy_idUser): void{
        $this->ArchivedBy_idUser = $ArchivedBy_idUser;
    }



}