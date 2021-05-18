<?php


class UserAccessControl
{
    //TODO: Add requirement settings for accessFeatures based on RoleControl. Finish Foreach logic in CourseCode and CourseDescription as accessControl features.
    private RoleControl $userRole;

    private bool $AdministratorAccess = false;
    private bool $AcademicContent = false;
    private bool $Approval = false;
    private bool $Comments = false;
    private bool $CompetenceGoals = false;
    private bool $ReadCourseCode = false;
    private bool $EditCourseCode = false;
    private bool $CourseCode = false;
    private bool $CourseCoordinator = false;

    //TODO: Implement single feature calls for CourseDescription such as Delete, Edit, Preview.
    private bool $WrapperCourseDescription = false;
    private bool $CreateCourseDescription = false;
    private bool $EditCourseDescription = false;
    private bool $DeleteCourseDescription = false;
    private bool $PreviewCourseDescription = false;

    private bool $CourseLeader = false;
    private bool $CourseLog = false;
    private bool $Degree = false;
    private bool $ExamType = false;
    private bool $GradeScale = false;
    private bool $WrapperInbox = false;
    private bool $InboxAll = false;
    private bool $InboxIndividual = false;
    private bool $InboxType = false;
    private bool $Language = false;
    private bool $LearningMethods = false;
    private bool $OfferOnlineStudents = false;
    private bool $Prerequisites = false;
    private bool $Statistics = false;
    private bool $StudyPoints = false;
    private bool $TeachingLocation = false;
    private bool $WorkRequirements = false;


    function __construct(RoleControl $Role){
        $this->userRole = $Role;
        if ($this->userRole->getIdRole() == 1){
            $this->AdministratorAccess = true;
        }
    }

    /**
     * @return RoleControl
     */
    public function getUserRole(): RoleControl
    {
        return $this->userRole;
    }

    /**
     * AccessLevel for AcademicContent, gives User access to read AcademicContent.
     * * General Function for most of the Access levels. *
     * @return bool
     */
    public function Access_Administrator(): bool
    {
        return $this->AdministratorAccess;
    }

