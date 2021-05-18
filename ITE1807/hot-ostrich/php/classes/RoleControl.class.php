<?php


class RoleControl
{
    private int $idRole;
    private String $role;
    private int $read;
    private int $write;
    private int $edit;
    private int $delete;
    private int $create;
    private int $approve;

    /**
     * RoleControl constructor.
     */
    public function __construct(){}

    /**
     * @return int
     */
    public function getIdRole(): int
    {
        return $this->idRole;
    }

    /**
     * @param int $idRole
     */
    public function setIdRole(int $idRole): void
    {
        $this->idRole = $idRole;
    }

    /**
     * @return String
     */
    public function getRole(): string
    {
        return $this->role;
    }

    /**
     * @param String $role
     */
    public function setRole(string $role): void
    {
        $this->role = $role;
    }

    /**
     * @return String
     */
    public function getRoleName(): String
    {
        return $this->role;
    }

    /**
     * @param String $role
     */
    public function setRoleName(String $role): void
    {
        $this->role = $role;
    }

    /**
     * @return int
     */
    public function getRead(): int
    {
        return $this->read;
    }

    /**
     * @param int $read
     */
    public function setRead(int $read): void
    {
        $this->read = $read;
    }

    /**
     * @return int
     */
    public function getWrite(): int
    {
        return $this->write;
    }

    /**
     * @param int $write
     */
    public function setWrite(int $write): void
    {
        $this->write = $write;
    }

    /**
     * @return int
     */
    public function getEdit(): int
    {
        return $this->edit;
    }

    /**
     * @param int $edit
     */
    public function setEdit(int $edit): void
    {
        $this->edit = $edit;
    }

    /**
     * @return int
     */
    public function getDelete(): int
    {
        return $this->delete;
    }

    /**
     * @param int $delete
     */
    public function setDelete(int $delete): void
    {
        $this->delete = $delete;
    }

    /**
     * @return int
     */
    public function getCreate(): int
    {
        return $this->create;
    }

    /**
     * @param int $create
     */
    public function setCreate(int $create): void
    {
        $this->create = $create;
    }

    /**
     * @return int
     */
    public function getApprove(): int
    {
        return $this->approve;
    }

    /**
     * @param int $approve
     */
    public function setApprove(int $approve): void
    {
        $this->approve = $approve;
    }





}