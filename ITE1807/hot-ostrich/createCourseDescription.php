<?php
/* Prerequisites */
spl_autoload_register(function ($class_name) {
    require_once "php/classes/" . $class_name . '.class.php';
});
require_once 'vendor/autoload.php';
@session_start();

/* User And active login variables. */
$loader = new Twig\Loader\FilesystemLoader('templates');


$twig = new Twig\Environment($loader, array('cache' => false));

// If we aren't logged in, ask to login...
if (!isset($_SESSION['AC'])) {
    require_once 'login.php';
    echo $twig->render('logInPage.twig');
} else {// if logged in, proceed with course creation
    $AC = $_SESSION['AC'];
    $inboxService = new InboxService(DB::getDBConnection());
    $userId = $_SESSION['bruker']->getIdUser();

    //Do we have the rights to create a coursedescription?
    if ($_SESSION['AC']->Access_CreateCourseDescription()) {
        // Get some data we'll need l8r
        $courseCodes = (new CourseCodeService(DB::getDBConnection()))->getAllCourseCodes();
        $users = (new UserService(DB::getDBConnection()))->getAllUsers();
        $languages = (new LanguageService(DB::getDBConnection()))->getAllLanguages();
        $examTypes = (new ExamTypeService(DB::getDBConnection()))->getAllExamTypes();
        $gradeScales = (new GradeScaleService(DB::getDBConnection()))->getAllGradeScales();

        // Have we submitted the data?
        if (isset($_POST["submit"])) {
            $error = false;

            // check values submitted
            $storedPostVars = array();
            if (isset($_POST["idCourseCode"])) {
                $storedPostVars["idCourseCode"] = $_POST["idCourseCode"];
                $matchFound = false;
                foreach ($courseCodes as $courseCode) {
                    if ($courseCode->getIdCourseCode() == $storedPostVars["idCourseCode"])
                        $matchFound = true;
                }
                if (!$matchFound) {
                    $error = true;
                }
            }
            if (isset($_POST["course_overview_year"])) {
                $storedPostVars["course_overview_year"] = $_POST["course_overview_year"];
                if ($storedPostVars["course_overview_year"] < date("Y") || $storedPostVars["course_overview_year"] > 2100) {
                    $error = true;
                }
            }

            if (isset($_POST["single_course"])) {
                $storedPostVars["single_course"] = $_POST["single_course"];
            } else {
                $storedPostVars["single_course"] = 0;
            }
            if (isset($_POST["continuation"])) {
                $storedPostVars["continuation"] = $_POST["continuation"];
            } else {
                $storedPostVars["continuation"] = 0;
            }
            if (isset($_POST["semester_spring"])) {
                $storedPostVars["semester_spring"] = $_POST["semester_spring"];
            } else {
                $storedPostVars["semester_spring"] = 0;
            }
            if (isset($_POST["semester_fall"])) {
                $storedPostVars["semester_fall"] = $_POST["semester_fall"];
            } else {
                $storedPostVars["semester_fall"] = 0;
            }

            if (isset($_POST["language"])) {
                $storedPostVars["language"] = $_POST["language"];
                $matchFound = false;
                foreach ($languages as $language) {
                    if ($language->getIdLanguage() === intval($storedPostVars["language"]))
                        $matchFound = true;
                }
                if (!$matchFound) {
                    $error = true;
                }
            }

            if (isset($_POST["exam_type"])) {
                $storedPostVars["exam_type"] = $_POST["exam_type"];
                $matchFound = false;
                foreach ($examTypes as $examType) {
                    if ($examType->getIdExamType() === intval($storedPostVars["exam_type"]))
                        $matchFound = true;
                }
                if (!$matchFound) {
                    $error = true;
                }
            }
            if (isset($_POST["grade_scale"])) {
                $storedPostVars["grade_scale"] = $_POST["grade_scale"];
                $matchFound = false;
                foreach ($gradeScales as $gradeScale) {
                    if ($gradeScale->getIdGradeScale() === intval($storedPostVars["grade_scale"]))
                        $matchFound = true;
                }
                if (!$matchFound) {
                    $error = true;
                }
            }

            if (isset($_POST["courseCoordinator1"])) {
                $storedPostVars["courseCoordinator1"] = $_POST["courseCoordinator1"];
                $matchFound = false;
                foreach ($users as $user) {
                    if ($user->getIdUser() === intval($storedPostVars["courseCoordinator1"]))
                        $matchFound = true;
                }
                if (!$matchFound) {
                    $error = true;
                }
            }
            if (isset($_POST["coursePart1"])) {
                $storedPostVars["coursePart1"] = $_POST["coursePart1"];
                if (strlen($storedPostVars["coursePart1"]) > 45 || strlen($storedPostVars["coursePart1"]) <= 0) {
                    $error = true;
                }
            }

            if (isset($_POST["courseCoordinator2"])) {
                if ($_POST["courseCoordinator2"] != 0) {
                    $storedPostVars["courseCoordinator2"] = $_POST["courseCoordinator2"];
                    $matchFound = false;
                    foreach ($users as $user) {
                        if ($user->getIdUser() === intval($storedPostVars["courseCoordinator2"]))
                            $matchFound = true;
                    }
                    if (!$matchFound) {
                        $error = true;
                    }
                    if (isset($_POST["coursePart2"])) {
                        $storedPostVars["coursePart2"] = $_POST["coursePart2"];
                        if (strlen($storedPostVars["coursePart2"]) > 45 || strlen($storedPostVars["coursePart2"]) <= 0) {
                            $error = true;
                        }
                    }
                }
            }


            if (isset($_POST["narvik"])) {
                $storedPostVars["narvik"] = $_POST["narvik"];
            } else {
                $storedPostVars["narvik"] = 0;
            }
            if (isset($_POST["tromso"])) {
                $storedPostVars["tromso"] = $_POST["tromso"];
            } else {
                $storedPostVars["tromso"] = 0;
            }
            if (isset($_POST["alta"])) {
                $storedPostVars["alta"] = $_POST["alta"];
            } else {
                $storedPostVars["alta"] = 0;
            }
            if (isset($_POST["mo_i_rana"])) {
                $storedPostVars["mo_i_rana"] = $_POST["mo_i_rana"];
            } else {
                $storedPostVars["mo_i_rana"] = 0;
            }
            if (isset($_POST["bodo"])) {
                $storedPostVars["bodo"] = $_POST["bodo"];
            } else {
                $storedPostVars["bodo"] = 0;
            }
            if (isset($_POST["online"])) {
                $storedPostVars["online"] = $_POST["online"];

                if (isset($_POST["streaming"])) {
                    $storedPostVars["streaming"] = $_POST["streaming"];
                } else {
                    $storedPostVars["streaming"] = 0;
                }
                if (isset($_POST["web_meeting_lecture"])) {
                    $storedPostVars["web_meeting_lecture"] = $_POST["web_meeting_lecture"];
                } else {
                    $storedPostVars["web_meeting_lecture"] = 0;
                }
                if (isset($_POST["web_meeting_evening"])) {
                    $storedPostVars["web_meeting_evening"] = $_POST["web_meeting_evening"];
                } else {
                    $storedPostVars["web_meeting_evening"] = 0;
                }
                if (isset($_POST["followup"])) {
                    $storedPostVars["followup"] = $_POST["followup"];
                } else {
                    $storedPostVars["followup"] = 0;
                }
                if (isset($_POST["organized_arrangements"])) {
                    $storedPostVars["organized_arrangements"] = $_POST["organized_arrangements"];
                } else {
                    $storedPostVars["organized_arrangements"] = 0;
                }
                if (isset($_POST["other"])) {
                    $storedPostVars["other"] = $_POST["other"];
                    if ($storedPostVars["other"] > 1000) {
                        $error = true;
                    }
                }
            } else {
                $storedPostVars["online"] = 0;
                $storedPostVars["streaming"] = 0;
                $storedPostVars["web_meeting_lecture"] = 0;
                $storedPostVars["web_meeting_evening"] = 0;
                $storedPostVars["followup"] = 0;
                $storedPostVars["organized_arrangements"] = 0;
                $storedPostVars["other"] = "";
            }

            if (isset($_POST["prerequisites"])) {
                $storedPostVars["prerequisites"] = $_POST["prerequisites"];
                array_splice($storedPostVars["prerequisites"], 1, 1);
                $matchFound = 0;
                foreach ($courseCodes as $courseCode) {
                    foreach ($storedPostVars["prerequisites"] as $idCourseDesc) {
                        if ($courseCode->getIdCourseCode() == $idCourseDesc)
                            $matchFound++;
                    }
                }
                if ($matchFound != sizeof($storedPostVars["prerequisites"])) {
                    $error = true;
                }
            }

            if (isset($_POST["prerequisites_required"])) {
                $storedPostVars["prerequisites_required"] = $_POST["prerequisites_required"];
                foreach ($storedPostVars["prerequisites_required"] as $required) {
                    if ($required < 0)
                        $error = true;
                }
            }

            if (isset($_POST["competence_goals"])) {
                $storedPostVars["competence_goals"] = $_POST["competence_goals"];
                if (strlen($storedPostVars["competence_goals"] > 6000)) {
                    $error = true;
                }
            }
            if (isset($_POST["academic_content"])) {
                $storedPostVars["academic_content"] = $_POST["academic_content"];
                if (strlen($storedPostVars["academic_content"] > 1800)) {
                    $error = true;
                }
            }
            if (isset($_POST["learning_methods"])) {
                $storedPostVars["learning_methods"] = $_POST["learning_methods"];
                if (strlen($storedPostVars["learning_methods"] > 900)) {
                    $error = true;
                }
            }
            if (isset($_POST["work_requirements"])) {
                $storedPostVars["work_requirements"] = $_POST["work_requirements"];
                if (strlen($storedPostVars["work_requirements"] > 3000)) {
                    $error = true;
                }
            }

            if ($error) {
                // Values weren't correct, retry!
                $storedPostVars["error"] = $error;
                echo $twig->render('createCourseDescription.twig', array(
                    'user' => $_SESSION['bruker'],
                    'notificationTypes' => $inboxService->getAllInboxTypes(),
                    'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
                    'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
                    'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
                    'courseCodes' => $courseCodes,
                    'users' => $users,
                    'languages' => $languages,
                    'examTypes' => $examTypes,
                    'gradeScales' => $gradeScales,
                    'storedPostVars' => $storedPostVars
                ));
            } else {
                // Create course
                $courseDescriptionService = new CourseDescriptionService(DB::getDBConnection());

                //$Service = new Service(DB::getDBConnection());

                $prerequisitesService = new PrerequisitesService(DB::getDBConnection());
                $offersOnlineStudentsService = new OffersOnlineStudentsService(DB::getDBConnection());
                $approvalService = new ApprovalService(DB::getDBConnection());
                $courseCoordinatorService = new CourseCoordinatorService(DB::getDBConnection());
                $teachingLocationService = new TeachingLocationService(DB::getDBConnection());

                $courseLogService = new CourseLogService(DB::getDBConnection());

                $academicContentService = new AcademicContentService(DB::getDBConnection());
                $learningMethodsService = new LearningMethodsService(DB::getDBConnection());
                $competenceGoalsService = new CompetenceGoalsService(DB::getDBConnection());
                $workRequirementsService = new WorkRequirementsService(DB::getDBConnection());


                $teachingLocationService->addTeachingLocationEntry($storedPostVars["narvik"],
                    $storedPostVars["tromso"],
                    $storedPostVars["alta"],
                    $storedPostVars["mo_i_rana"],
                    $storedPostVars["bodo"],
                    $storedPostVars["online"]
                );

                $courseDescriptionService->addCourseDescription($storedPostVars["idCourseCode"],
                    $storedPostVars["course_overview_year"],
                    $storedPostVars["single_course"],
                    $storedPostVars["continuation"],
                    $storedPostVars["semester_fall"],
                    $storedPostVars["semester_spring"],
                    CourseDescription::ARCHIVED_FALSE,
                    $_SESSION["bruker"]->getIdUser(),
                    $storedPostVars["language"],
                    $storedPostVars["exam_type"],
                    $storedPostVars["grade_scale"],
                    $teachingLocationService->getLastEntry()->getIdTeachingLocation(),
                    1);

                $idCourse = $courseDescriptionService->getLastEntry()->getIdCourse();
                $offersOnlineStudentsService->addOffersForCourseDescriptions($idCourse,
                    $storedPostVars["streaming"],
                    $storedPostVars["web_meeting_lecture"],
                    $storedPostVars["web_meeting_evening"],
                    $storedPostVars["followup"],
                    $storedPostVars["organized_arrangements"],
                    $storedPostVars["other"]);

                $i = 0;
                foreach ($storedPostVars["prerequisites"] as $prerequisite) {
                    //FIXME: If no prerequisite is selected, it will add itself as a prerequisite...
                    $isRequired = 0;
                    if(isset($storedPostVars["prerequisites_required"])) {
                        foreach ($storedPostVars["prerequisites_required"] as $requiredPrerequisite) {
                            if ($requiredPrerequisite == $i)
                                $isRequired = 1;
                        }
                    }
                    $prerequisitesService->addPrerequisites($isRequired, $idCourse, $storedPostVars["idCourseCode"],);
                    $i++;
                }

                $date = date("Y-m-d H:i:s");
                $dateDeadline = date('Y-m-d H:i:s', strtotime($date . ' + 30 days'));
                $approvalService->addApproval($dateDeadline, $idCourse);

                $courseLogService->addCourseLog($_SESSION["bruker"]->getIdUser(), $idCourse);
                $courseCoordinatorService->addCourseCoordinator($storedPostVars["courseCoordinator1"], $idCourse, $storedPostVars["coursePart1"]);
                if (isset($storedPostVars["courseCoordinator2"])) {
                    $courseCoordinatorService->addCourseCoordinator($storedPostVars["courseCoordinator2"], $idCourse, $storedPostVars["coursePart2"]);
                }

                $academicContentService->addEntry($idCourse, $storedPostVars["academic_content"]);
                $learningMethodsService->addEntry($idCourse, $storedPostVars["learning_methods"]);
                $competenceGoalsService->addEntry($idCourse, $storedPostVars["competence_goals"]);
                $workRequirementsService->addEntry($idCourse, $storedPostVars["work_requirements"]);

                echo $twig->render('createCourseDescription.twig', array(
                    'user' => $_SESSION['bruker'],
                    'notificationTypes' => $inboxService->getAllInboxTypes(),
                    'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
                    'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
                    'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
                    'courseCreated' => true
                ));
            }

        } else {
            // nothing submitted, show default CourseCreation page

            echo $twig->render('createCourseDescription.twig', array(
                'user' => $_SESSION['bruker'],
                'notificationTypes' => $inboxService->getAllInboxTypes(),
                'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
                'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
                'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
                'users' => $users,
                'courseCodes' => $courseCodes,
                'languages' => $languages,
                'examTypes' => $examTypes,
                'gradeScales' => $gradeScales
            ));
        }
    } else {
        // No rights to create a courseDescription, display error-message
        echo "Error: Insufficient rights to create course-description!";

        echo $twig->render('createCourseDescription.twig', array(
            'user' => $_SESSION['bruker'],
            'notificationTypes' => $inboxService->getAllInboxTypes(),
            'unreadNotifications' => $inboxService->getTotalAmountOfUnreadMessages($userId),
            'unreadNotificationsWithType' => $inboxService->getAmountOfUnreadMessagesWithType($userId),
            'userRole' => $_SESSION['AC']->getUserRole()->getRoleName(),
            'insufficientRights' => true
        ));
    }

    if (isset($_POST['logout-submit'])) {
        $_SESSION['bruker']->logOut();
        echo ' <script> window.location.replace("index.php"); </script>'; //TODO: Plz ignore, will be replaced in production version.
    }
}