<?php

class PDF extends TCPDF
{

    private array $arrWidths = [65, 115];
    private array $data;
    private array $header = array('Innholdskrav', 'Utdypende opplysninger og Kommentarer');
    private string $strHeader;

    /**
     * Sets up the page header
     */
    public function Header()
    {
        // Do this only on the first page
        if (count($this->pages) === 1) {
            // Use the following line for local dev:
            //$image_file = $_SERVER["DOCUMENT_ROOT"] . 'hot-ostrich/img/uit_logo.png';
            //Use this line in production
            $image_file = 'img/uit_logo.png';
            $this->Image($image_file, 10, 10, 25, '', 'PNG', '', 'T', false, 300, '', false, false, 0, false, false, false);
            $this->SetFont('helvetica', 'B', 20);
            $this->setY($this->getY() + 12);
            $this->Cell(0, 15, $this->strHeader, 0, false, 'C', 0, '', 0, false, 'M', 'M');
        } else {
            // Add top-border for cells on new-page (Actually a part of the page-header, but nobody needs to know that...)
            $this->setX(PDF_MARGIN_LEFT);
            $this->setY($this->getY() + 17.4);
            $this->Cell(array_sum($this->arrWidths), 0, '', 'B');
        }
    }


    /**
     * Loads all data for a given course-ID
     * @param int $idCourse ID of course to load data for.
     */
    protected function LoadData(int $idCourse)
    {
        // Dummy-Data
        $courseDescriptionService = new CourseDescriptionService(DB::getDBConnection());
        $courseDescription = $courseDescriptionService->getCourseDescription($idCourse);

        $languageService = new LanguageService(DB::getDBConnection());
        $language = $languageService->getLanguageByCourseDescription($idCourse);
        $examTypeService = new ExamTypeService(DB::getDBConnection());
        $examType = $examTypeService->getExamTypeByCourseDescription($idCourse);
        $gradeScaleService = new GradeScaleService(DB::getDBConnection());
        $gradeScale = $gradeScaleService->getGradeScaleByCourseDescription($idCourse);
        $teachingLocationService = new TeachingLocationService(DB::getDBConnection());
        $teachingLocations = $teachingLocationService->getTeachingLocationsByCourseDescription($idCourse);

        $academicContentService = new AcademicContentService(DB::getDBConnection());
        $academicContent = $academicContentService->getLastEntryForCourseDescription($idCourse);
        $learningMethodsService = new LearningMethodsService(DB::getDBConnection());
        $learningMethods = $learningMethodsService->getLastEntryForCourseDescription($idCourse);
        $competenceGoalsService = new CompetenceGoalsService(DB::getDBConnection());
        $competenceGoals = $competenceGoalsService->getLastEntryForCourseDescription($idCourse);
        $workRequirementsService = new WorkRequirementsService(DB::getDBConnection());
        $workRequirements = $workRequirementsService->getLastEntryForCourseDescription($idCourse);

        $offersOnlineStudentsService = new OffersOnlineStudentsService(DB::getDBConnection());
        $offersOnlineStudents = $offersOnlineStudentsService->getOffersByCourseDescription($idCourse);
        if($offersOnlineStudents==null){
            $offersOnlineStudents = new OffersOnlineStudents();
            $offersOnlineStudents->setCourseDescriptionIdCourse(0);
            $offersOnlineStudents->setFollowUp(0);
            $offersOnlineStudents->setIdOffersOnlineStudents(0);
            $offersOnlineStudents->setOrganizedArrangements(0);
            $offersOnlineStudents->setOther(" ");
            $offersOnlineStudents->setStreaming(0);
            $offersOnlineStudents->setWebMeetingEvening(0);
            $offersOnlineStudents->setWebMeetingLecture(0);

        }
        $prerequisitesService = new PrerequisitesService(DB::getDBConnection());

        $recommendedPrerequisites = $prerequisitesService->getPrerequisiteCourseCodesByIdCourse($idCourse, false);
        $requiredPrerequisites = $prerequisitesService->getPrerequisiteCourseCodesByIdCourse($idCourse, true);

        $courseCodeService = new CourseCodeService(DB::getDBConnection());
        $courseCode = $courseCodeService->getCourseCode($courseDescriptionService->getCourseCodeIdFromCourseId($idCourse));
        $degreeService = new DegreeService(DB::getDBConnection());
        $degree = $degreeService->getDegree($courseCode->getDegreeIdDegree());
        $studyPointsService = new StudyPointsService(DB::getDBConnection());
        $studyPoints = $studyPointsService->getStudyPoints($courseCode->getStudyPointsIdStudyPoints());
        $courseLeaderService = new CourseLeaderService(DB::getDBConnection());
        $courseLeader = $courseLeaderService->getCourseLeaderByCourseCode($courseCode->getIdCourseCode());
        $courseCoordinatorService = new CourseCoordinatorService(DB::getDBConnection());
        $courseCoordinators = $courseCoordinatorService->getAllCourseCoordinatorsByCourseDescription($idCourse);
        $courseLogService = new CourseLogService(DB::getDBConnection());
        $arrCourseLogs = $courseLogService->getAllCourseLogsForCourseDescription($idCourse);
        $courseLog = reset($arrCourseLogs);
        $userService = new UserService(DB::getDBConnection());
        $user = $userService->getUser($courseDescription->getCreatedByIdUser());


        $this->strHeader = $courseCode->getCourseCode() . " " . $courseCode->getNameNbNo();
        if ($courseDescription->isSemesterSpring() && $courseDescription->isSemesterFall())
            $this->strHeader .= ", " . $courseDescription->getYear();
        else if ($courseDescription->isSemesterSpring())
            $this->strHeader .= ", vår " . $courseDescription->getYear();
        else //if we get here, it probably is fall....
            $this->strHeader .= ", høst " . $courseDescription->getYear();

        $strCourseName = "Navn;Bokmål: " . $courseCode->getNameNbNo() . "\nNynorsk: " . $courseCode->getNameNbNn() . "\nEnglish: " . $courseCode->getNameEnGb() . "";
        $strSemester = "Undervisningstermin;" . ($courseDescription->isSemesterSpring() ? "Vår" : "") . ($courseDescription->isSemesterSpring() && $courseDescription->isSemesterFall() ? ", " : "") . ($courseDescription->isSemesterFall() ? "Høst" : "");

        $strGradeLevel = "Emnekode og emnenivå;" . $courseCode->getCourseCode() . " - " . $degree->getDegree();
        $strSingleCourse = "Enkeltemne;Emnet tilbys" . ($courseDescription->isSingleCourse() ? " " : " ikke ") . "som enkeltemne.";
        $strWebBased = "Nettbasert/Strømming;Emnet tilbys" . ($teachingLocations->getWebBased() ? " " : " ikke ") . "nettbasert.\nEmnet tilbyr" . ($offersOnlineStudents->getStreaming() ? " " : " ikke ") . "strømming.";
        $strOffersOnlineStudents = "Tilbud nettstudenter;For nettstudenter tilbys følgende:\n";

        if ($offersOnlineStudents->getStreaming())
            $strOffersOnlineStudents .= "   - Strømming av forelesningene\n";
        if ($offersOnlineStudents->getWebMeetingLecture())
            $strOffersOnlineStudents .= "   - Åpent nettmøte under forelesninger\n";
        if ($offersOnlineStudents->getWebMeetingEvening())
            $strOffersOnlineStudents .= "   - Nettmøte med studentassistenter på kveldstid\n";
        if ($offersOnlineStudents->getFollowUp())
            $strOffersOnlineStudents .= "   - Oppfølging via Telefon, Canvas, mail, VOIP e.l.\n";
        if ($offersOnlineStudents->getOrganizedArrangements())
            $strOffersOnlineStudents .= "   - Organiserte aktiviteter i samlingsukene\n";
        if (strlen($offersOnlineStudents->getOther()) > 1)
            $strOffersOnlineStudents .= "\nAnnet: " . $offersOnlineStudents->getOther();

        $strTeachingLocations = "Undervisningssted;Kurset tilbys ved følgende undervisningssteder:\n";
        $arrTeachingLocations = null;//dirty hack to remove "pssibly uninitialized" warning
        if ($teachingLocations->getNarvik())
            $arrTeachingLocations[] = "Narvik";
        if ($teachingLocations->getTromso())
            $arrTeachingLocations[] = "Tromsø";
        if ($teachingLocations->getAlta())
            $arrTeachingLocations[] = "Alta";
        if ($teachingLocations->getMoIRana())
            $arrTeachingLocations[] = "Mo i Rana";
        if ($teachingLocations->getBodo())
            $arrTeachingLocations[] = "Bodø";
        if ($teachingLocations->getWebBased())
            $arrTeachingLocations[] = "Nettbasert";
        $tmpStr = "";
        foreach ($arrTeachingLocations as $location)
            $tmpStr .= $location . ", ";
        $strTeachingLocations .= substr_replace($tmpStr, ".", strlen($tmpStr) - 2);


        $strStudyPoints = "Omfang (studiepoeng);Emnet består av " . $studyPoints->getStudyPoints() . " studiepoeng.";

        $strRecommendedPrerequisites = "Anbefalte forkunnskaper;Følgende forkunnskaper er anbefalt:";
        foreach ($recommendedPrerequisites as $recommendedPrerequisite)
            $strRecommendedPrerequisites .= "\n    " . $recommendedPrerequisite->getCourseCode() . "  -  " . $recommendedPrerequisite->getNameNbNo();

        $strRequiredPrerequisites = "Påkrevde forkunnskaper;Følgende forkunnskaper er påkrevd:";
        foreach ($requiredPrerequisites as $requiredPrerequisite)
            $strRequiredPrerequisites .= "\n    " . $requiredPrerequisite->getCourseCode() . "  -  " . $requiredPrerequisite->getNameNbNo();

        $strCompetenceGoals = "Kompetansemål;" . $competenceGoals->getCompetenceGoals();

        $strAcademicContent = "Faglig innhold;" . $academicContent->getAcademicContent();

        $strLearningMethods = "Læringsformer og aktiviteter;" . $learningMethods->getLearningMethods();

        $strWorkRequirements = "Arbeidskrav, eksamen og vurdering;" . $workRequirements->getWorkRequirements() .
            "\n\nEksamenstype: " . $examType->getExamType() .
            "\nKarakterskala: " . $gradeScale->getScale() .
            "\n\nKontinuasjon:\nDet gis" . ($courseDescription->isContinuation() ? " " : " ikke ") . "kontinuasjonsadgang for studenter som ikke har bestått siste ordinære arrangerte eksamen i dette emnet.";


        $strLanguages = "Undervisning og eksamensspråk;Undervisning og eksamen vil foregå på " . strtolower($language->getLanguage()) . ".";
        $strLastChangedBy = "Dato for siste endring;Sist endret " . $courseLog->getDateChanged() . " av " . $userService->getUser($courseLog->getUserIdUser())->getFullName() . ".";
        //courseCoordinator
        $strCourseCoordinators = "Emneansvarlig;";
        foreach ($courseCoordinators as $key => $courseCoordinator) {
            $strCourseCoordinators .= $userService->getUser($courseCoordinator->getUserIdUser())->getFullName() . ", ansvarlig for " . $courseCoordinator->getCoursePart() . ".\n";
        }

        //CourseLeader, benyttes som signaturfelt?
        $strCourseLeader = "Studieansvarlig;" . $userService->getUser($courseLeader->getUserIdUser())->getFullName() . ".";

        $strCreatedBy = "Opprettet;Opprettet av " . $user->getFullName() . " ". $courseDescription->getDateCreated() . ".";

        $lines = array($strCourseName,
            $strSemester,
            $strGradeLevel,
            $strSingleCourse,
            $strWebBased,
            $strOffersOnlineStudents,
            $strTeachingLocations,
            $strStudyPoints,
            $strRecommendedPrerequisites,
            $strRequiredPrerequisites,
            $strCompetenceGoals,
            $strAcademicContent,
            $strLearningMethods,
            $strWorkRequirements,
            $strLanguages,
            $strLastChangedBy,
            $strCourseCoordinators,
            $strCourseLeader,
            $strCreatedBy);

        foreach ($lines as $line)
            $this->data[] = explode(';', trim($line));
    }

