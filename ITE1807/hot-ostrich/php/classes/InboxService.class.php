<?php

class InboxService
{
    private PDO $db;
    private array $errorMsgs;

    /**
     * CourseDescriptionService.class constructor.
     * Throws InvalidArgumentException if no valid database-object passed.
     * @param $db Database-object to use for the connection
     */
    public function __construct(?PDO $db)
    {
        //Throw exception if we get a NULL object
        if (is_NULL($db)) {
            throw new InvalidArgumentException("No valid database-object passed: \$db=$db");
        } else {
            $this->db = $db;
        }
    }

    /**
     * Currently limited to return 50 messages.
     * @param int $userId
     * @return array|null
     */
    public function getAllMessages(int $userId): ?array
    {
        $messages = array();
        try {
            $stmt = $this->db->prepare("SELECT Inbox.id, Inbox.title, Inbox.date, Inbox.opened, InboxType.name FROM Inbox, InboxType 
                                                 WHERE Inbox.type = InboxType.id AND Inbox.user_id = :userId ORDER BY date DESC LIMIT 50");
            if ($stmt->execute(array(
                'userId' => $userId
            ))) {
                while ($message = $stmt->fetchObject("InboxAll")) {
                    $messages[] = $message;
                }
                return $messages;
            } else {
                $this->errorMsgs = $stmt->errorInfo();
                return NULL;
            }
        } catch (InvalidArgumentException $e) {
            print $e->getMessage() . PHP_EOL;
        }
        return NULL;
    }

    public function getMessage(int $messageId, int $userId): ?InboxIndividual
    {
        try {
            $sth = $this->db->prepare("SELECT Inbox.id, Inbox.title, Inbox.description, Inbox.date, InboxType.name, Inbox.opened from Inbox, InboxType 
                                                WHERE Inbox.id = :id AND Inbox.type = InboxType.id AND Inbox.user_id = :userId");
            if ($sth->execute(array(
                ':id' => $messageId,
                ':userId' => $userId
            ))) {
                return $sth->fetchObject("InboxIndividual");
            } else {
                $this->errorMsgs = $sth->errorInfo();
                return null;
            }
        } catch (InvalidArgumentException $e) {
            print $e->getMessage() . PHP_EOL;
        }
        return null;
    }

    public function addMessage(string $title, string $desc, int $type, int $userId): bool
    {
        try {
            $sth = $this->db->prepare("INSERT INTO Inbox(title, description, type, user_id) VALUES(:title, :msg_desc, :msg_type, :userId)");
            if ($sth->execute(array(
                'title' => htmlspecialchars($title),
                'msg_desc' => htmlspecialchars($desc),
                'msg_type' => htmlspecialchars($type),
                'userId' => htmlspecialchars($userId)
            ))) {
                return true;
            } else {
                $this->errorMsgs = $sth->errorInfo();
                return false;
            }
        } catch (InvalidArgumentException $e) {
            print $e->getMessage() . PHP_EOL;
        }
        return false;
    }

    public function getAllInboxTypes(): ?array
    {
        $inboxTypes = array();
        try {
            $sth = $this->db->prepare("SELECT name FROM InboxType");
            if ($sth->execute()) {
                while ($inboxType = $sth->fetch()) {
                    $inboxTypes[] = $inboxType;
                }
                return $inboxTypes;
            }
            $this->errorMsgs[] = $sth->errorInfo();
        } catch (Exception $e) {
            $this->errorMsgs[] = $e;
        }
        return null;
    }

    public function getTotalAmountOfUnreadMessages(int $userId): ?int
    {
        try {
            $sth = $this->db->prepare("SELECT COUNT(*) FROM Inbox WHERE user_id = :userId AND opened = 0");
            if ($sth->execute(array(
                'userId' => $userId
            ))) {
                return $sth->fetchColumn();
            } else {
                $this->errorMsgs = $sth->errorInfo();
            }
        } catch (Exception $e) {
            $this->errorMsgs[] = $e;
        }
        return null;
    }

    public function getAmountOfUnreadMessagesWithType($userId): ?array
    {
        $inboxTypes = array();
        try {
            $sth = $this->db->prepare("SELECT COUNT(opened) AS unread, name FROM Inbox, InboxType WHERE Inbox.type = InboxType.id
                                                AND user_id = :userId AND opened = 0 GROUP BY name");
            if ($sth->execute(array(
                'userId' => $userId
            ))) {
                while ($inboxType = $sth->fetch()) {
                    $inboxTypes[] = $inboxType;
                }
                return $inboxTypes;
            }
            $this->errorMsgs[] = $sth->errorInfo();
        } catch (Exception $e) {
            $this->errorMsgs[] = $e;
        }
        return null;
    }

    public function openMessage($messageId, $userId): void
    {
        try {
            $sth = $this->db->prepare("UPDATE Inbox SET opened = 1 WHERE id = :messageId AND user_id = :userId");
            if (!$sth->execute(array(
                'messageId' => $messageId,
                'userId' => $userId))) {
                $this->errorMsgs[] = $sth->errorInfo();
            }
        } catch (Exception $e) {
            $this->errorMsgs[] = $e;
        }
    }

    /**
     * Returns array $errorMsgs
     * @return array Array containing the last generated error-message
     */
    public function getLastError(): array
    {
        // TODO: Format array as a single string
        return $this->errorMsgs;
    }
}