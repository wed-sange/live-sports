package com.edi.commons.model.dto;

public class SearchDto {
    private Integer number = 1;
    private Integer size = 10;

    public Integer getNumber() {
        if (this.number < 1) {
            this.number = 1;
        } else if (this.number > 50) {
            this.number = 50;
        }

        return this.number;
    }

    public Integer getSize() {
        if (this.size < 1) {
            this.size = 10;
        } else if (this.size > 2000) {
            this.size = 2000;
        }

        return this.size;
    }

    public SearchDto() {
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