    /**
     * Generate all content-tables from data loaded by PDF::loadData($idCourse)
     */
    protected function createTable()
    {
        // Header
        $this->SetFillColor(255, 255, 178);
        $this->Cell($this->arrWidths[0], $this->getFontSizePt(), $this->header[0], 'LTB', 0, 'C', 1);
        $this->Cell($this->arrWidths[1], $this->getFontSizePt(), $this->header[1], 'LRTB', 0, 'C', 1);
        $this->Ln();
        $this->setCellPaddings(2, 2, 2, 2);

        // Data
        foreach ($this->data as $row) {
            //0.44167 - Magic number given to me by the muse Cafeina, of the god Bulshitticus.
            //0.46 - Cafeina patched her code...
            $lineCount = max($this->getNumLines($row[0], $this->arrWidths[0]), $this->getNumLines($row[1], $this->arrWidths[1]));
            $h = 0.46 * ($this->getFontSizePt() + 4) * $lineCount;

            //$this->multiCell($this->arrWidths[0],$h,$row[0],'LB', 'L', 0, 0);
            $this->Cell($this->arrWidths[0], $h, $row[0], 'LB', 0, 'C');
            $this->multiCell($this->arrWidths[1], $h, $row[1], 'LRB', 'L');
        }
        // Closing line
        $this->Cell(array_sum($this->arrWidths), 0, '', 'T');
    }