    /**
     * AccessLevel for AcademicContent, gives User access to read AcademicContent.
     * * General Function for most of the Access levels. *
     * @return bool
     */
    public function Access_AcademicContent(): bool
    {
        if ($this->getUserRole()->getRead() == 1) {
            $this->AcademicContent = true;
        }return $this->AcademicContent;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */
    public function Access_Approval(): bool
    {
        if ($this->getUserRole()->getApprove() == 1) {
            $this->Approval = true;
        }return $this->Approval;
    }

    /**
     * General accessfeature for being able to create and edit comments.
     * @return bool
     */

    public function Access_Comments(): bool
    {
        if ($this->getUserRole()->getRead() == 1 && $this->getUserRole()->getWrite() == 1) {
            $this->Comments = true;
        }return $this->Comments;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_CompetenceGoals(): bool
    {
        if ($this->getUserRole()->getRead() == 1 && $this->getUserRole()->getWrite()) {
            $this->CompetenceGoals = true;
        }return $this->CompetenceGoals;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_CourseCode(): bool
    {
        if ($this->getUserRole()->getCreate() == 1) {
            $this->CourseCode = true;
        }return $this->CourseCode;
    }
    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_EditCourseCode(): bool
    {
        if ($this->getUserRole()->getCreate() == 1) {
            $this->CourseCode = true;
        }return $this->CourseCode;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_ReadCourseCode(): bool
    {
        if ($this->getUserRole()->getRead() == 1) {
            $this->ReadCourseCode = true;
        }return $this->ReadCourseCode;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_CourseCoordinator(): bool
    {
        if ($this->getUserRole()->getCreate() == 1) {
            $this->CourseCoordinator = true;
        }return $this->CourseCoordinator;
    }




    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_WrapperCourseDescription(): bool
    {
        if ($this->getUserRole()->getCreate() == 1 && $this->getUserRole()->getEdit() && $this->getUserRole()->getDelete()) {
            $this->WrapperCourseDescription = true;
        }return $this->WrapperCourseDescription;
    }


    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_CreateCourseDescription(): bool
    {
        if ($this->getUserRole()->getCreate() == 1) {
            $this->CreateCourseDescription = true;
        }return $this->CreateCourseDescription;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_EditCourseDescription(): bool
    {
        if ($this->getUserRole()->getEdit()) {
            $this->EditCourseDescription = true;
        }return $this->EditCourseDescription;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_DeleteCourseDescription(): bool
    {
        if ($this->userRole->getDelete() == 1) {
            $this->DeleteCourseDescription = true;
        }return $this->DeleteCourseDescription;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_PreviewCourseDescription(): bool
    {
        if ($this->userRole->getRead() == 1) {
            $this->PreviewCourseDescription = true;
        }return $this->PreviewCourseDescription;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_CourseLeader(): bool
    {
        if ($this->userRole->getCreate() == 1) {
            $this->CourseLeader = true;
        }return $this->CourseLeader;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_CourseLog(): bool
    {
        if ($this->userRole->getCreate() == 1) {
            $this->CourseLog = true;
        }return $this->CourseLog;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_Degree(): bool
    {
        if ($this->userRole->getCreate() == 1) {
            $this->Degree = true;
        }return $this->Degree;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_ExamType(): bool
    {
        if ($this->userRole->getCreate() == 1) {
            $this->ExamType = true;
        }return $this->ExamType;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_GradeScale(): bool
    {
        if ($this->userRole->getCreate() == 1) {
            $this->GradeScale = true;
        }return $this->GradeScale;
    }


    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_WrapperInbox(): bool
    {
        if ($this->userRole->getRead() == 1 && $this->userRole->getWrite() ) {
            $this->WrapperInbox = true;
        }return $this->WrapperInbox;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_InboxAll(): bool
    {
        if ($this->userRole->getCreate() == 1) {
            $this->InboxAll = true;
        }return $this->InboxAll;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_InboxIndividual(): bool
    {
        if ($_SESSION['bruker']->isLoggedIn()) {
            $this->InboxIndividual = true;
        }return $this->InboxIndividual;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_InboxType(): bool
    {
        if ($this->userRole->getCreate() == 1) {
            $this->InboxType = true;
        }return $this->InboxType;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_Language(): bool
    {
        if ($this->userRole->getCreate() == 1) {
            $this->Language = true;
        }return $this->Language;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_LearningMethods(): bool
    {
        if ($this->userRole->getCreate() == 1) {
            $this->LearningMethods = true;
        }return $this->LearningMethods;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_OfferOnlineStudents(): bool
    {
        if ($this->userRole->getCreate() == 1) {
            $this->OfferOnlineStudents = true;
        }return $this->OfferOnlineStudents;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_Prerequisites(): bool
    {
        if ($this->userRole->getCreate() == 1) {
            $this->Prerequisites = true;
        }return $this->Prerequisites;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_Statistics(): bool
    {
        if ($this->userRole->getApprove() == 1) {
            $this->Statistics = true;
        }return $this->Statistics;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_StudyPoints(): bool
    {
        if ($this->userRole->getCreate() == 1) {
            $this->StudyPoints = true;
        }return $this->StudyPoints;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_TeachingLocation(): bool
    {
        if ($this->userRole->getCreate() == 1) {
            $this->TeachingLocation = true;
        }return $this->TeachingLocation;
    }

    /**
     * AccessLevel for **, gives User access to **,
     * and the ability to **. * Mainly for ** and ** *
     * @return bool
     */

    public function Access_WorkRequirements(): bool
    {
        if ($this->userRole->getCreate() == 1) {
            $this->WorkRequirements = true;
        }return $this->WorkRequirements;
    }
}