package com.iskandar.gdx.dto;


/**
 * Created by Iskandar on 2018/9/1.
 */
public class ModuleDto {
    private Integer id;
    private Integer pId;
    private String name;
    private Boolean checked = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