    /**
     * Generate a PDF containing complete course-description for the selected course
     * @param int $idCourse ID of course to generate PDF for.
     * @param bool $download Whether or not the PDF should be sent as inline data[<b>default</b>] or a forced download.
     */
    public static function getPDF(int $idCourse, bool $download = false): void
    {
        // create new PDF document
        $pdf = new PDF(PDF_PAGE_ORIENTATION, PDF_UNIT, PDF_PAGE_FORMAT, true, 'UTF-8', false);

        // Load all data...
        $pdf->LoadData($idCourse);

        // set document information
        $pdf->SetCreator(PDF_CREATOR);
        $pdf->SetAuthor('Autogenerated');
        $pdf->SetTitle($pdf->strHeader);
        $pdf->SetSubject('Emnebeskrivelse for ' . $pdf->strHeader);
        $pdf->SetKeywords($pdf->strHeader);

        // set default header data
        $pdf->SetHeaderData(PDF_HEADER_LOGO, PDF_HEADER_LOGO_WIDTH, PDF_HEADER_TITLE, PDF_HEADER_STRING);

        // set header and footer fonts
        $pdf->setHeaderFont(array(PDF_FONT_NAME_MAIN, '', PDF_FONT_SIZE_MAIN));
        $pdf->setFooterFont(array(PDF_FONT_NAME_DATA, '', PDF_FONT_SIZE_DATA));

        // set default monospaced font
        $pdf->SetDefaultMonospacedFont(PDF_FONT_MONOSPACED);

        // set margins
        $pdf->SetMargins(PDF_MARGIN_LEFT, PDF_MARGIN_TOP, PDF_MARGIN_RIGHT);
        $pdf->SetHeaderMargin(PDF_MARGIN_HEADER);
        $pdf->SetFooterMargin(PDF_MARGIN_FOOTER);

        // set auto page breaks
        $pdf->SetAutoPageBreak(TRUE, PDF_MARGIN_BOTTOM);

        // set image scale factor
        $pdf->setImageScale(PDF_IMAGE_SCALE_RATIO);

        /**/
        //Actually create the page
        $pdf->SetFont('Helvetica', '', 11);
        $pdf->AddPage();
        $pdf->setY($pdf->getY() + 15);
        $pdf->createTable();
        /**/

        //Close and output PDF document
        ob_end_clean();
        $pdf->Output(str_replace(" ", "_", $pdf->strHeader) . '.pdf', $download ? 'D' : 'I');
    }
}