package com.aeta.competition.entity;

public class UserGroup {
    private int id;
    private int groupId;
    private int userId;
    private int status;//0代表未被leader确认，1代表被确认，2代表被拒绝（leader不同意，这个user可以再加入别的队）

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
