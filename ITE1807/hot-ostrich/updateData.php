<?php
spl_autoload_register(function ($class_name) {
    require_once "php/classes/" . $class_name . '.class.php';
});
require_once 'vendor/autoload.php';
@session_start();

$loader = new Twig\Loader\FilesystemLoader('templates');
$twig = new Twig\Environment($loader);

if (isset($_SESSION['bruker'])) {
    $AC = $_SESSION['AC'];
    $inboxService = new InboxService(DB::getDBConnection());
    $currentUser = $_SESSION['bruker'];
    $userId = $_SESSION['bruker']->getIdUser();

    if (isset($_POST['update_course_desc'])) {
        if ($AC->Access_EditCourseDescription()) {
            $courseCodeService = new CourseCodeService(DB::getDBConnection());
            $parentCourseCode = $courseCodeService->getCourseCode($_POST['course_code_id']);
            $courseDescId = $_POST['course_desc_id'];
            $courseDescService = new CourseDescriptionService(DB::getDBConnection());
            $errorMessages = null;

            $year = "0000";
            if (isset($_POST['year'])) {
                $year = $_POST['year'];
            }

            $singleCourse = 0;
            if (isset($_POST['single_course'])) {
                $singleCourse = $_POST['single_course'];
            }

            $continuation = 0;
            if (isset($_POST['continuation'])) {
                $continuation = $_POST['continuation'];
            }

            $semesterSpring = 0;
            if (isset($_POST['semester_spring'])) {
                $semesterSpring = $_POST['semester_spring'];
            }

            $semesterFall = 0;
            if (isset($_POST['semester_fall'])) {
                $semesterFall = $_POST['semester_fall'];
            }


            $teachingLanguage = $_POST['teaching_language'];
            $examForm = $_POST['exam_type'];
            $gradeScale = $_POST['grade_scale'];

            $userId = $_SESSION['bruker']->getIdUser();
            $oldCourseDesc = $courseDescService->getCourseDescription($courseDescId);

            if (!$courseDescService->updateCourseDesc($courseDescId, $year, $singleCourse, $continuation, $semesterFall, $semesterSpring, $teachingLanguage,
                $examForm, $gradeScale)) {
                $errorMessages[] = $courseDescService->getLastError();
            }

            // Teaching location
            $tromso = false;
            if (isset($_POST['tromso'])) {
                if ($_POST['tromso'] == 1) {
                    $tromso = true;
                }
            }

            $narvik = false;
            if (isset($_POST['narvik'])) {
                if ($_POST['narvik'] == 1) {
                    $narvik = true;
                }
            }

            $bodo = false;
            if (isset($_POST['bodo'])) {
                if ($_POST['bodo'] == 1) {
                    $bodo = true;
                }
            }

            $alta = false;
            if (isset($_POST['alta'])) {
                if ($_POST['alta'] == 1) {
                    $alta = true;
                }
            }

            $moIRana = false;
            if (isset($_POST['mo_i_rana'])) {
                if ($_POST['mo_i_rana'] == 1) {
                    $moIRana = true;
                }
            }

            $webBased = false;
            if (isset($_POST['online'])) {
                if ($_POST['online'] == 1) {
                    $webBased = true;
                }
            }

            $locService = new TeachingLocationService(DB::getDBConnection());
            if (!$locService->updateTeachingLocations($oldCourseDesc->getTeachingLocationIdTeachingLocation(), $narvik, $tromso, $alta, $moIRana, $bodo, $webBased)) {
                $errorMessages[] = $locService->getLastError();
            }


            // Offers for online students
            $streaming = false;
            if (isset($_POST['streaming'])) {
                if ($_POST['streaming'] == 1) {
                    $streaming = true;
                }
            }

            $webMeetingLecture = false;
            if (isset($_POST['web_meeting_lecture'])) {
                if ($_POST['web_meeting_lecture'] == 1) {
                    $webMeetingLecture = true;
                }
            }

            $webMeetingEvening = false;
            if (isset($_POST['web_meeting_evening'])) {
                if ($_POST['web_meeting_evening'] == 1) {
                    $webMeetingEvening = true;
                }
            }

            $followup = false;
            if (isset($_POST['followup'])) {
                if ($_POST['followup'] == 1) {
                    $followup = true;
                }
            }

            $organizedArrangements = false;
            if (isset($_POST['organized_arrangements'])) {
                if ($_POST['organized_arrangements'] == 1) {
                    $organizedArrangements = true;
                }
            }

            $other = "";
            if (isset($_POST['online_other'])) {
                $other = $_POST['online_other'];
            }

            $onlineService = new OffersOnlineStudentsService(DB::getDBConnection());
            $oldOnline = $onlineService->getOffersByCourseDescription($oldCourseDesc->getIdCourse());
            if (!$onlineService->updateOffers($oldOnline->getIdOffersOnlineStudents(), $streaming, $webMeetingLecture, $webMeetingEvening, $followup, $organizedArrangements, $other)) {
                $errorMessages[] = $onlineService->getLastError();
            }

            // CourseCoordinators
            $coordinatorService = new CourseCoordinatorService(DB::getDBConnection());
            if (!$coordinatorService->deleteCourseCoordinatorByCourseDescription($courseDescId)) {
                $errorMessages[] = $coordinatorService->getLastError();
            }

            if (isset($_POST['coordinators'])) {
                $courseCoordinators = $_POST['coordinators'];

                foreach ($courseCoordinators as $key => $courseCoordinator) {
                    $field = "Ikke definert.";
                    if (isset($_POST['coordinator_field'][$key])) {
                        $field = $_POST['coordinator_field'][$key];
                    }
                    if (!$coordinatorService->addCourseCoordinator($courseCoordinators[$key], $courseDescId, $field)) {
                        $errorMessages[] = $coordinatorService->getLastError();
                    }
                }
            }


            // Prerequisites
            $prereqService = new PrerequisitesService(DB::getDBConnection());
            if (!$prereqService->deleteAllPrerequisitesByCourseDescription($courseDescId)) {
                $errorMessages[] = $prereqService->getLastError();
            }

            if (isset($_POST['prerequisites'])) {
                $prerequisites = $_POST['prerequisites'];

                foreach ($prerequisites as $key => $prerequisite) {
                    $required = false;
                    if (isset($_POST['prerequisites_required'][$key])) {
                        $required = true;
                    }
                    if (!$prereqService->addPrerequisites($required, $courseDescId, $prerequisite)) {
                        $errorMessages[] = $prereqService->getLastError();
                    }
                }
            }

            // Competence goals
            $compService = new CompetenceGoalsService(DB::getDBConnection());
            $competenceGoals = "";
            $oldCompetenceGoal = $compService->getLastEntryForCourseDescription($courseDescId);
            if (isset($_POST['competence_goals'])) {
                $competenceGoals = $_POST['competence_goals'];
            }

            if (strcmp($competenceGoals, $oldCompetenceGoal->getCompetenceGoals()) != 0) {
                if (!$compService->addEntry($courseDescId, $competenceGoals)) {
                    $errorMessages[] = $compService->getLastError();
                }
            }


            // Academic content
            $academicService = new AcademicContentService(DB::getDBConnection());
            $academicContent = "";
            $oldAcademicContent = $academicService->getLastEntryForCourseDescription($courseDescId);
            if (isset($_POST['academic_content'])) {
                $academicContent = $_POST['academic_content'];
            }

            if (strcmp($academicContent, $oldAcademicContent->getAcademicContent()) != 0) {
                if (!$academicService->addEntry($courseDescId, $academicContent)) {
                    $errorMessages[] = $academicService->getLastError();
                }
            }


            // Learning methods
            $learningService = new LearningMethodsService(DB::getDBConnection());
            $learningMethods = "";
            $oldLearningMethods = $learningService->getLastEntryForCourseDescription($courseDescId);
            if (isset($_POST['learning_methods'])) {
                $learningMethods = $_POST['learning_methods'];
            }

            if (strcmp($learningMethods, $oldLearningMethods->getLearningMethods()) != 0) {
                if (!$learningService->addEntry($courseDescId, $learningMethods)) {
                    $errorMessages[] = $learningService->getLastError();
                }
            }


            // Work requirements
            $workReqService = new WorkRequirementsService(DB::getDBConnection());
            $workRequirements = "";
            $oldWorkRequirements = $workReqService->getLastEntryForCourseDescription($courseDescId);
            if (isset($_POST['work_requirements'])) {
                $workRequirements = $_POST['work_requirements'];
            }

            if (strcmp($workRequirements, $oldWorkRequirements->getWorkRequirements()) != 0) {
                if (!$workReqService->addEntry($courseDescId, $workRequirements)) {
                    $errorMessages[] = $workReqService->getLastError();
                }
            }

            if (is_null($errorMessages)) {
                $desc = "Emnebeskrivelse for emnekode " . $parentCourseCode . " med id " . $oldCourseDesc->getIdCourse() .
                    " har blitt oppdatert av " . $currentUser->getFullName() . ".";
                if (isset($_POST['coordinators'])) {
                    $courseCoordinators = $_POST['coordinators'];
                    foreach ($courseCoordinators as $courseCoordinator) {
                        $inboxService->addMessage($parentCourseCode->getCourseCode(), $desc, 4, $courseCoordinator);
                    }
                }
                echo $twig->render('displayMessage.twig', array(
                    'user' => $_SESSION['bruker'],
                    'notificationTypes' => $inboxService->getAllInboxTypes(),
                    'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
                    'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
                    'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
                    'message_title' => 'Suksess!',
                    'message_description' => 'Emnekoden har blitt oppdatert.<br><a href="courseDescription.php?id=' . $courseDescId . '"><- Tilbake til emnebeskrivelsen</a>'
                ));
            } else {
                echo $twig->render('displayMessage.twig', array(
                    'user' => $_SESSION['bruker'],
                    'notificationTypes' => $inboxService->getAllInboxTypes(),
                    'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
                    'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
                    'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
                    'message_title' => 'Feil!',
                    'message_description' => $errorMessages
                ));
            }
        } else {
            echo $twig->render('displayMessage.twig', array(
                'user' => $_SESSION['bruker'],
                'notificationTypes' => $inboxService->getAllInboxTypes(),
                'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
                'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
                'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
                'message_title' => "Feil",
                'message_description' => "Det har skjedd en feil, kontroller at du har adgang til funksjonen med din administrator"));
        }
    } else if (isset($_POST['update_course_code'])) {
        if ($AC->Access_EditCourseCode()) {
            $courseCodeService = new CourseCodeService(DB::getDBConnection());
            $courseCodeId = $_POST['course_code_id'];
            $courseCode = $_POST['course_code'];
            $courseNameNbNo = $_POST['course_name_nb_no'];
            $courseNameNbNn = $_POST['course_name_nb_nn'];
            $courseNameEnGb = $_POST['course_name_en_gb'];
            $degreeId = $_POST['degree'];
            $studyPointsId = $_POST['ects'];
            $courseLeaderId = $_POST['course_leader'];

            if ($courseCodeService->updateCourseCode($courseCodeId, $courseCode, $courseNameNbNo, $courseNameNbNn, $courseNameEnGb, $degreeId, $studyPointsId, $courseLeaderId)) {
                $desc = "Emnekode " . $courseCode . " har blitt oppdatert av " . $currentUser->getFullName() . ".";
                $type = 3;

                if ($inboxService->addMessage($courseCode, $desc, $type, $courseLeaderId)) {
                    echo $twig->render('displayMessage.twig', array(
                        'user' => $_SESSION['bruker'],
                        'notificationTypes' => $inboxService->getAllInboxTypes(),
                        'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
                        'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
                        'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
                        'message_title' => "Emnekode oppdatert",
                        'message_description' => "Emneansvarlig for '" . $courseCode . "' har blitt varslet."
                    ));
                } else {
                    echo $twig->render('displayMessage.twig', array(
                        'user' => $_SESSION['bruker'],
                        'notificationTypes' => $inboxService->getAllInboxTypes(),
                        'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
                        'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
                        'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
                        'message_title' => "Feil",
                        'message_description' => "Emnekoden ble oppdatert, men det oppsto en feil ved oppretting av varsel til den ansvarlige."
                    ));
                }
            } else {
                echo $twig->render('displayMessage.twig', array(
                    'user' => $_SESSION['bruker'],
                    'notificationTypes' => $inboxService->getAllInboxTypes(),
                    'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
                    'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
                    'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
                    'message_title' => "Feil",
                    'message_description' => "Det oppsto et problem ved oppdatering av emnekode '" . $courseCode . "'\n" . $courseCodeService->getLastError()
                ));
            }
        } else {
            echo $twig->render('displayMessage.twig', array(
                'user' => $_SESSION['bruker'],
                'notificationTypes' => $inboxService->getAllInboxTypes(),
                'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
                'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
                'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
                'message_title' => "Ingen adgang",
                'message_description' => "Du har ikke adgang til denne ressursen."
            ));
        }
    }
} else {
    echo $twig->render('displayMessage.twig', array(
        'message_title' => "Ingen adgang",
        'message_description' => "Du har ikke adgang til denne ressursen."
    ));
}